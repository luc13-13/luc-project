package com.lc.framework.datascope.entity;

import com.lc.framework.datascope.handler.*;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-04 09:10
 */
public enum DataColumnType {
    SYS_USER(SysUserDataScopeSqlHandler.class),
    SYS_ROLE(SysRoleDataScopeSqlHandler.class),
    TENANT(TenantDataScopeSqlHandler.class);
    public final Class<? extends IDataScopeSqlHandler> clazz;

    DataColumnType(Class<? extends IDataScopeSqlHandler> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends IDataScopeSqlHandler> getClazz() {
        return clazz;
    }
}
