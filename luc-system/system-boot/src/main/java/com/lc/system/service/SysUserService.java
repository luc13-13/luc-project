package com.lc.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.system.api.SysUserDetailDTO;
import com.lc.system.domain.dto.SysUserInfoDTO;
import com.lc.system.domain.entity.SysUserDO;

/**
 * 用户信息表(luc_system.sys_user)表服务接口
 *
 * @author lucheng
 * @since 2025-08-15
 */
public interface SysUserService extends IService<SysUserDO> {
    /**
     * 查询用户详情
     * @param username 用户名
     * @return 用户详情
     */
    SysUserDetailDTO getSysUserDetail(String username);

    /**
     * 查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    SysUserInfoDTO getSysUserInfo(String username);
}

