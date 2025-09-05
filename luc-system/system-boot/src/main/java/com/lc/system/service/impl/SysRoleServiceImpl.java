package com.lc.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.system.domain.vo.RoleInfoVO;
import com.lc.system.mapper.SysRoleMapper;
import com.lc.system.domain.entity.SysRoleDO;
import com.lc.system.service.SysRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统角色表(luc_system.sys_role)表服务实现类
 *
 * @author lucheng
 * @since 2025-09-04
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleDO> implements SysRoleService {

    @Override
    public List<RoleInfoVO> getRoleList() {
        return List.of();
    }
}

