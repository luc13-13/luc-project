package com.lc.auth.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.auth.server.domain.entity.SysUserDO;
import com.lc.auth.server.mapper.SysUserMapper;
import com.lc.auth.server.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * 用户信息表(SysUser)表服务实现类
 *
 * @author lucheng
 * @since 2023-12-20 09:37:21
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserDO> implements SysUserService {

}

