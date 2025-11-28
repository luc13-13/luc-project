package com.lc.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.system.domain.dto.SysRoleDTO;
import com.lc.system.domain.entity.SysRoleDO;
import com.lc.system.domain.vo.RoleInfoVO;

import java.util.List;

/**
 * 系统角色表(luc_system.sys_role)表服务接口
 *
 * @author lucheng
 * @since 2025-09-04
 */
public interface SysRoleService extends IService<SysRoleDO> {

    List<RoleInfoVO> getRoleList();

    String saveRole(SysRoleDTO dto);

    RoleInfoVO deleteRole(String roleId);

    RoleInfoVO getRoleDetails(String roleId);
}

