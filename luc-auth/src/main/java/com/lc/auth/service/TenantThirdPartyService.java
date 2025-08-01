package com.lc.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.auth.domain.entity.TenantThirdParty;
import com.lc.auth.domain.security.TenantOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

/**
 * <pre>
 * 租户第三方账号绑定服务接口
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
public interface TenantThirdPartyService extends IService<TenantThirdParty> {

    /**
     * 根据第三方平台和用户ID查询绑定信息
     *
     * @param provider       第三方平台类型
     * @param providerUserId 第三方平台用户ID
     * @return 绑定信息
     */
    TenantThirdParty findByProviderAndUserId(String provider, String providerUserId);

    /**
     * 根据租户ID查询所有绑定的第三方账号
     *
     * @param tenantId 租户ID
     * @return 绑定信息列表
     */
    List<TenantThirdParty> findByTenantId(String tenantId);

    /**
     * 根据租户ID和第三方平台查询绑定信息
     *
     * @param tenantId 租户ID
     * @param provider 第三方平台类型
     * @return 绑定信息
     */
    TenantThirdParty findByTenantIdAndProvider(String tenantId, String provider);

    /**
     * 处理第三方登录
     *
     * @param provider   第三方平台类型
     * @param oAuth2User OAuth2用户信息
     * @return 自定义OAuth2用户
     */
    TenantOAuth2User processThirdPartyLogin(String provider, OAuth2User oAuth2User);

    /**
     * 绑定第三方账号到租户
     *
     * @param tenantId   租户ID
     * @param provider   第三方平台类型
     * @param oAuth2User OAuth2用户信息
     * @return 绑定结果
     */
    boolean bindThirdPartyToTenant(String tenantId, String provider, OAuth2User oAuth2User);

    /**
     * 解绑第三方账号
     *
     * @param tenantId 租户ID
     * @param provider 第三方平台类型
     * @return 解绑结果
     */
    boolean unbindThirdParty(String tenantId, String provider);

    /**
     * 检查第三方账号是否已绑定其他租户
     *
     * @param provider        第三方平台类型
     * @param providerUserId  第三方平台用户ID
     * @param excludeTenantId 排除的租户ID
     * @return 是否已绑定
     */
    boolean isBoundToOtherTenant(String provider, String providerUserId, String excludeTenantId);

    /**
     * 更新第三方账号登录信息
     *
     * @param provider       第三方平台类型
     * @param providerUserId 第三方平台用户ID
     */
    void updateLastLoginInfo(String provider, String providerUserId);

    /**
     * 创建第三方账号信息
     *
     * @param provider   第三方平台类型
     * @param oAuth2User OAuth2用户信息
     * @return 第三方账号信息
     */
    TenantThirdParty createThirdPartyInfo(String provider, OAuth2User oAuth2User);
}
