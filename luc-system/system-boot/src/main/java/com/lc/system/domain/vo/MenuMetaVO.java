package com.lc.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * <pre>
 *     渲染用户菜单、菜单列表、菜单详情
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/18 10:56
 * @version : 1.0
 */
@Data
@Builder
@Schema(description = "前端路由元数据")
public class MenuMetaVO {

    /**
     * 菜单标题
     */
    @Schema(name = "title", description = "菜单标题")
    private String title;

    /**
     * 菜单图标
     */
    @Schema(name = "icon", description = "菜单图标")
    private String icon;

    /**
     * 激活时图标
     */
    @Schema(name = "activeIcon", description = "激活时图标")
    private String activeIcon;

    /**
     * 激活路径
     */
    @Schema(name = "activePath", description = "激活路径")
    private String activePath;

    /**
     * 权限标识数组
     */
    @Schema(name = "authority", description = "权限标识数组")
    private String authority;

    /**
     * 忽略权限验证(0:否 1:是)
     */
    @Schema(name = "ignoreAccess", description = "忽略权限验证(0:否 1:是)")
    private Boolean ignoreAccess;

    /**
     * 菜单可见但访问受限(0:否 1:是)
     */
    @Schema(name = "menuVisibleWithForbidden", description = "菜单可见但访问受限(0:否 1:是)")
    private Boolean menuVisibleWithForbidden;

    /**
     * 在菜单中隐藏(0:否 1:是)
     */
    @Schema(name = "hideInMenu", description = "在菜单中隐藏(0:否 1:是)")
    private Boolean hideInMenu;

    /**
     * 在标签页中隐藏(0:否 1:是)
     */
    @Schema(name = "hideInTab", description = "在标签页中隐藏(0:否 1:是)")
    private Boolean hideInTab;

    /**
     * 在面包屑中隐藏(0:否 1:是)
     */
    @Schema(name = "hideInBreadcrumb", description = "在面包屑中隐藏(0:否 1:是)")
    private Boolean hideInBreadcrumb;

    /**
     * 隐藏子菜单(0:否 1:是)
     */
    @Schema(name = "hideChildrenInMenu", description = "隐藏子菜单(0:否 1:是)")
    private Boolean hideChildrenInMenu;

    /**
     * 固定标签页(0:否 1:是)
     */
    @Schema(name = "affixTab", description = "固定标签页(0:否 1:是)")
    private Boolean affixTab;

    /**
     * 固定标签页顺序
     */
    @Schema(name = "affixTabOrder", description = "固定标签页顺序")
    private Integer affixTabOrder;

    /**
     * 最大打开标签数
     */
    @Schema(name = "maxNumOfOpenTab", description = "最大打开标签数")
    private Integer maxNumOfOpenTab;

    /**
     * 页面缓存(0:否 1:是)
     */
    @Schema(name = "keepAlive", description = "页面缓存(0:否 1:是)")
    private Short keepAlive;

    /**
     * 不使用基础布局(0:否 1:是)
     */
    @Schema(name = "noBasicLayout", description = "不使用基础布局(0:否 1:是)")
    private Short noBasicLayout;

    /**
     * 外链地址
     */
    @Schema(name = "link", description = "外链地址")
    private String link;

    /**
     * iframe地址
     */
    @Schema(name = "iframeSrc", description = "iframe地址")
    private String iframeSrc;

    /**
     * 新窗口打开(0:否 1:是)
     */
    @Schema(name = "openInNewWindow", description = "新窗口打开(0:否 1:是)")
    private Short openInNewWindow;

    /**
     * 徽标内容
     */
    @Schema(name = "badge", description = "徽标内容")
    private String badge;

    /**
     * 徽标类型(dot/normal)
     */
    @Schema(name = "badgeType", description = "徽标类型(dot/normal)")
    private String badgeType;

    /**
     * 徽标颜色(default/destructive/primary/success/warning)
     */
    @Schema(name = "badgeVariants", description = "徽标颜色(default/destructive/primary/success/warning)")
    private String badgeVariants;

    /**
     * 路由查询参数
     */
    @Schema(name = "queryParams", description = "路由查询参数")
    private String queryParams;
}
