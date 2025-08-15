package com.lc.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.system.domain.dto.MenuDTO;
import com.lc.system.domain.entity.MenuDO;
import com.lc.system.mapper.MenuMapper;
import com.lc.system.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 系统菜单表(luc_system.menu)表服务实现类
 *
 * @author lucheng
 * @since 2025-08-15
 */
@Slf4j
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuDO> implements MenuService {

    @Override
    public List<MenuDTO> getMenuTreeByUserId(String userId) {
        // 1. 通过SQL直接查询用户有权限的菜单（包含meta信息）
        List<MenuDTO> menuDTOs = this.baseMapper.selectMenusByUserId(userId);

        if (CollectionUtils.isEmpty(menuDTOs)) {
            return new ArrayList<>();
        }

        // 2. 构建树形结构
        return buildMenuTree(menuDTOs);
    }

    @Override
    public List<MenuDTO> getAllMenuTree() {
        // 1. 通过SQL直接查询所有菜单（包含meta信息）
        List<MenuDTO> menuDTOs = this.baseMapper.selectAllMenusWithMeta();

        if (CollectionUtils.isEmpty(menuDTOs)) {
            return new ArrayList<>();
        }

        // 2. 构建树形结构
        return buildMenuTree(menuDTOs);
    }



    /**
     * 构建菜单树
     */
    private List<MenuDTO> buildMenuTree(List<MenuDTO> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return new ArrayList<>();
        }

        Map<String, MenuDTO> menuMap = menus.stream()
                .collect(Collectors.toMap(MenuDTO::getMenuId, Function.identity()));

        List<MenuDTO> rootMenus = new ArrayList<>();

        for (MenuDTO menu : menus) {
            if (!StringUtils.hasText(menu.getParentMenuId())) {
                // 根菜单
                rootMenus.add(menu);
            } else {
                // 子菜单
                MenuDTO parent = menuMap.get(menu.getParentMenuId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(menu);
                }
            }
        }

        // 递归排序
        sortMenuTree(rootMenus);
        return rootMenus;
    }

    /**
     * 递归排序菜单树
     */
    private void sortMenuTree(List<MenuDTO> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return;
        }

        // 按 sortOrder 排序
        menus.sort(Comparator.comparing(
                menu -> menu.getSortOrder() != null ? menu.getSortOrder() : 999
        ));

        // 递归排序子菜单
        for (MenuDTO menu : menus) {
            if (!CollectionUtils.isEmpty(menu.getChildren())) {
                sortMenuTree(menu.getChildren());
            }
        }
    }
}

