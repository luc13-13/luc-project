package com.lc.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.system.domain.bo.MenuBO;
import com.lc.system.domain.dto.MenuDTO;
import com.lc.system.domain.entity.MenuDO;
import com.lc.system.domain.vo.MenuVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统菜单表(luc_system.menu)表服务接口
 *
 * @author lucheng
 * @since 2025-08-15
 */
public interface MenuService extends IService<MenuDO> {

    /**
     * 查询菜单列表
     * @param dto 查询条件
     * @return 菜单列表
     */
    List<MenuBO> getMenuList(MenuDTO dto);

    /**
     * 根据用户ID获取菜单树
     *
     * @param dto 查询条件
     * @return 菜单树
     */
    List<MenuVO> getRouteTreeByUserId(MenuDTO dto);

    List<MenuVO> getMenuVOList(MenuDTO dto);

    @Transactional(rollbackFor = Exception.class)
    void saveMenu(MenuDTO dto);
}

