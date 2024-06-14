package com.lc.framework.datascope.handler;

import com.lc.framework.datascope.entity.DataScopeEntity;
import com.lc.framework.datascope.entity.SupportTableDefinition;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;

/**
 * <pre>
 * 针对系统用户角色字段的sql处理器
 * </pre>
 *
 * @author Lu Cheng
 * @create 2023-08-01 11:15
 */
public class SysRoleDataScopeSqlHandler implements IDataScopeSqlHandler {
    @Override
    public Expression handleSelect(final Table table, DataScopeEntity dataScopeEntity) {
        return null;
    }

    @Override
    public void bindTable(SupportTableDefinition tableDefinition) {

    }

    @Override
    public boolean supportDataScope(DataScopeEntity dataScopeEntity) {
        return false;
    }

    @Override
    public boolean supportTable(String tableName) {
        return false;
    }
}
