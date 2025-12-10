package com.lc.system.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统角色表(luc_system.sys_role)表实体类
 *
 * @author lucheng
 * @since 2025-09-04
 */
@Data
@TableName("luc_system.sys_role")
public class SysRoleDO implements Serializable {
    /**
     * 主键ID
     */
    @TableId("id")
    private Integer id;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private String roleId;

    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 角色描述
     */
    @TableField("description")
    private String description;

    /**
     * 状态(0:禁用 1:启用)
     */
    @TableField("status")
    private Boolean status;

    /**
     * 创建者
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "dt_created", fill = FieldFill.INSERT)
    private Date dtCreated;

    /**
     * 更新者
     */
    @TableField(value = "modified_by", fill = FieldFill.UPDATE)
    private String modifiedBy;

    /**
     * 更新时间
     */
    @TableField(value = "dt_modified", fill = FieldFill.UPDATE)
    private Date dtModified;

    /**
     * 逻辑删除(0:未删除 1:已删除)
     */
    @TableField("deleted")
    private Boolean deleted;

}

