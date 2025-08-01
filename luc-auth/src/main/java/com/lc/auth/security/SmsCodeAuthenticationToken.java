package com.lc.auth.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * <pre>
 * 短信验证码认证Token
 * 用于短信验证码登录的认证令牌
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object credentials;

    /**
     * 创建未认证的Token（用于认证前）
     *
     * @param phone 手机号
     * @param code  验证码
     */
    public SmsCodeAuthenticationToken(String phone, String code) {
        super(null);
        this.principal = phone;
        this.credentials = code;
        setAuthenticated(false);
    }

    /**
     * 创建已认证的Token（用于认证后）
     *
     * @param principal   用户主体
     * @param credentials 凭证
     * @param authorities 权限集合
     */
    public SmsCodeAuthenticationToken(Object principal, Object credentials,
                                      Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
