package com.lc.framework.data.permission.entity;

import com.lc.framework.data.permission.handler.IDataPermissionSqlHandler;
import com.lc.framework.data.permission.handler.SysRoleDataPermissionSqlHandler;
import com.lc.framework.data.permission.handler.SysUserDataScopeSqlHandler;
import com.lc.framework.data.permission.handler.TenantDataPermissionSqlHandler;
import lombok.Getter;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-04 09:10
 */
@Getter
public enum DataColumnType {
    /**
     * 多租户sql处理器类型
     */
    SYS_USER(SysUserDataScopeSqlHandler.class),
    SYS_ROLE(SysRoleDataPermissionSqlHandler.class),
    TENANT(TenantDataPermissionSqlHandler.class);
    public final Class<? extends IDataPermissionSqlHandler> clazz;

    DataColumnType(Class<? extends IDataPermissionSqlHandler> clazz) {
        this.clazz = clazz;
    }

}
