package com.lc.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.framework.web.utils.WebUtil;
import com.lc.system.converter.SysRoleConverter;
import com.lc.system.domain.dto.SysRoleDTO;
import com.lc.system.domain.vo.RoleInfoVO;
import com.lc.system.mapper.SysRoleMapper;
import com.lc.system.domain.entity.SysRoleDO;
import com.lc.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SysRoleConverter sysRoleConverter;

    @Override
    public List<RoleInfoVO> getRoleList() {
        return List.of();
    }

    @Override
    public String saveRole(SysRoleDTO dto) {
        SysRoleDO sysRoleDO = sysRoleConverter.convertDTO2DO(dto);
        if (sysRoleDO.getId() != null) {
            // 更新操作
            sysRoleDO.setModifiedBy(WebUtil.getUserId());
        } else {
            // 新增操作
        }
        this.saveOrUpdate(sysRoleDO);
        return sysRoleDO.getRoleId();
    }

    @Override
    public RoleInfoVO deleteRole(String roleId) {
        return null;
    }

    @Override
    public RoleInfoVO getRoleDetails(String roleId) {
        return null;
    }
}

