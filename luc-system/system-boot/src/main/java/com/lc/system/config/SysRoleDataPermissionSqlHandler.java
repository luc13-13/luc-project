package com.lc.system.config;

import com.lc.framework.data.permission.handler.AbstractDataPermissionSqlHandler;
import com.lc.framework.web.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Table;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
public class SysRoleDataPermissionSqlHandler extends AbstractDataPermissionSqlHandler {

    @Override
    public boolean supportTable(Table table) {
        // 根据角色判断是否拦截表格
        String roleId = WebUtil.getRoleId();
        boolean roleSupported = switch (roleId) {
            case "productManager" -> "product_catalog".equals(table.getUnquotedName());
            case "customerManager" -> "customer".equals(table.getUnquotedName());
            case "regionManager" -> "region".equals(table.getUnquotedName());
            case "user" -> "sys_user".equals(table.getUnquotedName());
            default ->  false;
        };
        return roleSupported && super.supportTable(table);
    }

    @Override
    public Expression getExpression(final Table table) {
        Set<String> columns =  getColumns(getTableName(table));
        if (CollectionUtils.isEmpty(columns)) {
            return null;
        }
        // 获取当前用户角色
        String roleId = WebUtil.getRoleId();
        List<Expression>  expressionList = new ArrayList<>();
        for (String column : columns) {
            Expression columnExpression = switch (roleId) {
                // 产品经理只能查看负责产品的数据
                case "productManager" -> new InExpression(getAliasColumn(table, column), new ParenthesedExpressionList<>(
                        new StringValue("productA"),
                        new StringValue("productB"))
                );
                // 客户经理只能查看负责客户的数据
                case "customerManager" -> new EqualsTo(getAliasColumn(table, column), new StringValue(WebUtil.getUserId()));
                // 区域经理只能查看负责区域的数据
                case "regionManager" -> new InExpression(getAliasColumn(table, column), new ParenthesedExpressionList<>(
                        new StringValue("regionA"),
                        new StringValue("regionB")));
                case "user" -> new EqualsTo(getAliasColumn(table, column), new StringValue(WebUtil.getUserId()));
                default -> null;
            };
            if (columnExpression != null) expressionList.add(columnExpression);
        }
        if (CollectionUtils.isEmpty(expressionList)) {
            return null;
        }
        Expression injectedExpression = expressionList.getFirst();
        if (expressionList.size() > 1) {
            for (int i = 1; i < expressionList.size(); i++) {
                injectedExpression = new AndExpression(expressionList.get(i), injectedExpression);
            }
        }
        log.info("SysRole inject Expression: {}", injectedExpression.toString());
        return injectedExpression;
    }
}
