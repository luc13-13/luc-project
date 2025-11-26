package com.lc.system.converter;

import com.lc.system.domain.entity.SysRoleDO;
import com.lc.system.domain.dto.SysRoleDTO;
import com.lc.system.domain.bo.SysRoleBO;

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
     * 转换BO为数据库对象
     * @param bo 请求参数
     * @return 数据库对象
     */
    SysRoleDO convertBO2DO(SysRoleBO bo);
}

