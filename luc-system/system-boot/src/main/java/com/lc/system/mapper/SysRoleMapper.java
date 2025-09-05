package com.lc.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.system.domain.entity.SysRoleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统角色表(luc_system.sys_role)表数据库访问层
 *
 * @author lucheng
 * @since 2025-09-04
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRoleDO> {

}

