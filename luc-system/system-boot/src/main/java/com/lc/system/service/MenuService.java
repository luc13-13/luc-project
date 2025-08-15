package com.lc.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.system.domain.entity.MenuDO;
import com.lc.system.domain.dto.MenuDTO;

import java.util.List;

/**
 * 系统菜单表(luc_system.menu)表服务接口
 *
 * @author lucheng
 * @since 2025-08-15
 */
public interface MenuService extends IService<MenuDO> {

    /**
     * 根据用户ID获取菜单树
     *
     * @param userId 用户ID
     * @return 菜单树
     */
    List<MenuDTO> getMenuTreeByUserId(String userId);

    /**
     * 获取所有菜单树（管理后台使用）
     *
     * @return 所有菜单的树形结构
     */
    List<MenuDTO> getAllMenuTree();
}

