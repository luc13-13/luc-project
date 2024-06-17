package com.lc.system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 *     菜单元信息， 包含权限、名称、排序等
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/17 14:50
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuMeta {

    private Integer orderNo;
    // title
    private String title;
    // dynamic router level.
    private Integer dynamicLevel;
    // dynamic router real route path (For performance).
    private String realPath;
    // Whether to ignore permissions
    private Boolean ignoreAuth;
    //required permission
    private String permission;
    // role info
    private String roles;
    // Whether not to cache
    private Boolean ignoreKeepAlive;
    // Is it fixed on tab
    private Boolean affix;
    // icon on tab
    private String icon;
    // img on tab
    private String img;
    private String frameSrc;
    // current page transition
    private String transitionName;
    // Whether the route has been dynamically added
    private Boolean hideBreadcrumb;
    // Hide submenu
    private Boolean hideChildrenInMenu;
    // Carrying parameters
    private Boolean carryParam;
    // Used internally to mark single-level menus
    private Boolean single;
    // Currently active menu
    private String currentActiveMenu;
    // Never show in tab
    private Boolean hideTab;
    // Never show in menu
    private Boolean hideMenu;
    private Boolean isLink;
    // only build for Menu
    private Boolean ignoreRoute;
    // Hide path for children
    private Boolean hidePathForChildren;
}
