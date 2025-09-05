package com.lc.system.convertor.impl;

import com.lc.system.convertor.SysRoleConvertor;
import com.lc.system.domain.entity.SysRoleDO;
import com.lc.system.domain.dto.SysRoleDTO;
import com.lc.system.domain.bo.SysRoleBO;
import org.springframework.stereotype.Service;

/**
 * 系统角色表(luc_system.sys_role)表对象转换接口
 *
 * @author lucheng
 * @since 2025-09-04
 */
@Service("sysRoleConvertor")
public class SysRoleConvertorImpl implements SysRoleConvertor {

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
   
    @Override
    public SysRoleDO convertBO2DO(SysRoleBO bo) {
        SysRoleDO entity = new SysRoleDO();
        entity.setRoleId(bo.getRoleId());
        entity.setRoleName(bo.getRoleName());
        entity.setDescription(bo.getDescription());
        entity.setStatus(bo.getStatus());
        entity.setCreatedBy(bo.getCreatedBy());
        entity.setDtCreated(bo.getDtCreated());
        entity.setModifiedBy(bo.getModifiedBy());
        entity.setDtModified(bo.getDtModified());
        entity.setDeleted(bo.getDeleted());
        return entity;
    }
}

