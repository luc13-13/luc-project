package com.lc.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统菜单元数据表(luc_system.menu_meta)表实体类
 *
 * @author lucheng
 * @since 2025-08-15
 */
@Data
@TableName("luc_system.menu_meta")
public class MenuMetaDO implements Serializable {
    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 菜单ID
     */
    @TableField("menu_id")
    private String menuId;

    /**
     * 菜单标题
     */
    @TableField("title")
    private String title;

    /**
     * 菜单图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 激活时图标
     */
    @TableField("active_icon")
    private String activeIcon;

    /**
     * 激活路径
     */
    @TableField("active_path")
    private String activePath;

    /**
     * 权限标识数组
     */
    @TableField("authority")
    private String authority;

    /**
     * 忽略权限验证(0:否 1:是)
     */
    @TableField("ignore_access")
    private Boolean ignoreAccess;

    /**
     * 菜单可见但访问受限(0:否 1:是)
     */
    @TableField("menu_visible_with_forbidden")
    private Boolean menuVisibleWithForbidden;

    /**
     * 在菜单中隐藏(0:否 1:是)
     */
    @TableField("hide_in_menu")
    private Boolean hideInMenu;

    /**
     * 在标签页中隐藏(0:否 1:是)
     */
    @TableField("hide_in_tab")
    private Boolean hideInTab;

    /**
     * 在面包屑中隐藏(0:否 1:是)
     */
    @TableField("hide_in_breadcrumb")
    private Boolean hideInBreadcrumb;

    /**
     * 隐藏子菜单(0:否 1:是)
     */
    @TableField("hide_children_in_menu")
    private Boolean hideChildrenInMenu;

    /**
     * 固定标签页(0:否 1:是)
     */
    @TableField("affix_tab")
    private Boolean affixTab;

    /**
     * 固定标签页顺序
     */
    @TableField("affix_tab_order")
    private Integer affixTabOrder;

    /**
     * 最大打开标签数
     */
    @TableField("max_num_of_open_tab")
    private Integer maxNumOfOpenTab;

    /**
     * 页面缓存(0:否 1:是)
     */
    @TableField("keep_alive")
    private Boolean keepAlive;

    /**
     * 不使用基础布局(0:否 1:是)
     */
    @TableField("no_basic_layout")
    private Boolean noBasicLayout;

    /**
     * 外链地址
     */
    @TableField("link")
    private String link;

    /**
     * iframe地址
     */
    @TableField("iframe_src")
    private String iframeSrc;

    /**
     * 新窗口打开(0:否 1:是)
     */
    @TableField("open_in_new_window")
    private Boolean openInNewWindow;

    /**
     * 徽标内容
     */
    @TableField("badge")
    private String badge;

    /**
     * 徽标类型(dot/normal)
     */
    @TableField("badge_type")
    private String badgeType;

    /**
     * 徽标颜色(default/destructive/primary/success/warning)
     */
    @TableField("badge_variants")
    private String badgeVariants;

    /**
     * 路由查询参数
     */
    @TableField("query_params")
    private String queryParams;

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

