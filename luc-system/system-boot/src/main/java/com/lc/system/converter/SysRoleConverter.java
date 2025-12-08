package com.lc.system.converter;

import com.lc.system.domain.bo.SysRoleBO;
import com.lc.system.domain.dto.SysRoleDTO;
import com.lc.system.domain.entity.SysRoleDO;
import com.lc.system.domain.vo.RoleInfoVO;

import java.util.List;

/**
 * 系统角色表(luc_system.sys_role)表对象转换接口
 *
 * @author lucheng
 * @since 2025-09-04
 */
public interface SysRoleConverter {

    /**
     * 转换DTO为数据库对象
     * @param dto 请求参数
     * @return 数据库对象
     */
    SysRoleDO convertDTO2DO(SysRoleDTO dto);

    /**
     * 转换DO为BO
     * @param entity 数据库对象
     * @return BO对象
     */
    SysRoleBO convertDO2BO(SysRoleDO entity);

    /**
     * 转换BO为VO
     * @param bo 业务对象
     * @return VO对象
     */
    RoleInfoVO convertBO2VO(SysRoleBO bo);

    /**
     * 批量转换DO为BO
     * @param entities 数据库对象列表
     * @return BO对象列表
     */
    List<SysRoleBO> convertDO2BO(List<SysRoleDO> entities);

    /**
     * 批量转换BO为VO
     * @param bos 业务对象列表
     * @return VO对象列表
     */
    List<RoleInfoVO> convertBO2VO(List<SysRoleBO> bos);
}

