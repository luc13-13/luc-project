package com.lc.system.converter.impl;

import com.lc.system.converter.SysRoleConverter;
import com.lc.system.domain.dto.SysRoleDTO;
import com.lc.system.domain.entity.SysRoleDO;
import org.springframework.stereotype.Service;

/**
 * 系统角色表(luc_system.sys_role)表对象转换接口
 *
 * @author lucheng
 * @since 2025-09-04
 */
@Service("sysRoleConvertor")
public class SysRoleConverterImpl implements SysRoleConverter {

    @Override
    public SysRoleDO convertDTO2DO(SysRoleDTO dto) {
        SysRoleDO entity = new SysRoleDO();
        entity.setRoleId(dto.getRoleId());
        entity.setRoleName(dto.getRoleName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setDtCreated(dto.getDtCreated());
        entity.setModifiedBy(dto.getModifiedBy());
        entity.setDtModified(dto.getDtModified());
        entity.setDeleted(dto.getDeleted());
        return entity;
    }
}

