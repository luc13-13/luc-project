package com.lc.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.system.domain.bo.SysRoleBO;
import com.lc.system.domain.entity.SysRoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统角色表(luc_system.sys_role)表数据库访问层
 *
 * @author lucheng
 * @since 2025-09-04
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRoleDO> {

    /**
     * 查询角色列表（含关联菜单ID）
     */
    List<SysRoleBO> selectRoleListWithMenus();

    /**
     * 查询单个角色详情（含关联菜单ID）
     */
    SysRoleBO selectRoleDetailWithMenus(@Param("roleId") String roleId);
}
