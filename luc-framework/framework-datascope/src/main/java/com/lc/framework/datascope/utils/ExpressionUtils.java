package com.lc.framework.datascope.utils;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;

import java.util.Objects;

import static com.lc.framework.common.constants.StringConstants.DOT;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-11-21 16:49
 */
public class ExpressionUtils {
    public static Expression buildInExpression() {
        return new InExpression();
    }

    public static Column buildColumn(String tableName, Alias tableAlias, String columnName) {
        return new Column((Objects.isNull(tableAlias) ? tableName : tableAlias.getName()) + DOT + columnName);
    }
}
