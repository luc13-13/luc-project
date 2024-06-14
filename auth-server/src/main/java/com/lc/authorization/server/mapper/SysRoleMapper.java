package com.lc.authorization.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.authorization.server.domain.entity.SysRoleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色信息表(SysRole)表数据库访问层
 *
 * @author lucheng
 * @since 2023-12-20 09:38:54
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRoleDO> {

}

