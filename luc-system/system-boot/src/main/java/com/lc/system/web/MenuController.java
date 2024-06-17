package com.lc.system.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.system.domain.dto.MenuDTO;
import com.lc.system.domain.dto.MenuMeta;
import com.lc.system.domain.entity.SysMenuDO;
import com.lc.system.mapper.SysMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/27 17:03
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @GetMapping("/getRouters")
    public WebResult<List<MenuDTO>> getRoutes() {
//        List<SysMenuDO> menuDOList = sysMenuMapper.selectMenuTreeByUserId(2L);
//        List<MenuDTO> menuDTOList= buildMenuTree(menuDOList);
        return WebResult.successData(buildMock());
    }

    private List<MenuDTO> buildMenuTree(List<SysMenuDO> menuDOList) {
        // 转换为DTO
        List<MenuDTO> dtoList = convertDO2DTOList(menuDOList);
        // 根据parentId分组，找到每个parentId的children
        Map<Long, List<MenuDTO>> menuMapByPid = dtoList.parallelStream().collect(Collectors.groupingBy(MenuDTO::getParentId));
        dtoList.forEach(menu -> menu.setChildren(menuMapByPid.get(menu.getMenuId())));
        return dtoList.stream().filter(menu -> menu.getParentId().equals(0L)).collect(Collectors.toList());
    }

    private MenuDTO convertDO2DTO(SysMenuDO menuDO) {
        return MenuDTO.builder()
                .menuId(menuDO.getMenuId())
                .parentId(menuDO.getParentId())
                .name(menuDO.getMenuName())
                .path(menuDO.getPath())
                .component(Objects.nonNull(menuDO.getComponent()) ? menuDO.getComponent() : "LAYOUT")
                .query(menuDO.getQuery())
                .icon(menuDO.getIcon())
                .isCache(menuDO.getIsCache() == 0)
                .isFrame(menuDO.getIsFrame() == 0)
                .menuType(menuDO.getMenuType())
                .orderNum(menuDO.getOrderNum())
                .permissions(menuDO.getPerms())
                .visible("0".equals(menuDO.getVisible()))
                .build();
    }

    private List<MenuDTO> convertDO2DTOList(List<SysMenuDO> menuDOList) {
        return menuDOList.stream().map(this::convertDO2DTO).collect(Collectors.toList());
    }

    public static List<MenuDTO> buildMock() {
        MenuDTO dashboard = MenuDTO.builder()
                .menuId(100L)
                .path("/dashboard")
                .component("Layout")
                .name("Dashboard")
                .redirect("/dashboard/analysis")
                .meta(MenuMeta.builder()
                        .title("routes.dashboard.dashboard")
                        .hideChildrenInMenu(true)
                        .icon("bx:bx-home")
                        .build())
                .children(Arrays.asList(
                        MenuDTO.builder()
                                .path("analysis")
                                .name("Analysis")
                                .component("/dashboard/analysis/index")
                                .meta(MenuMeta.builder()
                                        .hideMenu(true)
                                        .hideBreadcrumb(true)
                                        .title("routes.dashboard.analysis")
                                        .currentActiveMenu("/dashboard")
                                        .icon("bx:bx-home")
                                        .build())
                                .build(),
                                MenuDTO.builder()
                                        .path("workbench")
                                        .name("Workbench")
                                        .component("/dashboard/workbench/index")
                                        .meta(MenuMeta.builder()
                                                .hideMenu(true)
                                                .hideBreadcrumb(true)
                                                .title("routes.dashboard.workbench")
                                                .currentActiveMenu("/dashboard")
                                                .icon("bx:bx-home")
                                                .build())
                                        .build()
                        ))
                .build();


        MenuDTO backRoute = MenuDTO.builder()
                .menuId(200L)
                .path("back")
                .name("PermissionBackDemo")
                .meta(MenuMeta.builder()
                        .title("routes.demo.permission.back")
                        .build())
                .children(Arrays.asList(
                        MenuDTO.builder()
                                .path("page")
                                .name("BackAuthPage")
                                .component("/demo/permission/back/index")
                                .meta(MenuMeta.builder()
                                        .title("routes.demo.permission.backPage")
                                        .build())
                                .build(),
                        MenuDTO.builder()
                                .path("btn")
                                .name("BackAuthBtn")
                                .component("/demo/permission/back/Btn")
                                .meta(MenuMeta.builder()
                                        .title("routes.demo.permission.backBtn")
                                        .build())
                                .build()
                ))
                .build();

        MenuDTO authRoute = MenuDTO.builder()
                .menuId(300L)
                .path("/permission")
                .redirect("/permission/front/page")
                .component("LAYOUT")
                .name("Permission")
                .meta(MenuMeta.builder()
                        .icon("carbon:user-role")
                        .title("routes.demo.permission.permission")
                        .build())
                .children(List.of(backRoute))
                .build();

        MenuDTO levelRoute = MenuDTO.builder()
                .menuId(400L)
                .path("/level")
                .redirect("/level/menu1/menu1-1")
                .component("LAYOUT")
                .name("Level")
                .meta(MenuMeta.builder()
                        .icon("carbon:user-role")
                        .title("routes.demo.level.level")
                        .build())
                .children(Arrays.asList(
                        MenuDTO.builder()
                                .path("menu1")
                                .name("Menu1Demo")
                                .meta(MenuMeta.builder()
                                        .title("Menu1")
                                        .build())
                                .children(Arrays.asList(
                                        MenuDTO.builder()
                                                .path("menu1-1-1")
                                                .name("Menu111Demo")
                                                .component("/demo/level/Menu111")
                                                .meta(MenuMeta.builder()
                                                        .title("Menu111")
                                                        .build())
                                                .build()
                                ))
                                .build(),
                        MenuDTO.builder()
                                .path("menu1-2")
                                .name("Menu12Demo")
                                .component("/demo/level/Menu12")
                                .meta(MenuMeta.builder()
                                        .title("Menu1-2")
                                        .build())
                                .build()
                ))
                .build();

        MenuDTO sysRoute = MenuDTO.builder()
                .menuId(400L)
                .path("/system")
                .redirect("/system/account")
                .component("Layout")
                .name("System")
                .meta(MenuMeta.builder()
                        .icon("ion:settings-outline")
                        .title("routes.demo.system.moduleName")
                        .build())
                .children(List.of(
                                MenuDTO.builder()
                                        .path("account")
                                        .component("/demo/system/account/index")
                                        .name("AccountManagement")
                                        .meta(MenuMeta.builder().title("routes.demo.system.account").build())
                                        .build(),
                        MenuDTO.builder()
                                .path("account_detail/:id")
                                .name("AccountDetail")
                                .component("/demo/system/account/AccountDetail")
                                .meta(MenuMeta.builder()
                                        .hideMenu(true)
                                        .ignoreKeepAlive(true)
                                        .currentActiveMenu("/system/account")
                                        .title("routes.demo.system.account").build())
                                .build(),
                        MenuDTO.builder()
                                .path("role")
                                .name("RoleManagement")
                                .component("/demo/system/role/index")
                                .meta(MenuMeta.builder()
                                        .ignoreKeepAlive(true)
                                        .title("routes.demo.system.role").build())
                                .build(),
                        MenuDTO.builder()
                                .path("menu")
                                .component("/demo/system/menu/index")
                                .name("MenuManagement")
                                .meta(MenuMeta.builder()
                                        .ignoreKeepAlive(true)
                                        .title("routes.demo.system.menu").build())
                                .build(),
                        MenuDTO.builder()
                                .path("dept")
                                .component("/demo/system/dept/index")
                                .name("DeptManagement")
                                .meta(MenuMeta.builder()
                                        .ignoreKeepAlive(true)
                                        .title("routes.demo.system.dept").build())
                                .build(),
                        MenuDTO.builder()
                                .path("changePassword")
                                .component("/demo/system/password/index")
                                .name("ChangePassword")
                                .meta(MenuMeta.builder()
                                        .ignoreKeepAlive(true)
                                        .title("routes.demo.system.password").build())
                                .build()
                        )
                ).build();
        return List.of(dashboard, authRoute, levelRoute, sysRoute);
    }
}
