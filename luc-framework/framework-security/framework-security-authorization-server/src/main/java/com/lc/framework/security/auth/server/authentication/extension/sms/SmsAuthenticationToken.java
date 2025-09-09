package com.lc.framework.security.auth.server.authentication.extension.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/5 09:29
 * @version : 1.0
 */
public class SmsAuthenticationToken extends AbstractAuthenticationToken {

    private final String phone;

    private final String code;

    /**
     *
     * @param phone 手机号
     * @param code 验证码
     */
    public SmsAuthenticationToken(String phone, String code) {
        super(null);
        this.phone = phone;
        this.code = code;
        setAuthenticated(false);
    }

    public SmsAuthenticationToken(String phone, String code, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.phone = phone;
        this.code = code;
        setAuthenticated(true);
    }

    public static SmsAuthenticationToken unauthenticated(String phone, String code) {
        return new SmsAuthenticationToken(phone, code);
    }

    public static SmsAuthenticationToken authenticated(String phone, String code, Collection<? extends GrantedAuthority> authorities) {
        return new SmsAuthenticationToken(phone, code, authorities);
    }

    @Override
    public Object getCredentials() {
        return code;
    }

    @Override
    public Object getPrincipal() {
        return phone;
    }
}
