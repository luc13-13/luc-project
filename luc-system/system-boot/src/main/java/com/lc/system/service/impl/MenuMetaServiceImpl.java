package com.lc.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.system.mapper.MenuMetaMapper;
import com.lc.system.domain.entity.MenuMetaDO;
import com.lc.system.service.MenuMetaService;
import org.springframework.stereotype.Service;

/**
 * 系统菜单元数据表(luc_system.menu_meta)表服务实现类
 *
 * @author lucheng
 * @since 2025-08-15
 */
@Service("menuMetaService")
public class MenuMetaServiceImpl extends ServiceImpl<MenuMetaMapper, MenuMetaDO> implements MenuMetaService {

}

