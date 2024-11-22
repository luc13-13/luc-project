package com.lc.framework.datascope.handler;

import com.lc.framework.datascope.entity.DataScopeEntity;
import com.lc.framework.datascope.entity.SupportTableDefinition;
import com.lc.framework.datascope.utils.ExpressionUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Table;
import org.springframework.util.StringUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * 针对租户的sql处理器，要求表必须有租户字段，根据租户的tenant_id字段进行处理
 * 支持用户自定义不同表中的租户字段名
 * </pre>
 *
 * @author Lu Cheng
 * @create 2023-08-01 11:05
 */
public class TenantDataScopeSqlHandler implements IDataScopeSqlHandler {

    /**
     * 封装表名与租户字段名映射关系
     */
    private final Map<String, String> tenatTableColumnMap = new ConcurrentHashMap<>();

    @Override
    public Expression handleSelect(final Table table, DataScopeEntity dataScopeEntity) {
        String column = tenatTableColumnMap.get(table.getName());
        return new EqualsTo(ExpressionUtils.buildColumn(table.getFullyQualifiedName(), table.getAlias(), column), new LongValue(dataScopeEntity.getUserId()));
    }

    @Override
    public void bindTable(SupportTableDefinition tableDefinition) {
        tenatTableColumnMap.put(tableDefinition.getTableName(), tableDefinition.getColumnName());
    }

    @Override
    public boolean supportDataScope(DataScopeEntity dataScopeEntity) {
        return !StringUtils.hasLength(dataScopeEntity.getCurrentRole()) || "tenant".equals(dataScopeEntity.getCurrentRole());
    }

    @Override
    public boolean supportTable(String tableName) {
        return tenatTableColumnMap.containsKey(tableName);
    }
}
