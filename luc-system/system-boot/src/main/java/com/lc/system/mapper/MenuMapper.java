package com.lc.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.system.domain.bo.MenuBO;
import com.lc.system.domain.dto.MenuDTO;
import com.lc.system.domain.entity.MenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统菜单表(luc_system.menu)表数据库访问层
 *
 * @author lucheng
 * @since 2025-08-15
 */
@Mapper
public interface MenuMapper extends BaseMapper<MenuDO> {

    /**
     * 根据用户ID查询用户有权限的菜单（包含meta信息）
     *
     * @param dto 用户ID
     * @return 菜单列表（包含meta信息）
     */
    List<MenuBO> selectMenusByDTO(@Param("dto") MenuDTO dto);
}

