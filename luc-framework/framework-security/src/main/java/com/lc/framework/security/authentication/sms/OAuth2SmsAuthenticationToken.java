package com.lc.framework.security.authentication.sms;

import com.lc.framework.security.authentication.BasicAuthenticationToken;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

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

    public OAuth2SmsAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Set<String> scopes, String mobile, String captcha) {
        super(authorizationGrantType, clientPrincipal, scopes);
        this.mobile = mobile;
        this.captcha = captcha;
    }


}
