package com.lc.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.framework.datasource.starter.annotation.DataSourceSwitch;
import com.lc.system.domain.entity.SysMenuDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜单权限表(SysMenu)表数据库访问层
 *
 * @author lucheng
 * @since 2023-12-27 16:39:20
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenuDO> {
    @DataSourceSwitch
    List<SysMenuDO> selectMenuTreeByUserId(Long userId);
}

