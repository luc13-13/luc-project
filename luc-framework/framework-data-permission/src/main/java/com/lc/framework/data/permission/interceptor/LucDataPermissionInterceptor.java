package com.lc.framework.data.permission.interceptor;

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.lc.framework.core.utils.ReflectionUtils;
import com.lc.framework.data.permission.anno.DataPermission;
import com.lc.framework.data.permission.handler.IDataPermissionSqlHandler;
import com.lc.framework.data.permission.utils.PluginUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.Values;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        if (DataPermissionContextHolder.isNotAnnotated(ms)) {
            log.info("beforeQuery: cached not annotated method, {}", ms.getId());
            return;
        }
        // 从方法参数中获取权限属性，并像缓存中放入DataScope
        DataPermission dataPermissionAnno = getAnnotation(ms);
        // 获取注解声明的权限处理器
        List<IDataPermissionSqlHandler> requiredHandlers = getRequiredHandlers(dataPermissionAnno);
        if (skip(ms, dataPermissionAnno)) {
            return;
        }
        try {
            PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
            DataPermissionContextHolder.setDataPermissionLocal(dataPermissionAnno);
            mpBs.sql(parserSingle(mpBs.sql(), ms.getId()));
        } finally {
            // 根据本地线程中的REWRITE状态，判断当前sql是否被重写，如果没被重写，则放入缓存中，下次无需进行解析
            DataPermissionContextHolder.addIgnoredMapStatement(ms, requiredHandlers);
            // 解析方法执行完毕，清理本地缓存，避免OOM
            DataPermissionContextHolder.clear();
        }
    }

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        // 将代理对象转为真正的实体对象
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        // 获取mybatis对执行sql的解析结果
        MappedStatement ms = mpSh.mappedStatement();
        // 从方法参数中获取权限属性，并向缓存中放入DataScope
        DataPermission dataPermissionAnno = getAnnotation(ms);
        if (skip(ms, dataPermissionAnno)) {
            return;
        }
        // 获取权限处理器
        List<IDataPermissionSqlHandler> requiredHandlers = getRequiredHandlers(dataPermissionAnno);

        SqlCommandType sct = ms.getSqlCommandType();
        if (sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
            if (DataPermissionContextHolder.willIgnored(ms, requiredHandlers)) {
                // 如果sql不在handler的处理范围内， 则直接返回
                return;
            }
            // 将当前线程的DataScope与DataScopeEntity进行封装并缓存，供后续方法调用
            try {
                DataPermissionContextHolder.setDataPermissionLocal(dataPermissionAnno);
                PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
                mpBs.sql(parserMulti(mpBs.sql(), ms.getId()));
            } finally {
                // 根据本地线程中的REWRITE状态，判断当前sql是否被重写，如果没被重写，则放入缓存中，下次无需进行解析
                DataPermissionContextHolder.addIgnoredMapStatement(ms, requiredHandlers);
                // 解析方法执行完毕，清理本地缓存，避免OOM
                DataPermissionContextHolder.clear();
            }
        }

    }

    /**
     * 向insert语句增加字段，只支持 insert into table_name(c1, c2, c3 ) values () 语句
     */
    @Override
    protected void processInsert(Insert insert, int index, String sql, Object obj) {
        if (CollectionUtils.isNotEmpty(insert.getColumns()) && insert.getValues() != null) {
            // 从本地线程获取权限包装对象
            DataPermission dataPermission = DataPermissionContextHolder.getDataPermissionLocal();
            // 获取当前线程执行的方法上需要的处理器
            List<IDataPermissionSqlHandler> requiredHandlerList = getRequiredHandlers(dataPermission);
            Values values = insert.getValues();
            List<Column> columns = insert.getColumns();

            for (IDataPermissionSqlHandler handler : requiredHandlerList) {
                if (handler.supportTable(insert.getTable()) && !handler.ignoreInsert(columns)) {
                    Expression appendExpression = handler.getExpression(insert.getTable());
                    if (values.getExpressions() != null &&  values.getExpressions() instanceof ParenthesedExpressionList) {
                        values.addExpressions(appendExpression);
                    }
                }
            }
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
            updateSets.forEach(updateSet -> updateSet.getValues().forEach(value -> {
                if (value instanceof Select selectValue) {
                    this.processSelectBody(selectValue, (String) obj);
                }
            }));
        }
        final Expression sqlSegment = this.andExpression(update.getTable(), update.getWhere(), (String) obj);
        if (sqlSegment != null) {
            update.setWhere(sqlSegment);
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
     * @author Lu Cheng
     * @desc
     * @create 2023/8/2
     */
    private DataPermission getAnnotation(MappedStatement ms) {
        return DataPermissionContextHolder.getAnnotationCache(ms.getId(), () -> {
            String id = ms.getId();
            // 未命中本地缓存，根据方法名解析
            DataPermission anno;
            try {
                Class<?> clazz = Class.forName(id.substring(0, id.lastIndexOf(".")));
                Method method = ReflectionUtils.getFirstMethod(clazz, id.substring(id.lastIndexOf(".") + 1));
                anno = ReflectionUtils.getAnnotation(method, DataPermission.class);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return anno;
        });
    }

    private static boolean skip(MappedStatement ms, DataPermission dataPermissionAnno) {
        if (Objects.isNull(dataPermissionAnno) || ArrayUtils.isEmpty(dataPermissionAnno.rules())) {
            // 方法上没有权限注解、权限注解中没有声明权限处理器，缓存方法id
            DataPermissionContextHolder.addNotAnnotatedMapStatement(ms);
            return true;
        }
        // 方法上没有权限注解、权限参数为null、权限注解中没有声明权限字段，则直接返回，不对sql做额外处理
        return false;
    }

    /**
     * 获取注解中指定的权限处理器，如果没有指定注解，则使用所有生效的处理器，每个处理器的结果用And拼接
     *
     * @param dataPermissionAnno 方法上的注解
     * @return 应用于该SQL的权限处理器
     */
    private List<IDataPermissionSqlHandler> getRequiredHandlers(DataPermission dataPermissionAnno) {
        if (Objects.nonNull(dataPermissionAnno) && ArrayUtils.isNotEmpty(dataPermissionAnno.rules())) {
            List<IDataPermissionSqlHandler> requiredHandlers = new ArrayList<>();
            for (Class<? extends IDataPermissionSqlHandler> clazz : dataPermissionAnno.rules()) {
                if (registeredHandler.containsKey(clazz)) {
                    requiredHandlers.add(registeredHandler.get(clazz));
                }
            }
            return requiredHandlers;
        }
        return new ArrayList<>(registeredHandler.values());
    }

    @Override
    public Expression buildTableExpression(Table table, Expression where, String whereSegment) {
        // 从本地线程获取权限包装对象
        DataPermission dataPermission = DataPermissionContextHolder.getDataPermissionLocal();
        // 获取当前线程执行的方法上需要的处理器
        List<IDataPermissionSqlHandler> requiredHandlerList = getRequiredHandlers(dataPermission);
        // 获取每个权限处理器针对当前表返回的表达式
        List<Expression> appendedExpressions =
        requiredHandlerList.stream().filter(handler -> handler.supportTable(table))
                .map(handler -> handler.getExpression(table))
                .filter(Objects::nonNull).collect(Collectors.toList());
        // 拼接多个处理器的表达式，根据DataPermission#isMutex判断是AND连接还是OR连接。处理器返回结果均为空时，返回原有的表达式
        if (CollectionUtils.isNotEmpty(appendedExpressions)) {
            // 处理器的返回的表达式非空，则设置本地线程属性REWRITE=true, 记得在sql处理结束后清空
            DataPermissionContextHolder.rewrite();
            Expression injectExpression = appendedExpressions.getFirst();
            if (appendedExpressions.size() > 1) {
                for (int i = 1; i < appendedExpressions.size(); i++) {
                    injectExpression = dataPermission.isMutex() ?
                            new AndExpression(injectExpression, appendedExpressions.get(i)) :
                            new OrExpression(injectExpression, appendedExpressions.get(i));
                }
            }

            return  injectExpression;
        } else {
            return null;
        }
    }
}
