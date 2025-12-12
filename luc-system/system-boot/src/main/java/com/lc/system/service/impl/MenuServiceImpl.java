package com.lc.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.framework.core.mvc.BizException;
import com.lc.framework.web.utils.WebUtil;
import com.lc.system.converter.MenuConverter;
import com.lc.system.converter.MenuMetaConverter;
import com.lc.system.domain.bo.MenuBO;
import com.lc.system.domain.dto.MenuDTO;
import com.lc.system.domain.entity.MenuDO;
import com.lc.system.domain.entity.MenuMetaDO;
import com.lc.system.domain.vo.MenuVO;
import com.lc.system.mapper.MenuMapper;
import com.lc.system.service.MenuMetaService;
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

    private final MenuMetaConverter menuMetaConverter;

    private final MenuMetaService menuMetaService;

    public MenuServiceImpl(MenuConverter menuConverter, MenuMetaConverter menuMetaConverter,
            MenuMetaService menuMetaService) {
        this.menuConverter = menuConverter;
        this.menuMetaConverter = menuMetaConverter;
        this.menuMetaService = menuMetaService;
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

    @Override
    public void saveMenu(MenuDTO dto) {
        // 转换为数据库对象
        MenuDO menuDO = menuConverter.convertDTO2DO(dto);
        // 提取元数据
        MenuMetaDO menuMetaDO = menuMetaConverter.convertMenuDTO2DO(dto);
        // 封装创建人
        String createBy = WebUtil.getUserId();
        menuDO.setCreatedBy(createBy);
        menuMetaDO.setCreatedBy(createBy);
        this.save(menuDO);
        menuMetaService.save(menuMetaDO);
    }

    @Override
    public void updateMenu(MenuDTO dto) {
        // 1. 检查菜单是否存在
        MenuDO existingMenu = this.getById(dto.getId());
        if (existingMenu == null) {
            throw BizException.exp("菜单不存在: " + dto.getId());
        }

        // 2. 更新 MenuDO
        MenuDO menuDO = menuConverter.convertDTO2DO(dto);
        this.updateById(menuDO);

        // 3. 更新 MenuMetaDO
        // 注意：DTO 中的 children 被忽略，不进行级联更新
        if (dto.getMeta() != null) {
            MenuMetaDO menuMetaDO = menuMetaConverter.convertMenuDTO2DO(dto);

            LambdaUpdateWrapper<MenuMetaDO> metaUpdateWrapper = new LambdaUpdateWrapper<>();
            metaUpdateWrapper.eq(MenuMetaDO::getMenuId, existingMenu.getMenuId());
            menuMetaService.update(menuMetaDO, metaUpdateWrapper);
        }
    }

    @Override
    public void deleteMenu(Long id) {
        // 1. 检查菜单是否存在
        MenuDO menuDO = this.getById(id);
        if (menuDO == null) {
            throw BizException.exp("菜单不存在: " + id);
        }

        // 2. 检查是否有子菜单 (查询数据库)
        LambdaQueryWrapper<MenuDO> childQuery = new LambdaQueryWrapper<>();
        childQuery.eq(MenuDO::getParentMenuId, menuDO.getMenuId());
        if (this.count(childQuery) > 0) {
            throw BizException.exp("存在子菜单，不允许删除");
        }

        // 3. 逻辑删除 MenuDO
        this.removeById(id);

        // 4. 逻辑删除 MenuMetaDO
        LambdaUpdateWrapper<MenuMetaDO> metaDeleteWrapper = new LambdaUpdateWrapper<>();
        metaDeleteWrapper.eq(MenuMetaDO::getMenuId, menuDO.getMenuId());
        menuMetaService.remove(metaDeleteWrapper);
    }
}
