package com.lc.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统菜单表(luc_system.menu)表实体类
 *
 * @author lucheng
 * @since 2025-08-15
 */
@Data
@TableName("luc_system.menu")
public class MenuDO implements Serializable {
    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 菜单唯一标识
     */
    @TableField("menu_id")
    private String menuId;

    /**
     * 父级菜单ID
     */
    @TableField("parent_menu_id")
    private String parentMenuId;

    /**
     * 路由名称(必须唯一)
     */
    @TableField("name")
    private String name;

    /**
     * 路由路径
     */
    @TableField("path")
    private String path;

    /**
     * 组件路径(字符串)
     */
    @TableField("component")
    private String component;

    /**
     * 重定向路径
     */
    @TableField("redirect")
    private String redirect;

    /**
     * 菜单类型(catalog/menu/button/embedded/link)
     */
    @TableField("menu_type")
    private String menuType;

    /**
     * 状态(0:禁用 1:启用)
     */
    @TableField("status")
    private Short status;

    /**
     * 排序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 创建者
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField("dt_created")
    private Date dtCreated;

    /**
     * 更新者
     */
    @TableField("modified_by")
    private String modifiedBy;

    /**
     * 更新时间
     */
    @TableField("dt_modified")
    private Date dtModified;

    /**
     * 逻辑删除(0:未删除 1:已删除)
     */
    @TableField("deleted")
    private Short deleted;

}

