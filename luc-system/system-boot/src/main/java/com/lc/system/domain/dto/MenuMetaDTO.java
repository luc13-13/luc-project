package com.lc.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统菜单元数据表(luc_system.menu_meta)表数据传输类
 *
 * @author lucheng
 * @since 2025-08-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "MenuMetaDTO")
public class MenuMetaDTO implements Serializable {

    /**
     * 菜单标题
     */
    @Schema(name = "title", title = "菜单标题")
    private String title;

    /**
     * 菜单图标
     */
    @Schema(name = "icon", title = "菜单图标")
    private String icon;

    /**
     * 激活时图标
     */
    @Schema(name = "activeIcon", title = "激活时图标")
    private String activeIcon;

    /**
     * 激活路径
     */
    @Schema(name = "activePath", title = "激活路径")
    private String activePath;

    /**
     * 权限标识数组
     */
    @Schema(name = "authority", title = "权限标识数组")
    private String authority;

    /**
     * 忽略权限验证(0:否 1:是)
     */
    @Schema(name = "ignoreAccess", title = "忽略权限验证(0:否 1:是)")
    private Short ignoreAccess;

    /**
     * 菜单可见但访问受限(0:否 1:是)
     */
    @Schema(name = "menuVisibleWithForbidden", title = "菜单可见但访问受限(0:否 1:是)")
    private Short menuVisibleWithForbidden;

    /**
     * 在菜单中隐藏(0:否 1:是)
     */
    @Schema(name = "hideInMenu", title = "在菜单中隐藏(0:否 1:是)")
    private Short hideInMenu;

    /**
     * 在标签页中隐藏(0:否 1:是)
     */
    @Schema(name = "hideInTab", title = "在标签页中隐藏(0:否 1:是)")
    private Short hideInTab;

    /**
     * 在面包屑中隐藏(0:否 1:是)
     */
    @Schema(name = "hideInBreadcrumb", title = "在面包屑中隐藏(0:否 1:是)")
    private Short hideInBreadcrumb;

    /**
     * 隐藏子菜单(0:否 1:是)
     */
    @Schema(name = "hideChildrenInMenu", title = "隐藏子菜单(0:否 1:是)")
    private Short hideChildrenInMenu;

    /**
     * 固定标签页(0:否 1:是)
     */
    @Schema(name = "affixTab", title = "固定标签页(0:否 1:是)")
    private Short affixTab;

    /**
     * 固定标签页顺序
     */
    @Schema(name = "affixTabOrder", title = "固定标签页顺序")
    private Integer affixTabOrder;

    /**
     * 最大打开标签数
     */
    @Schema(name = "maxNumOfOpenTab", title = "最大打开标签数")
    private Integer maxNumOfOpenTab;

    /**
     * 页面缓存(0:否 1:是)
     */
    @Schema(name = "keepAlive", title = "页面缓存(0:否 1:是)")
    private Short keepAlive;

    /**
     * 不使用基础布局(0:否 1:是)
     */
    @Schema(name = "noBasicLayout", title = "不使用基础布局(0:否 1:是)")
    private Short noBasicLayout;

    /**
     * 外链地址
     */
    @Schema(name = "link", title = "外链地址")
    private String link;

    /**
     * iframe地址
     */
    @Schema(name = "iframeSrc", title = "iframe地址")
    private String iframeSrc;

    /**
     * 新窗口打开(0:否 1:是)
     */
    @Schema(name = "openInNewWindow", title = "新窗口打开(0:否 1:是)")
    private Short openInNewWindow;

    /**
     * 徽标内容
     */
    @Schema(name = "badge", title = "徽标内容")
    private String badge;

    /**
     * 徽标类型(dot/normal)
     */
    @Schema(name = "badgeType", title = "徽标类型(dot/normal)")
    private String badgeType;

    /**
     * 徽标颜色(default/destructive/primary/success/warning)
     */
    @Schema(name = "badgeVariants", title = "徽标颜色(default/destructive/primary/success/warning)")
    private String badgeVariants;

    /**
     * 路由查询参数
     */
    @Schema(name = "queryParams", title = "路由查询参数")
    private String queryParams;
}

