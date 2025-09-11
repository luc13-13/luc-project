package com.lc.framework.data.permission.interceptor;

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.BaseMultiTableInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.lc.framework.core.utils.ReflectionUtils;
import com.lc.framework.data.permission.anno.DataScope;
import com.lc.framework.data.permission.entity.DataScopeEntity;
import com.lc.framework.data.permission.entity.DataScopeWrapper;
import com.lc.framework.data.permission.handler.IDataPermissionSqlHandler;
import com.lc.framework.data.permission.utils.PluginUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;

/**
 * <pre>
 *     实现mybatis-plus提供的内部拦截器，拦截sql执行
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-01 10:34
 */
@Slf4j
public class LucDataPermissionInterceptor extends BaseMultiTableInnerInterceptor implements InnerInterceptor {

    /**
     * 封装需要校验的表名与处理器， key为表， value为处理器bean， 在配置文件中设置
     */
    private final Map<Class<? extends IDataPermissionSqlHandler>, IDataPermissionSqlHandler> registeredHandler;

    public LucDataPermissionInterceptor(Map<Class<? extends IDataPermissionSqlHandler>, IDataPermissionSqlHandler> registeredHandler) {
        Assert.notEmpty(registeredHandler, "handler must non null!");
        this.registeredHandler = registeredHandler;
    }

    public static final String DATA_SCOPE_PARAM_NAME = "dataScopeEntity";

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        if (MappedStatementCache.isNotAnnotated(ms)) {
            log.info("beforeQuery: cached not annotated method, {}", ms.getId());
            return;
        }
        // 从方法参数中获取权限属性，并像缓存中放入DataScope
        DataScope dataScopeAnno = getAnnotationDataScope(ms);
        // 获取方法中的权限参数
        DataScopeEntity dataScopeEntity = getDataScopeEntity(parameter);
        // 获取注解声明的权限处理器
        List<IDataPermissionSqlHandler> requiredHandlers = getRequiredHandlers(dataScopeAnno);
        if (skip(ms, dataScopeAnno, dataScopeEntity)) {
            return;
        }
        try {
            PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
            DataScopeContextHolder.putDataScopeWrapper(dataScopeAnno.isMutex(), dataScopeEntity, requiredHandlers);
            mpBs.sql(parserMulti(mpBs.sql(), ms.getId()));
        } finally {
            // 根据本地线程中的REWRITE状态，判断当前sql是否被重写，如果没被重写，则放入缓存中，下次无需进行解析
            MappedStatementCache.addIgnoredMapStatement(ms, requiredHandlers);
            // 解析方法执行完毕，清理本地缓存，避免OOM
            DataScopeContextHolder.clear();
        }
    }

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        // 将代理对象转为真正的实体对象
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        // 获取mybatis对执行sql的解析结果
        MappedStatement ms = mpSh.mappedStatement();
        // 获取执行方法的参数对象
        Object paramObj = mpSh.boundSql().getParameterObject();
        // 从方法参数中获取权限属性，并向缓存中放入DataScope
        DataScope dataScopeAnno = getAnnotationDataScope(ms);
        // 获取方法中的权限参数
        DataScopeEntity dataScopeEntity = getDataScopeEntity(paramObj);
        if (skip(ms, dataScopeAnno, dataScopeEntity)) {
            return;
        }
        // 获取权限处理器
        List<IDataPermissionSqlHandler> requiredHandlers = getRequiredHandlers(dataScopeAnno);

        SqlCommandType sct = ms.getSqlCommandType();
        if (sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE || sct == SqlCommandType.SELECT) {
            if (MappedStatementCache.willIgnored(ms, requiredHandlers)) {
                // 如果sql不在handler的处理范围内， 则直接返回
                return;
            }
            // 将当前线程的DataScope与DataScopeEntity进行封装并缓存，供后续方法调用
            try {
                DataScopeContextHolder.putDataScopeWrapper(dataScopeAnno.isMutex(), dataScopeEntity, requiredHandlers);
                PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
                mpBs.sql(parserMulti(mpBs.sql(), ms.getId()));
            } finally {
                // 根据本地线程中的REWRITE状态，判断当前sql是否被重写，如果没被重写，则放入缓存中，下次无需进行解析
                MappedStatementCache.addIgnoredMapStatement(ms, requiredHandlers);
                // 解析方法执行完毕，清理本地缓存，避免OOM
                DataScopeContextHolder.clear();
            }
        }

    }

    /**
     * 处理select和where语句
     *
     * @author Lu Cheng
     * @create 2023/8/4
     */
    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        final String whereSegment = (String) obj;
        processSelectBody(select, whereSegment);
        List<WithItem<?>> withItemsList = select.getWithItemsList();
        if (CollectionUtils.isNotEmpty(withItemsList)) {
            withItemsList.forEach(withItem -> processSelectBody(withItem.getSelect(), (String) obj));
        }
    }

    /**
     * <pre>
     * {@code
     * UPDATE table_name
     * SET column_1 = (SELECT other FROM other_table WHERE condition)
     * SET column_2 = value
     * }
     *
     * <pre/>
     * （1）追加update语句中select条件
     * （2）追加update语句条件
     */
    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        List<UpdateSet> updateSets = update.getUpdateSets();
        if (CollectionUtils.isNotEmpty(updateSets)) {
            // 处理update语句中的select
            updateSets.forEach(updateSet -> {
                updateSet.getValues().forEach(value -> {
                    if (value instanceof Select selectValue) {
                        this.processSelectBody(selectValue, (String) obj);
                    }
                });
            });
        }
        final Expression sqlSegment = this.andExpression(update.getTable(), update.getWhere(), (String) obj);
        if (sqlSegment != null) {
            update.setWhere(sqlSegment);
        }
    }

    /**
     * 向DELETE语句追加WHERE条件（无法处理）
     */
    @Override
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        final Expression sqlSegment = this.andExpression(delete.getTable(), delete.getWhere(), (String) obj);
        if (sqlSegment != null) {
            delete.setWhere(sqlSegment);
        }
    }

    /**
     * @author Lu Cheng
     * @desc
     * @create 2023/8/2
     */
    private DataScope getAnnotationDataScope(MappedStatement ms) {
        return MappedStatementCache.getDataScopeAnno(ms.getId(), () -> {
            String id = ms.getId();
            // 未命中本地缓存，根据方法名解析
            DataScope anno;
            try {
                Class<?> clazz = Class.forName(id.substring(0, id.lastIndexOf(".")));
                Method method = ReflectionUtils.getFirstMethod(clazz, id.substring(id.lastIndexOf(".") + 1));
                anno = ReflectionUtils.getAnnotation(method, DataScope.class);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return anno;
        });
    }

    private static boolean skip(MappedStatement ms, DataScope dataScopeAnno, DataScopeEntity dataScopeEntity) {
        if (Objects.isNull(dataScopeAnno) || ArrayUtils.isEmpty(dataScopeAnno.rules())) {
            // 方法上没有权限注解、权限注解中没有声明权限处理器，缓存方法id
            MappedStatementCache.addNotAnnotatedMapStatement(ms);
            return true;
        }
        // 方法上没有权限注解、权限参数为null、权限注解中没有声明权限字段，则直接返回，不对sql做额外处理
        return Objects.isNull(dataScopeEntity);
    }

    /**
     * mybatis对方法参数的解析方式：
     * 一个参数时，直接使用原参数；
     * 多个参数时，用Map封装，取参数名为key
     *
     * @author Lu Cheng
     * @create 2023/8/1
     */
    private DataScopeEntity getDataScopeEntity(Object parameterObject) {
        if (parameterObject == null) {
            return null;
        }
        if (parameterObject instanceof DataScopeEntity entity) {
            return entity;
        } else if (parameterObject instanceof Map<?, ?> map && map.get(DATA_SCOPE_PARAM_NAME) instanceof DataScopeEntity entity) {
            return entity;
        } else {
            return null;
        }
    }

    /**
     * 获取注解中指定的权限处理器，如果没有指定注解，则使用所有生效的处理器，每个处理器的结果用And拼接
     *
     * @param dataScopeAnno 方法上的注解
     * @return 应用于该SQL的权限处理器
     */
    private List<IDataPermissionSqlHandler> getRequiredHandlers(DataScope dataScopeAnno) {
        if (Objects.nonNull(dataScopeAnno) && ArrayUtils.isNotEmpty(dataScopeAnno.rules())) {
            List<IDataPermissionSqlHandler> requiredHandlers = new ArrayList<>();
            for (Class<? extends IDataPermissionSqlHandler> clazz : dataScopeAnno.rules()) {
                if (registeredHandler.containsKey(clazz)) {
                    requiredHandlers.add(registeredHandler.get(clazz));
                }
            }
            return requiredHandlers;
        }
        return new ArrayList<>(registeredHandler.values());
    }

    /**
     * 返回表的全限定名, 去除引号`
     *
     * @author Lu Cheng
     * @create 2023/8/5
     */
    private String getTableName(Table table) {
        return table.getFullyQualifiedName().replaceAll("`", "");
    }

    @Override
    public Expression buildTableExpression(Table table, Expression where, String whereSegment) {
        // 从本地线程获取权限包装对象
        DataScopeWrapper dataScopeWrapper = DataScopeContextHolder.getDataScopeWrapper();
        // 获取当前线程执行的方法上需要的处理器
        List<IDataPermissionSqlHandler> requiredHandlerList = dataScopeWrapper.getIncludeHandlers();
        // 当前的数据权限对象
        DataScopeEntity dataScopeEntity = dataScopeWrapper.getDataScopeEntity();
        List<Expression> expressionsFromHandler = new ArrayList<>();
        for (IDataPermissionSqlHandler handler : requiredHandlerList) {
            if (handler.supportDataScope(dataScopeEntity)) {
                // 获取权限处理器针对当前表返回的表达式
                Expression expressionForTable = handler.getExpression(table, dataScopeEntity);
                if (expressionForTable != null) {
                    expressionsFromHandler.add(expressionForTable);
                }
            }
        }
        // 拼接多个处理器的表达式，处理器返回结果均为空时，返回原有的表达式
        if (CollectionUtils.isNotEmpty(expressionsFromHandler)) {
            // 处理器的返回的表达式非空，则设置本地线程属性REWRITE=true
            DataScopeContextHolder.rewrite();
            Expression injectExpression = expressionsFromHandler.getFirst();
            for (int i = 1; i < expressionsFromHandler.size(); i++) {
                // 如果声明了多个处理器的表达式之间为互斥关系，则用AND连接， 否则用OR连接
                injectExpression = dataScopeWrapper.isMutex() ?
                        new AndExpression(injectExpression, expressionsFromHandler.get(i)) :
                        new OrExpression(injectExpression, expressionsFromHandler.get(i));
            }

            return  injectExpression;
        } else {
            return null;
        }
    }
}
