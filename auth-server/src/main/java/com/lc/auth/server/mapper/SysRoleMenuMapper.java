package com.lc.auth.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.auth.server.domain.entity.SysRoleMenuDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色和菜单关联表(SysRoleMenu)表数据库访问层
 *
 * @author lucheng
 * @since 2023-12-20 09:39:13
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenuDO> {

}

