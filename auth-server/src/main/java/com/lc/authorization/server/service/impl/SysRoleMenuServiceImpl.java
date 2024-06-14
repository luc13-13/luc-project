package com.lc.authorization.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.authorization.server.mapper.SysRoleMenuMapper;
import com.lc.authorization.server.domain.entity.SysRoleMenuDO;
import com.lc.authorization.server.service.SysRoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(SysRoleMenu)表服务实现类
 *
 * @author lucheng
 * @since 2023-12-20 09:39:13
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenuDO> implements SysRoleMenuService {

}

