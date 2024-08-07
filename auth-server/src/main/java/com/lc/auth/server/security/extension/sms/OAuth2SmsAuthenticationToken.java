package com.lc.auth.server.security.extension.sms;

import com.lc.auth.server.security.extension.BasicAuthenticationToken;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/3/11 10:59
 */

public class OAuth2SmsAuthenticationToken extends BasicAuthenticationToken {
    @Getter
    private final String mobile;

    @Getter
    private final String captcha;

    public static final AuthorizationGrantType SMS = new AuthorizationGrantType("sms");

    public OAuth2SmsAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Set<String> scopes, String mobile, String captcha) {
        this(authorizationGrantType, clientPrincipal, scopes, mobile, captcha, Collections.emptyMap());
    }

    public OAuth2SmsAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Set<String> scopes, String mobile, String captcha, Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
        this.mobile = mobile;
        this.captcha = captcha;
    }


}
