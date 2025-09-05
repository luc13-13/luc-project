package com.lc.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.system.api.SysUserDetailDTO;
import com.lc.system.converter.SysUserConverter;
import com.lc.system.domain.bo.MenuBO;
import com.lc.system.domain.dto.MenuDTO;
import com.lc.system.domain.entity.SysUserDO;
import com.lc.system.mapper.SysUserMapper;
import com.lc.system.service.MenuService;
import com.lc.system.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lc.framework.core.constants.NumberConstants.STATUS_TRUE;

/**
 * 用户信息表(luc_system.sys_user)表服务实现类
 *
 * @author lucheng
 * @since 2025-08-15
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserDO> implements SysUserService {

    private final MenuService menuService;

    private final SysUserConverter sysUserConverter;

    public SysUserServiceImpl(MenuService menuService, SysUserConverter sysUserConverter) {
        this.menuService = menuService;
        this.sysUserConverter = sysUserConverter;
    }

    @Override
    public SysUserDetailDTO getSysUserDetail(String username) {
        // 查询账号
        SysUserDO userDO = this.getOne(new LambdaQueryWrapper<SysUserDO>().eq(SysUserDO::getUserName, username));
        // 查询权限信息
        Map<String, List<String>> roleMenuMap = menuService.getMenuList(MenuDTO.builder().userId(userDO.getUserId()).status(STATUS_TRUE).build())
                .stream().collect(Collectors.groupingBy(MenuBO::getRoleId,
                        Collectors.mapping(menuBO -> menuBO.getMenuMetaDO().getAuthority(), Collectors.toList())));
        // 数据转换
        SysUserDetailDTO sysUserDetailDTO = sysUserConverter.convertDO2DetailDTO(userDO);
        sysUserDetailDTO.setRoleAuthoritiesMap(roleMenuMap);
        return sysUserDetailDTO;
    }
}

