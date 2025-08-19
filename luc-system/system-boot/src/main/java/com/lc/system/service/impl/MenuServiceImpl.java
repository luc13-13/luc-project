package com.lc.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.system.converter.MenuConverter;
import com.lc.system.domain.bo.MenuBO;
import com.lc.system.domain.dto.MenuDTO;
import com.lc.system.domain.entity.MenuDO;
import com.lc.system.domain.vo.MenuVO;
import com.lc.system.mapper.MenuMapper;
import com.lc.system.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统菜单表(luc_system.menu)表服务实现类
 *
 * @author lucheng
 * @since 2025-08-15
 */
@Slf4j
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuDO> implements MenuService {

    private final MenuConverter menuConverter;

    public MenuServiceImpl(MenuConverter menuConverter) {
        this.menuConverter = menuConverter;
    }

    @Override
    public List<MenuBO> getMenuList(MenuDTO dto) {
        // 1. 通过SQL直接查询用户有权限的菜单（包含meta信息）
        List<MenuBO> menuDTOs = this.baseMapper.selectMenusByDTO(dto);

        if (CollectionUtils.isEmpty(menuDTOs)) {
            return new ArrayList<>();
        }
        return menuDTOs;
    }

    @Override
    public List<MenuVO> getRouteTreeByUserId(MenuDTO dto) {
        return menuConverter.convertBOList2VOTree(getMenuList(dto));
    }

    @Override
    public List<MenuVO> getMenuVOList(MenuDTO dto) {
        return menuConverter.convertBOList2VOList(getMenuList(dto));
    }
}

