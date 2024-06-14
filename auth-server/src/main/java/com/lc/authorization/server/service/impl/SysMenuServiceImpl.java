package com.lc.authorization.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.authorization.server.mapper.SysMenuMapper;
import com.lc.authorization.server.domain.entity.SysMenuDO;
import com.lc.authorization.server.service.SysMenuService;
import org.springframework.stereotype.Service;

/**
 * 菜单权限表(SysMenu)表服务实现类
 *
 * @author lucheng
 * @since 2023-12-20 09:38:38
 */
@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenuDO> implements SysMenuService {

}

