package com.lc.system.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色菜单关联表(luc_system.sys_role_menu)表实体类
 *
 * @author lucheng
 * @since 2025-09-04
 */
@Data
@TableName("luc_system.sys_role_menu")
public class SysRoleMenuDO implements Serializable {
    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private String roleId;

    /**
     * 菜单ID
     */
    @TableField("menu_id")
    private String menuId;

    /**
     * 创建者
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "dt_created", fill = FieldFill.INSERT)
    private Date dtCreated;
}
