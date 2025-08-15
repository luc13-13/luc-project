package com.lc.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.system.mapper.SysUserMapper;
import com.lc.system.domain.entity.SysUserDO;
import com.lc.system.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * 用户信息表(luc_system.sys_user)表服务实现类
 *
 * @author lucheng
 * @since 2025-08-15
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserDO> implements SysUserService {

}

