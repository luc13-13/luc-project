package com.lc.auth.domain.security;

import com.lc.auth.domain.entity.Tenant;
import com.lc.auth.domain.entity.TenantThirdParty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 * 自定义OAuth2用户实现
 * 实现Spring Security OAuth2的OAuth2User接口，用于第三方登录
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TenantOAuth2User implements OAuth2User {

    /**
     * 租户信息（可能为空，表示未绑定）
     */
    private Tenant tenant;

    /**
     * 第三方账号信息
     */
    private TenantThirdParty thirdParty;

    /**
     * 第三方平台返回的原始属性
     */
    private Map<String, Object> attributes;

    /**
     * 用户详情（用于权限管理）
     */
    private TenantUserDetails userDetails;

    /**
     * 构造函数 - 未绑定租户的第三方用户
     *
     * @param thirdParty 第三方账号信息
     * @param attributes 第三方平台属性
     */
    public TenantOAuth2User(TenantThirdParty thirdParty, Map<String, Object> attributes) {
        this.thirdParty = thirdParty;
        this.attributes = attributes;
        this.tenant = null;
        this.userDetails = null;
    }

    /**
     * 构造函数 - 已绑定租户的第三方用户
     *
     * @param tenant      租户信息
     * @param thirdParty  第三方账号信息
     * @param attributes  第三方平台属性
     * @param authorities 权限集合
     * @param roles       角色集合
     */
    public TenantOAuth2User(Tenant tenant, TenantThirdParty thirdParty, Map<String, Object> attributes,
                            Set<String> authorities, Set<String> roles) {
        this.tenant = tenant;
        this.thirdParty = thirdParty;
        this.attributes = attributes;
        this.userDetails = new TenantUserDetails(tenant, authorities, roles);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails != null ? userDetails.getAuthorities() : Set.of();
    }

    @Override
    public String getName() {
        // 如果已绑定租户，返回租户用户名；否则返回第三方用户名
        if (tenant != null && tenant.getUsername() != null) {
            return tenant.getUsername();
        }
        return thirdParty != null ? thirdParty.getProviderUsername() : null;
    }

    /**
     * 获取租户ID
     *
     * @return 租户ID
     */
    public String getTenantId() {
        return tenant != null ? tenant.getTenantId() : null;
    }

    /**
     * 获取第三方平台类型
     *
     * @return 第三方平台类型
     */
    public String getProvider() {
        return thirdParty != null ? thirdParty.getProvider() : null;
    }

    /**
     * 获取第三方平台用户ID
     *
     * @return 第三方平台用户ID
     */
    public String getProviderUserId() {
        return thirdParty != null ? thirdParty.getProviderUserId() : null;
    }

    /**
     * 获取第三方平台用户昵称
     *
     * @return 第三方平台用户昵称
     */
    public String getProviderNickname() {
        return thirdParty != null ? thirdParty.getProviderNickname() : null;
    }

    /**
     * 获取第三方平台用户头像
     *
     * @return 第三方平台用户头像
     */
    public String getProviderAvatar() {
        return thirdParty != null ? thirdParty.getProviderAvatar() : null;
    }

    /**
     * 获取第三方平台用户邮箱
     *
     * @return 第三方平台用户邮箱
     */
    public String getProviderEmail() {
        return thirdParty != null ? thirdParty.getProviderEmail() : null;
    }

    /**
     * 是否已绑定租户
     *
     * @return true-已绑定，false-未绑定
     */
    public boolean isBoundToTenant() {
        return tenant != null;
    }

    /**
     * 是否启用
     *
     * @return true-启用，false-禁用
     */
    public boolean isEnabled() {
        return userDetails != null && userDetails.isEnabled();
    }

    /**
     * 账号是否未锁定
     *
     * @return true-未锁定，false-已锁定
     */
    public boolean isAccountNonLocked() {
        return userDetails == null || userDetails.isAccountNonLocked();
    }
}
