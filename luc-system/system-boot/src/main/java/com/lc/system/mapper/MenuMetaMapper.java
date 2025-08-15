package com.lc.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.system.domain.entity.MenuMetaDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统菜单元数据表(luc_system.menu_meta)表数据库访问层
 *
 * @author lucheng
 * @since 2025-08-15
 */
@Mapper
public interface MenuMetaMapper extends BaseMapper<MenuMetaDO> {

}

