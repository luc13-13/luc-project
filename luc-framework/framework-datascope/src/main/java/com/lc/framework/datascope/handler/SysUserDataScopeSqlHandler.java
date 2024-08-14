package com.lc.framework.datascope.handler;

import com.lc.framework.datascope.entity.DataScopeEntity;
import com.lc.framework.datascope.entity.SupportTableDefinition;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Table;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * 针对系统用户的数据权限处理器, 为拓展IDataScopeSqlHandler提供参考
 * </pre>
 *
 * @author Lu Cheng
 * @create 2023-08-01 11:16
 */
public class SysUserDataScopeSqlHandler implements IDataScopeSqlHandler {
    // 封装表名与租户字段名映射关系
    private final Map<String, String> tenatTableColumnMap = new ConcurrentHashMap<>();

    @Override
    public Expression handleSelect(final Table table, DataScopeEntity dataScopeEntity) {
        if (Objects.isNull(table)) {
            return null;
        }

        new LongValue(dataScopeEntity.getUserId());
        return null;
    }

    @Override
    public void bindTable(SupportTableDefinition tableDefinition) {
        tenatTableColumnMap.put(tableDefinition.getTableName(), tableDefinition.getColumnName());
    }

    @Override
    public boolean supportDataScope(DataScopeEntity dataScopeEntity) {
        return "tenant".equals(dataScopeEntity.getCurrentRole());
    }

    @Override
    public boolean supportTable(String tableName) {
        return false;
    }
}
