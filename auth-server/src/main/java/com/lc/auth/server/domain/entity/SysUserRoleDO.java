package com.lc.auth.server.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户和角色关联表(SysUserRole)表实体类
 *
 * @author lucheng
 * @since 2023-12-20 09:39:32
 */
@Data
public class SysUserRoleDO implements Serializable {
    /**
     * 用户ID
     */    
    private Long userId;
    
    /**
     * 角色ID
     */    
    private Long roleId;
    
}

