package com.lc.system.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统角色表(luc_system.sys_role)表业务查询结果封装类
 *
 * @author lucheng
 * @since 2025-09-04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleBO implements Serializable {
/**
     * 主键ID
     */     
    private Integer id;
    
/**
     * 角色ID
     */     
    private String roleId;
    
/**
     * 角色名称
     */     
    private String roleName;
    
/**
     * 角色描述
     */     
    private String description;
    
/**
     * 状态(0:禁用 1:启用)
     */     
    private Boolean status;
    
/**
     * 创建者
     */     
    private String createdBy;
    
/**
     * 创建时间
     */     
    private Date dtCreated;
    
/**
     * 更新者
     */     
    private String modifiedBy;
    
/**
     * 更新时间
     */     
    private Date dtModified;
    
/**
     * 逻辑删除(0:未删除 1:已删除)
     */     
    private Boolean deleted;
    

}

