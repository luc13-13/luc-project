package com.lc.system.converter.impl;

import com.lc.system.converter.MenuConverter;
import com.lc.system.domain.bo.MenuBO;
import com.lc.system.domain.dto.MenuDTO;
import com.lc.system.domain.entity.MenuDO;
import com.lc.system.domain.vo.MenuMetaVO;
import com.lc.system.domain.vo.MenuVO;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/18 09:11
 * @version : 1.0
 */
@Service
public class MenuConverterImpl implements MenuConverter {

    @Override
    public MenuVO convertBO2RouteVO(@NotNull MenuBO menuBO) {
        MenuVO vo = MenuVO.builder().build();
        if (menuBO.getMenuDO() != null) {
            vo.setId(menuBO.getMenuDO().getId());
            vo.setMenuId(menuBO.getMenuDO().getMenuId());
            vo.setParentMenuId(menuBO.getMenuDO().getParentMenuId());
            vo.setName(menuBO.getMenuDO().getName());
            vo.setPath(menuBO.getMenuDO().getPath());
            vo.setComponent(menuBO.getMenuDO().getComponent());
            vo.setRedirect(menuBO.getMenuDO().getRedirect());
            vo.setMenuType(menuBO.getMenuDO().getMenuType());
            vo.setSortOrder(menuBO.getMenuDO().getSortOrder());
            vo.setStatus(menuBO.getMenuDO().getStatus());
        }
        if (menuBO.getMenuMetaDO() != null) {
            MenuMetaVO metaVO = MenuMetaVO.builder().build();
            metaVO.setTitle(menuBO.getMenuMetaDO().getTitle());
            metaVO.setIcon(menuBO.getMenuMetaDO().getIcon());
            metaVO.setActiveIcon(menuBO.getMenuMetaDO().getActiveIcon());
            metaVO.setActivePath(menuBO.getMenuMetaDO().getActivePath());
            metaVO.setAuthority(menuBO.getMenuMetaDO().getAuthority());
            metaVO.setIgnoreAccess(menuBO.getMenuMetaDO().getIgnoreAccess());
            metaVO.setMenuVisibleWithForbidden(menuBO.getMenuMetaDO().getMenuVisibleWithForbidden());
            metaVO.setHideInMenu(menuBO.getMenuMetaDO().getHideInMenu());
            metaVO.setHideInTab(menuBO.getMenuMetaDO().getHideInTab());
            metaVO.setHideInBreadcrumb(menuBO.getMenuMetaDO().getHideInBreadcrumb());
            metaVO.setHideChildrenInMenu(menuBO.getMenuMetaDO().getHideChildrenInMenu());
            metaVO.setAffixTab(menuBO.getMenuMetaDO().getAffixTab());
            vo.setMeta(metaVO);
        }
        return vo;
    }

    @Override
    public List<MenuVO> convertBOList2VOTree(List<MenuBO> menuBOList) {
        return buildRouteTree(convertBOList2VOList(menuBOList));
    }

    @Override
    public List<MenuVO> convertBOList2VOList(List<MenuBO> menuBOList) {
        if (CollectionUtils.isEmpty(menuBOList)) {
            return List.of();
        }
        return menuBOList.stream().filter(bo -> bo.getMenuDO() != null).map(this::convertBO2RouteVO).toList();
    }

    @Override
    public MenuDO convertDTO2DO(MenuDTO menuDTO) {
        MenuDO menuDO = new MenuDO();
        menuDO.setMenuId(menuDTO.getMenuId());
        menuDO.setParentMenuId(StringUtils.hasText(menuDTO.getParentMenuId()) ? menuDTO.getParentMenuId() : null);
        menuDO.setName(menuDTO.getName());
        menuDO.setPath(menuDTO.getPath());
        menuDO.setComponent(StringUtils.hasText(menuDTO.getComponent()) ? menuDTO.getComponent() : null);
        menuDO.setRedirect(StringUtils.hasText(menuDTO.getRedirect()) ? menuDTO.getRedirect() : null);
        menuDO.setMenuType(menuDTO.getMenuType());
        menuDO.setStatus(menuDTO.getStatus());
        menuDO.setSortOrder(menuDTO.getSortOrder());
        menuDO.setCreatedBy(menuDTO.getCreatedBy());
        menuDO.setModifiedBy(menuDTO.getModifiedBy());
        return menuDO;
    }

    /**
     * 构建菜单树
     */
    private List<MenuVO> buildRouteTree(List<MenuVO> routes) {
        if (CollectionUtils.isEmpty(routes)) {
            return List.of();
        }
        Map<String, MenuVO> routeMap = routes.stream().collect(Collectors.toMap(MenuVO::getMenuId, Function.identity()));

        List<MenuVO> rootRoutes = new ArrayList<>();

        for (MenuVO route : routes) {
            if (!StringUtils.hasText(route.getParentMenuId())) {
                // 根菜单
                rootRoutes.add(route);
            } else {
                // 子菜单
                MenuVO parent = routeMap.get(route.getParentMenuId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(route);
                }
            }
        }

        // 递归排序
        sortMenuTree(rootRoutes);
        return rootRoutes;
    }

    /**
     * 递归排序菜单树
     */
    private void sortMenuTree(List<MenuVO> routes) {
        if (CollectionUtils.isEmpty(routes)) {
            return;
        }

        // 按 sortOrder 排序
        routes.sort(Comparator.comparing(
                menu -> menu.getSortOrder() != null ? menu.getSortOrder() : 999
        ));

        // 递归排序子菜单
        for (MenuVO route : routes) {
            if (!CollectionUtils.isEmpty(route.getChildren())) {
                sortMenuTree(route.getChildren());
            }
        }
    }
}
