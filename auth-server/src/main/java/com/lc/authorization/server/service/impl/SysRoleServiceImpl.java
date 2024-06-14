package com.lc.authorization.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.authorization.server.mapper.SysRoleMapper;
import com.lc.authorization.server.domain.entity.SysRoleDO;
import com.lc.authorization.server.service.SysRoleService;
import org.springframework.stereotype.Service;

/**
 * 角色信息表(SysRole)表服务实现类
 *
 * @author lucheng
 * @since 2023-12-20 09:38:54
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleDO> implements SysRoleService {

}

