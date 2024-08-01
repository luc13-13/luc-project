package com.lc.auth.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.auth.server.domain.entity.SysUserRoleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户和角色关联表(SysUserRole)表数据库访问层
 *
 * @author lucheng
 * @since 2023-12-20 09:39:32
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRoleDO> {

}

