package com.lc.framework.datascope.interceptor;

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.lc.framework.core.utils.ReflectionUtils;
import com.lc.framework.datascope.anno.DataScope;
import com.lc.framework.datascope.entity.DataScopeEntity;
import com.lc.framework.datascope.entity.DataScopeWrapper;
import com.lc.framework.datascope.handler.IDataScopeSqlHandler;
import com.lc.framework.datascope.utils.PluginUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
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
public class DataScopeInterceptor extends JsqlParserSupport implements InnerInterceptor {
    /**
     * 封装需要校验的表全限定名称：数据库名.表名
     */
    private final Set<String> includeTables;

    /**
     * 封装需要校验的表名与处理器， key为表， value为处理器bean， 在配置文件中设置
     */
    private final Map<Class<? extends IDataScopeSqlHandler>, IDataScopeSqlHandler> registeredHandler;

    public DataScopeInterceptor(Set<String> includeTables, Map<Class<? extends IDataScopeSqlHandler>, IDataScopeSqlHandler> registeredHandler) {
        this.includeTables = includeTables;
        this.registeredHandler = registeredHandler;
    }

    public static final String DATA_SCOPE_PARAM_NAME = "dataScopeEntity";

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        // 如果拦截器中没有注入tableName和handler， 直接返回不做处理
        if (CollectionUtils.isEmpty(includeTables) || CollectionUtils.isEmpty(registeredHandler)) {
            log.info("beforeQuery: registered handler is empty");
            return;
        }
        if (MappedStatementCache.isNotAnnotated(ms)) {
            log.info("beforeQuery: cached not annotated method, {}", ms.getId());
            return;
        }
        // 从方法参数中获取权限属性，并像缓存中放入DataScope
        DataScope dataScopeAnno = getAnnotationDataScope(ms);
        // 获取方法中的权限参数
        DataScopeEntity dataScopeEntity = getDataScopeEntity(parameter);
        // 获取注解声明的权限处理器
        List<IDataScopeSqlHandler> requiredHandlers = getRequiredHandlers(dataScopeAnno);
        if (skip(ms, dataScopeAnno, dataScopeEntity)) return;
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
        // 如果拦截器中没有注入tableName和handler， 直接返回不做处理
        if (CollectionUtils.isEmpty(includeTables) || CollectionUtils.isEmpty(registeredHandler)) {
            return;
        }
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
        if (skip(ms, dataScopeAnno, dataScopeEntity)) return;
        // 获取权限处理器
        List<IDataScopeSqlHandler> requiredHandlers = getRequiredHandlers(dataScopeAnno);

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
        processSelectBody(select.getSelectBody());
        List<WithItem> withItemsList = select.getWithItemsList();
        if (CollectionUtils.isNotEmpty(withItemsList)) {
            withItemsList.forEach(this::processSelectBody);
        }
    }

    /**
     * 查询语句的解析结果为SelectBody, {@link SelectBody}的实现类有四种，其中查询语句对应了三种实现类，
     * <pre>
     * {@link PlainSelect}：普通查询，连接查询
     * (1){@code SELECT * FROM sys_user u WHERE u.user_id = }
     *
     * (2){@code SELECT * FROM sys_user u
     *          LEFT JOIN sys_dept d ON u.dept_id = d.id
     *          WHERE u.user_id =}
     * {@link WithItem}：当sql中用到WITH关键字时：
     * (1) {@code WITH t AS ( SELECT * FROM user WHERE user.user_name = 'test' )
     *         SELECT t.* FROM t"}
     * {@link SetOperationList}：当sql中用到INTERSECT、EXCEPT、MINUS、UNION这些关键字时
     *
     * </pre>
     *
     * @author Lu Cheng
     * @create 2023/8/4
     */
    protected void processSelectBody(SelectBody selectBody) {
        if (selectBody == null) {
            return;
        }
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect) selectBody);
        } else if (selectBody instanceof WithItem withItem) {
            processSelectBody(withItem.getSubSelect().getSelectBody());
        } else {
            SetOperationList operationList = (SetOperationList) selectBody;
            List<SelectBody> list = operationList.getSelects();
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(this::processSelectBody);
            }
        }
    }

    /**
     * 处理简单查询, 参考{@link com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor}
     *
     * @author Lu Cheng
     * @create 2023/8/4
     */
    protected void processPlainSelect(PlainSelect plainSelect) {
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        if (CollectionUtils.isNotEmpty(selectItems)) {
            selectItems.forEach(this::processSelectItem);
        }

        // 处理 where 中的子查询
        Expression where = plainSelect.getWhere();
        processWhereSubSelect(where);
        // 处理 fromItem， 也就是from用到的表
        FromItem fromItem = plainSelect.getFromItem();
        List<Table> list = processFromItem(fromItem);
        List<Table> mainTables = new ArrayList<>(list);
        // 处理join用到的表
        // 处理 join
        List<Join> joins = plainSelect.getJoins();
        if (CollectionUtils.isNotEmpty(joins)) {
            mainTables = processJoins(mainTables, joins);
        }

        // 获取查询语句种的所有表， 通过表名与DataColumn的匹配关系判断是否要进行过滤，以及构建过滤条件
        if (CollectionUtils.isNotEmpty(mainTables)) {
            plainSelect.setWhere(buildExpression(where, mainTables));
        }
    }

    private List<Table> processFromItem(FromItem fromItem) {
        // 处理括号括起来的表达式
        while (fromItem instanceof ParenthesisFromItem) {
            fromItem = ((ParenthesisFromItem) fromItem).getFromItem();
        }

        List<Table> mainTables = new ArrayList<>();
        // 无 join 时的处理逻辑
        if (fromItem instanceof Table fromTable) {
            if (includeTables.contains(getTableName(fromTable))) {
                mainTables.add(fromTable);
            }
        } else if (fromItem instanceof SubJoin) {
            // SubJoin 类型则还需要添加上 where 条件
            List<Table> tables = processSubJoin((SubJoin) fromItem);
            mainTables.addAll(tables);
        } else {
            // 处理下 fromItem
            processOtherFromItem(fromItem);
        }
        return mainTables;
    }

    /**
     * 处理 joins
     *
     * @param mainTables 可以为 null
     * @param joins      join 集合
     * @return List<Table> 右连接查询的 Table 列表
     */
    private List<Table> processJoins(List<Table> mainTables, List<Join> joins) {
        if (mainTables == null) {
            mainTables = new ArrayList<>();
        }

        // join 表达式中最终的主表
        Table mainTable = null;
        // 当前 join 的左表
        Table leftTable = null;
        if (mainTables.size() == 1) {
            mainTable = mainTables.get(0);
            leftTable = mainTable;
        }

        //对于 on 表达式写在最后的 join，需要记录下前面多个 on 的表名
        Deque<List<Table>> onTableDeque = new LinkedList<>();
        for (Join join : joins) {
            // 处理 on 表达式
            FromItem joinItem = join.getRightItem();

            // 获取当前 join 的表，subJoint 可以看作是一张表
            List<Table> joinTables = null;
            if (joinItem instanceof Table) {
                joinTables = new ArrayList<>();
                joinTables.add((Table) joinItem);
            } else if (joinItem instanceof SubJoin) {
                joinTables = processSubJoin((SubJoin) joinItem);
            }

            if (joinTables != null) {

                // 如果是隐式内连接
                if (join.isSimple()) {
                    mainTables.addAll(joinTables);
                    continue;
                }

                // 当前表是否忽略, 要用表的权限定名， database.table_name
                Table joinTable = joinTables.get(0);
                boolean joinTableNeedIgnore = !includeTables.contains(getTableName(joinTable));

                List<Table> onTables = null;
                // 如果不要忽略，且是右连接，则记录下当前表
                if (join.isRight()) {
                    mainTable = joinTableNeedIgnore ? null : joinTable;
                    if (leftTable != null) {
                        onTables = Collections.singletonList(leftTable);
                    }
                } else if (join.isLeft()) {
                    if (!joinTableNeedIgnore) {
                        onTables = Collections.singletonList(joinTable);
                    }
                } else if (join.isInner()) {
                    if (mainTable == null) {
                        onTables = Collections.singletonList(joinTable);
                    } else {
                        onTables = Arrays.asList(mainTable, joinTable);
                    }
                    mainTable = null;
                }
                mainTables = new ArrayList<>();
                if (mainTable != null) {
                    mainTables.add(mainTable);
                }

                // 获取 join 尾缀的 on 表达式列表
                Collection<Expression> originOnExpressions = join.getOnExpressions();
                // 正常 join on 表达式只有一个，立刻处理
                if (originOnExpressions.size() == 1 && onTables != null) {
                    List<Expression> onExpressions = new LinkedList<>();
                    onExpressions.add(buildExpression(originOnExpressions.iterator().next(), onTables));
                    join.setOnExpressions(onExpressions);
                    leftTable = joinTable;
                    continue;
                }
                // 表名压栈，忽略的表压入 null，以便后续不处理
                onTableDeque.push(onTables);
                // 尾缀多个 on 表达式的时候统一处理
                if (originOnExpressions.size() > 1) {
                    Collection<Expression> onExpressions = new LinkedList<>();
                    for (Expression originOnExpression : originOnExpressions) {
                        List<Table> currentTableList = onTableDeque.poll();
                        if (CollectionUtils.isEmpty(currentTableList)) {
                            onExpressions.add(originOnExpression);
                        } else {
                            onExpressions.add(buildExpression(originOnExpression, currentTableList));
                        }
                    }
                    join.setOnExpressions(onExpressions);
                }
                leftTable = joinTable;
            } else {
                processOtherFromItem(joinItem);
                leftTable = null;
            }

        }
        return mainTables;
    }

    /**
     * 处理 sub join
     *
     * @param subJoin subJoin
     * @return Table subJoin 中的主表
     */
    private List<Table> processSubJoin(SubJoin subJoin) {
        List<Table> mainTables = new ArrayList<>();
        if (subJoin.getJoinList() != null) {
            List<Table> list = processFromItem(subJoin.getLeft());
            mainTables.addAll(list);
            mainTables = processJoins(mainTables, subJoin.getJoinList());
        }
        return mainTables;
    }

    /**
     * 从{@link DataScopeContextHolder}获取当前线程持有的{@link DataScopeWrapper}, 根据注解中指定的类型处理sql语句
     */
    protected Expression buildExpression(Expression currentExpression, List<Table> tables) {
        // 没有表需要处理直接返回
        if (CollectionUtils.isEmpty(tables)) {
            return currentExpression;
        }
        // 从本地线程获取权限包装对象
        DataScopeWrapper dataScopeWrapper = DataScopeContextHolder.getDataScopeWrapper();
        // 获取当前线程执行的方法上需要的处理器
        List<IDataScopeSqlHandler> requiredHandlerList = dataScopeWrapper.getIncludeHandlers();
        // 当前的数据权限对象
        DataScopeEntity dataScopeEntity = dataScopeWrapper.getDataScopeEntity();
        List<Expression> expressionsFromHandler = new ArrayList<>();
        for (IDataScopeSqlHandler handler : requiredHandlerList) {
            if (handler.supportDataScope(dataScopeEntity)) {
                // 获取权限处理器针对不同表返回的表达式
                List<Expression> expressionsForTables = tables.stream()
                        .filter(table -> handler.supportTable(getTableName(table)))
                        .map(table -> handler.handleSelect(table, dataScopeEntity))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                //同一个权限处理器返回的表达式用AND拼接
                if (CollectionUtils.isNotEmpty(expressionsForTables)) {
                    Expression handlerInjectExpression = expressionsForTables.get(0);
                    for (int i = 1; i < expressionsForTables.size(); i++) {
                        handlerInjectExpression = new AndExpression(handlerInjectExpression, expressionsForTables.get(i));
                    }
                    expressionsFromHandler.add(handlerInjectExpression);
                }
            }
        }
        // 拼接多个处理器的表达式，处理器返回结果均为空时，返回原有的表达式
        if (CollectionUtils.isNotEmpty(expressionsFromHandler)) {
            // 处理器的返回的表达式非空，则设置本地线程属性REWRITE=true
            DataScopeContextHolder.rewrite();
            Expression injectExpression = expressionsFromHandler.get(0);
            for (int i = 1; i < expressionsFromHandler.size(); i++) {
                // 如果声明了多个处理器的表达式之间为互斥关系，则用AND连接， 否则用OR连接
                injectExpression = dataScopeWrapper.isMutex() ?
                        new AndExpression(injectExpression, expressionsFromHandler.get(i)) :
                        new OrExpression(injectExpression, expressionsFromHandler.get(i));
            }

            if (currentExpression == null) {
                return injectExpression;
            }
            if (currentExpression instanceof OrExpression) {
                return new AndExpression(new Parenthesis(currentExpression), injectExpression);
            } else {
                return new AndExpression(currentExpression, injectExpression);
            }
        } else {
            return currentExpression;
        }
    }

//    protected Column getAliasColumn(Table table) {
//        StringBuilder column = new StringBuilder();
//        // 为了兼容隐式内连接，没有别名时条件就需要加上表名
//        if (table.getAlias() != null) {
//            column.append(table.getAlias().getName());
//        } else {
//            column.append(table.getName());
//        }
//        column.append(StringPool.DOT).append(includeTables.get(getTableName(table)).getColumnName());
//        return new Column(column.toString());
//    }

    /**
     * 处理where条件内的子查询
     * <p>
     * 支持如下:
     * 1. in
     * 2. =
     * 3. >
     * 4. <
     * 5. >=
     * 6. <=
     * 7. <>
     * 8. EXISTS
     * 9. NOT EXISTS
     * <p>
     * 前提条件:
     * 1. 子查询必须放在小括号中
     * 2. 子查询一般放在比较操作符的右边
     *
     * @param where where 条件
     */
    protected void processWhereSubSelect(Expression where) {
        if (where == null) {
            return;
        }
        if (where instanceof FromItem) {
            processOtherFromItem((FromItem) where);
            return;
        }
        if (where.toString().indexOf("SELECT") > 0) {
            // 有子查询
            if (where instanceof BinaryExpression expression) {
                // 比较符号 , and , or , 等等
                processWhereSubSelect(expression.getLeftExpression());
                processWhereSubSelect(expression.getRightExpression());
            } else if (where instanceof InExpression expression) {
                // in
                Expression inExpression = expression.getRightExpression();
                if (inExpression instanceof SubSelect) {
                    processSelectBody(((SubSelect) inExpression).getSelectBody());
                }
            } else if (where instanceof ExistsExpression expression) {
                // exists
                processWhereSubSelect(expression.getRightExpression());
            } else if (where instanceof NotExpression expression) {
                // not exists
                processWhereSubSelect(expression.getExpression());
            } else if (where instanceof Parenthesis expression) {
                processWhereSubSelect(expression.getExpression());
            }
        }
    }


    /**
     * 处理子查询等
     */
    protected void processOtherFromItem(FromItem fromItem) {
        // 去除括号
        while (fromItem instanceof ParenthesisFromItem) {
            fromItem = ((ParenthesisFromItem) fromItem).getFromItem();
        }

        if (fromItem instanceof SubSelect subSelect) {
            if (subSelect.getSelectBody() != null) {
                processSelectBody(subSelect.getSelectBody());
            }
        } else if (fromItem instanceof ValuesList) {
            logger.debug("Perform a sub query, if you do not give us feedback");
        } else if (fromItem instanceof LateralSubSelect lateralSubSelect) {
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect.getSubSelect();
                if (subSelect.getSelectBody() != null) {
                    processSelectBody(subSelect.getSelectBody());
                }
            }
        }
    }

    protected void processSelectItem(SelectItem selectItem) {
        if (selectItem instanceof SelectExpressionItem selectExpressionItem) {
            if (selectExpressionItem.getExpression() instanceof SubSelect) {
                processSelectBody(((SubSelect) selectExpressionItem.getExpression()).getSelectBody());
            } else if (selectExpressionItem.getExpression() instanceof Function) {
                processFunction((Function) selectExpressionItem.getExpression());
            }
        }
    }

    /**
     * 处理函数
     * <p>支持: 1. select fun(args..) 2. select fun1(fun2(args..),args..)<p>
     * <p> fixed gitee pulls/141</p>
     */
    protected void processFunction(Function function) {
        ExpressionList parameters = function.getParameters();
        if (parameters != null) {
            parameters.getExpressions().forEach(expression -> {
                if (expression instanceof SubSelect) {
                    processSelectBody(((SubSelect) expression).getSelectBody());
                } else if (expression instanceof Function) {
                    processFunction((Function) expression);
                }
            });
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
    private List<IDataScopeSqlHandler> getRequiredHandlers(DataScope dataScopeAnno) {
        if (Objects.nonNull(dataScopeAnno) && ArrayUtils.isNotEmpty(dataScopeAnno.rules())) {
            List<IDataScopeSqlHandler> requiredHandlers = new ArrayList<>();
            for (Class<? extends IDataScopeSqlHandler> clazz : dataScopeAnno.rules()) {
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
}
