package com.lc.auth.server.web;

import com.lc.framework.core.mvc.WebResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 菜单控制器
 * 提供与前端 mock 项目相同格式的菜单数据
 *
 * @author Lu Cheng
 * @date 2025/8/14
 */
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@Slf4j
public class MenuController {

    /**
     * 获取用户所有菜单
     * 返回与前端 mock 项目 /menu/all 相同格式的数据
     */
    @GetMapping("/all")
    public WebResult<List<Map<String, Object>>> getAllMenus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "anonymous";
        
        log.info("获取用户 {} 的菜单数据", username);
        
        List<Map<String, Object>> menus = getMenusByUsername(username);
        return WebResult.success(menus);
    }

    /**
     * 根据用户名获取菜单数据
     */
    private List<Map<String, Object>> getMenusByUsername(String username) {
        // 基础仪表板菜单（所有用户都有）
        List<Map<String, Object>> dashboardMenus = createDashboardMenus();
        
        // 根据用户角色返回不同的菜单
        if ("admin".equals(username)) {
            List<Map<String, Object>> result = new ArrayList<>(dashboardMenus);
            result.addAll(createSuperMenus());
            return result;
        } else if ("vben".equals(username)) {
            List<Map<String, Object>> result = new ArrayList<>(dashboardMenus);
            result.addAll(createSuperMenus());
            return result;
        } else {
            List<Map<String, Object>> result = new ArrayList<>(dashboardMenus);
//            result.addAll(createDemosMenus("user"));
            return result;
        }
    }

    /**
     * 创建仪表板菜单
     */
    private List<Map<String, Object>> createDashboardMenus() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("name", "Dashboard");
        dashboard.put("path", "/dashboard");
        dashboard.put("redirect", "/analytics");
        
        Map<String, Object> meta = new HashMap<>();
        meta.put("order", -1);
        meta.put("title", "page.dashboard.title");
        dashboard.put("meta", meta);
        
        List<Map<String, Object>> children = new ArrayList<>();
        
        // Analytics 子菜单
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("name", "Analytics");
        analytics.put("path", "/analytics");
        analytics.put("component", "/dashboard/analytics/index");
        Map<String, Object> analyticsMeta = new HashMap<>();
        analyticsMeta.put("affixTab", true);
        analyticsMeta.put("title", "page.dashboard.analytics");
        analytics.put("meta", analyticsMeta);
        children.add(analytics);
        
        // Workspace 子菜单
        Map<String, Object> workspace = new HashMap<>();
        workspace.put("name", "Workspace");
        workspace.put("path", "/workspace");
        workspace.put("component", "/dashboard/workspace/index");
        Map<String, Object> workspaceMeta = new HashMap<>();
        workspaceMeta.put("title", "page.dashboard.workspace");
        workspace.put("meta", workspaceMeta);
        children.add(workspace);
        
        dashboard.put("children", children);
        
        return Arrays.asList(dashboard);
    }

    /**
     * 创建超级管理员菜单
     */
    private List<Map<String, Object>> createSuperMenus() {
        Map<String, Object> system = new HashMap<>();
        system.put("name", "System");
        system.put("path", "/system");
        
        Map<String, Object> meta = new HashMap<>();
        meta.put("icon", "carbon:settings");
        meta.put("keepAlive", true);
        meta.put("order", 9997);
        meta.put("title", "page.system.title");
        system.put("meta", meta);
        
        List<Map<String, Object>> children = new ArrayList<>();
        
        // 角色管理
        Map<String, Object> role = new HashMap<>();
        role.put("name", "RoleManage");
        role.put("path", "/system/role");
        role.put("component", "/system/role/index");
        Map<String, Object> roleMeta = new HashMap<>();
        roleMeta.put("icon", "icon-park-solid:permissions");
        roleMeta.put("title", "page.system.role");
        roleMeta.put("order", 1);
        role.put("meta", roleMeta);
        children.add(role);
        
        // 菜单管理
        Map<String, Object> menu = new HashMap<>();
        menu.put("name", "MenuManage");
        menu.put("path", "/system/menu");
        menu.put("component", "/system/menu/index");
        Map<String, Object> menuMeta = new HashMap<>();
        menuMeta.put("icon", "carbon:menu");
        menuMeta.put("title", "page.system.menu");
        menuMeta.put("order", 2);
        menu.put("meta", menuMeta);
        children.add(menu);
        
        // 租户管理
        Map<String, Object> tenant = new HashMap<>();
        tenant.put("name", "TenantManage");
        tenant.put("path", "/system/tenant");
        tenant.put("component", "/system/tenant/index");
        Map<String, Object> tenantMeta = new HashMap<>();
        tenantMeta.put("icon", "ep:user-filled");
        tenantMeta.put("title", "page.system.tenant");
        tenantMeta.put("order", 3);
        tenant.put("meta", tenantMeta);
        children.add(tenant);
        
        system.put("children", children);
        
        return Arrays.asList(system);
    }

    /**
     * 创建演示菜单
     */
    private List<Map<String, Object>> createDemosMenus(String role) {
        Map<String, Object> demos = new HashMap<>();
        demos.put("name", "Demos");
        demos.put("path", "/demos");
        demos.put("redirect", "/demos/access");
        
        Map<String, Object> meta = new HashMap<>();
        meta.put("icon", "ic:baseline-view-in-ar");
        meta.put("keepAlive", true);
        meta.put("order", 1000);
        meta.put("title", "demos.title");
        demos.put("meta", meta);
        
        List<Map<String, Object>> children = new ArrayList<>();
        
        // Access Demos
        Map<String, Object> accessDemos = new HashMap<>();
        accessDemos.put("name", "AccessDemos");
        accessDemos.put("path", "/demos/access");
        accessDemos.put("redirect", "/demos/access/page-control");
        Map<String, Object> accessMeta = new HashMap<>();
        accessMeta.put("icon", "mdi:cloud-key-outline");
        accessMeta.put("title", "demos.access.backendPermissions");
        accessDemos.put("meta", accessMeta);
        
        List<Map<String, Object>> accessChildren = new ArrayList<>();
        
        // Page Control
        Map<String, Object> pageControl = new HashMap<>();
        pageControl.put("name", "AccessPageControlDemo");
        pageControl.put("path", "/demos/access/page-control");
        pageControl.put("component", "/demos/access/index");
        Map<String, Object> pageControlMeta = new HashMap<>();
        pageControlMeta.put("icon", "mdi:page-previous-outline");
        pageControlMeta.put("title", "demos.access.pageAccess");
        pageControl.put("meta", pageControlMeta);
        accessChildren.add(pageControl);
        
        // Button Control
        Map<String, Object> buttonControl = new HashMap<>();
        buttonControl.put("name", "AccessButtonControlDemo");
        buttonControl.put("path", "/demos/access/button-control");
        buttonControl.put("component", "/demos/access/button-control");
        Map<String, Object> buttonControlMeta = new HashMap<>();
        buttonControlMeta.put("icon", "mdi:button-cursor");
        buttonControlMeta.put("title", "demos.access.buttonControl");
        buttonControl.put("meta", buttonControlMeta);
        accessChildren.add(buttonControl);
        
        // Role specific menu
        Map<String, Object> roleSpecific = createRoleSpecificMenu(role);
        if (roleSpecific != null) {
            accessChildren.add(roleSpecific);
        }
        
        accessDemos.put("children", accessChildren);
        children.add(accessDemos);
        
        demos.put("children", children);
        
        return Arrays.asList(demos);
    }

    /**
     * 创建角色特定菜单
     */
    private Map<String, Object> createRoleSpecificMenu(String role) {
        Map<String, Object> menu = new HashMap<>();
        Map<String, Object> meta = new HashMap<>();
        meta.put("icon", "mdi:button-cursor");
        
        switch (role) {
            case "admin":
                menu.put("name", "AccessAdminVisibleDemo");
                menu.put("path", "/demos/access/admin-visible");
                menu.put("component", "/demos/access/admin-visible");
                meta.put("title", "demos.access.adminVisible");
                break;
            case "super":
                menu.put("name", "AccessSuperVisibleDemo");
                menu.put("path", "/demos/access/super-visible");
                menu.put("component", "/demos/access/super-visible");
                meta.put("title", "demos.access.superVisible");
                break;
            case "user":
                menu.put("name", "AccessUserVisibleDemo");
                menu.put("path", "/demos/access/user-visible");
                menu.put("component", "/demos/access/user-visible");
                meta.put("title", "demos.access.userVisible");
                break;
            default:
                return null;
        }
        
        menu.put("meta", meta);
        return menu;
    }
}
