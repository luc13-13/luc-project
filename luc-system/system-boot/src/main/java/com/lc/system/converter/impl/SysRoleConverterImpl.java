package com.lc.system.converter.impl;

import com.lc.system.converter.SysRoleConverter;
import com.lc.system.domain.bo.SysRoleBO;
import com.lc.system.domain.dto.SysRoleDTO;
import com.lc.system.domain.entity.SysRoleDO;
import com.lc.system.domain.vo.RoleInfoVO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        if (dto == null) {
            return null;
        }
        SysRoleDO entity = new SysRoleDO();
        entity.setId(dto.getId());
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
    public SysRoleBO convertDO2BO(SysRoleDO entity) {
        if (entity == null) {
            return null;
        }
        return SysRoleBO.builder()
                .id(entity.getId())
                .roleId(entity.getRoleId())
                .roleName(entity.getRoleName())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .createdBy(entity.getCreatedBy())
                .dtCreated(entity.getDtCreated())
                .modifiedBy(entity.getModifiedBy())
                .dtModified(entity.getDtModified())
                .deleted(entity.getDeleted())
                .build();
    }

    @Override
    public RoleInfoVO convertBO2VO(SysRoleBO bo) {
        if (bo == null) {
            return null;
        }
        return RoleInfoVO.builder()
                .id(bo.getId())
                .roleId(bo.getRoleId())
                .roleName(bo.getRoleName())
                .description(bo.getDescription())
                .status(bo.getStatus())
                .dtCreated(bo.getDtCreated())
                .menuIds(bo.getMenuIds())
                .build();
    }

    @Override
    public List<SysRoleBO> convertDO2BO(List<SysRoleDO> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::convertDO2BO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleInfoVO> convertBO2VO(List<SysRoleBO> bos) {
        if (CollectionUtils.isEmpty(bos)) {
            return Collections.emptyList();
        }
        return bos.stream()
                .map(this::convertBO2VO)
                .collect(Collectors.toList());
    }
}
