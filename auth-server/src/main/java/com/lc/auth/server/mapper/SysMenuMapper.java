package com.lc.auth.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.auth.server.domain.entity.SysMenuDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单权限表(SysMenu)表数据库访问层
 *
 * @author lucheng
 * @since 2023-12-20 09:38:38
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenuDO> {

}

