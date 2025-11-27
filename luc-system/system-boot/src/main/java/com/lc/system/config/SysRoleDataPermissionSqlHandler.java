package com.lc.system.config;

import com.lc.framework.core.system.RoleEnum;
import com.lc.framework.data.permission.entity.SupportTableDefinition;
import com.lc.framework.data.permission.handler.AbstractDataPermissionSqlHandler;
import com.lc.framework.web.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <pre>
 * 针对系统用户角色字段的sql处理器
 * </pre>
 *
 * @author Lu Cheng
 * @create 2023-08-01 11:15
 */
@Slf4j
@Component
public class SysRoleDataPermissionSqlHandler extends AbstractDataPermissionSqlHandler implements InitializingBean {

    /**
     * {@link SupportTableDefinition#getProperties()}中，角色信息
     */
    public static final String ROLE_PROPERTIES_KEY = "role";

    /**
     * 每个角色的表达式构建方法
     */
    private final Map<String, Function<Column, Expression>> roleTableConverterMap = new ConcurrentHashMap<>();

    /**
     * 角色和表的绑定关系, key为tableName, value为roleId
     */
    private final Map<String, String> roleTableMap = new ConcurrentHashMap<>();

    @Override
    public void bindTable(SupportTableDefinition tableDefinition) {
        super.bindTable(tableDefinition);
        // 绑定每张表的角色
        if (!CollectionUtils.isEmpty(tableDefinition.getProperties())
                && tableDefinition.getProperties().containsKey(ROLE_PROPERTIES_KEY)) {
            String role = tableDefinition.getProperties().get(ROLE_PROPERTIES_KEY);
            RoleEnum roleEnum = RoleEnum.valueOf(role);
            roleTableMap.put(getTableName(tableDefinition), roleEnum.getRoleId());
        }
    }

    @Override
    public boolean supportTable(Table table) {
        // 根据角色判断是否拦截表格
        String roleId = WebUtil.getRoleId();
        return roleId.equals(roleTableMap.get(getTableName(table)));
    }

    @Override
    public Expression getExpression(final Table table) {
        Set<String> columns = getColumns(getTableName(table));
        if (CollectionUtils.isEmpty(columns)) {
            return null;
        }
        // 获取当前用户角色
        String roleId = WebUtil.getRoleId();
        Function<Column, Expression> expressionFunction = this.roleTableConverterMap.get(roleId);
        if (expressionFunction == null) {
            return null;
        }
        List<Expression> expressionList = columns.stream()
                .map(column -> expressionFunction.apply(getAliasColumn(table, column)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(expressionList)) {
            return null;
        }
        Expression injectedExpression = expressionList.getFirst();

        for (int i = 1; i < expressionList.size(); i++) {
            injectedExpression = new AndExpression(expressionList.get(i), injectedExpression);
        }

        log.info("SysRole inject Expression: {}", injectedExpression.toString());
        return injectedExpression;
    }

    @Override
    public void afterPropertiesSet() {
        this.roleTableConverterMap.put("productManager", this::productManagerExpression);
        this.roleTableConverterMap.put("customerManager", this::customerManagerExpression);
        this.roleTableConverterMap.put("regionManager", this::regionManagerExpression);
        this.roleTableConverterMap.put("user", this::userExpression);
    }

    private Expression productManagerExpression(Column column) {
        return new InExpression(column, new ParenthesedExpressionList<>(
                new StringValue("productA"),
                new StringValue("productB")));
    }

    private Expression customerManagerExpression(Column column) {
        return new EqualsTo(column, new StringValue(WebUtil.getUserId()));
    }

    private Expression regionManagerExpression(Column column) {
        return new InExpression(column, new ParenthesedExpressionList<>(
                new StringValue("regionA"),
                new StringValue("regionB")));
    }

    private Expression userExpression(Column column) {
        return new EqualsTo(column, new StringValue(WebUtil.getUserId()));
    }
}
