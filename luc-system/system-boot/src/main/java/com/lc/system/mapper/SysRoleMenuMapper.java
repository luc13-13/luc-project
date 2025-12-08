package com.lc.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.system.domain.entity.SysRoleMenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色菜单关联表(luc_system.sys_role_menu)表数据库访问层
 *
 * @author lucheng
 * @since 2025-09-04
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenuDO> {

    /**
     * 根据角色ID查询菜单ID列表
     */
    @Select("SELECT menu_id FROM luc_system.sys_role_menu WHERE role_id = #{roleId}")
    List<String> selectMenuIdsByRoleId(@Param("roleId") String roleId);
}
