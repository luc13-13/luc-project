package com.lc.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.auth.domain.entity.Tenant;
import com.lc.auth.domain.security.TenantUserDetails;

/**
 * <pre>
 * 租户服务接口
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
public interface TenantService extends IService<Tenant> {

    /**
     * 根据用户名查询租户
     *
     * @param username 用户名
     * @return 租户信息
     */
    Tenant findByUsername(String username);

    /**
     * 根据手机号查询租户
     *
     * @param phone 手机号
     * @return 租户信息
     */
    Tenant findByPhone(String phone);

    /**
     * 根据邮箱查询租户
     *
     * @param email 邮箱
     * @return 租户信息
     */
    Tenant findByEmail(String email);

    /**
     * 根据租户ID查询租户
     *
     * @param tenantId 租户ID
     * @return 租户信息
     */
    Tenant findByTenantId(String tenantId);

    /**
     * 创建租户
     *
     * @param tenant 租户信息
     * @return 创建结果
     */
    boolean createTenant(Tenant tenant);

    /**
     * 租户注册
     *
     * @param username 用户名
     * @param password 密码
     * @param phone    手机号
     * @param email    邮箱（可选）
     * @return 注册结果
     */
    Tenant registerTenant(String username, String password, String phone, String email);

    /**
     * 验证租户密码
     *
     * @param tenant      租户信息
     * @param rawPassword 原始密码
     * @return 验证结果
     */
    boolean validatePassword(Tenant tenant, String rawPassword);

    /**
     * 更新租户最后登录信息
     *
     * @param tenantId 租户ID
     * @param loginIp  登录IP
     */
    void updateLastLoginInfo(String tenantId, String loginIp);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @return 是否存在
     */
    boolean existsByPhone(String phone);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 加载用户详情（用于Spring Security）
     *
     * @param username 用户名
     * @return 用户详情
     */
    TenantUserDetails loadUserByUsername(String username);

    /**
     * 根据手机号加载用户详情
     *
     * @param phone 手机号
     * @return 用户详情
     */
    TenantUserDetails loadUserByPhone(String phone);
}
