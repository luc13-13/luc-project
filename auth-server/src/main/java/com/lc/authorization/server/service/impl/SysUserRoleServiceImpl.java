package com.lc.authorization.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.authorization.server.mapper.SysUserRoleMapper;
import com.lc.authorization.server.domain.entity.SysUserRoleDO;
import com.lc.authorization.server.service.SysUserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(SysUserRole)表服务实现类
 *
 * @author lucheng
 * @since 2023-12-20 09:39:32
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRoleDO> implements SysUserRoleService {

}

