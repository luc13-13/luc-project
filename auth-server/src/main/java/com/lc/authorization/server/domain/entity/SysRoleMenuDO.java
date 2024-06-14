package com.lc.authorization.server.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色和菜单关联表(SysRoleMenu)表实体类
 *
 * @author lucheng
 * @since 2023-12-20 09:39:13
 */
@Data
public class SysRoleMenuDO implements Serializable {
    /**
     * 角色ID
     */    
    private Long roleId;
    
    /**
     * 菜单ID
     */    
    private Long menuId;
    
}

