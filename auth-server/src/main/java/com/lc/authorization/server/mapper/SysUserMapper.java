package com.lc.authorization.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.authorization.server.domain.entity.SysUserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息表(SysUser)表数据库访问层
 *
 * @author lucheng
 * @since 2023-12-20 09:32:17
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserDO> {

}

