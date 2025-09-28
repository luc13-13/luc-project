package com.lc.framework.core.system;

import lombok.Getter;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/9/17 10:22
 * @version : 1.0
 */
public enum RoleEnum {
    Admin("admin", "系统管理员"),
    ProductManager("productManager", "产品经理"),
    RegionManager("regionManager", "区域经理"),
    CustomerManager("customerManager", "客户经理"),
    TenantManager("tenantManager", "租户管理员"),
    Tenant("tenant", "租户");

    @Getter
    private final String roleId;
    @Getter
    private final String roleName;

    RoleEnum(String roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }
}
