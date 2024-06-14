package com.lc.framework.security.authentication.password;

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
 * @date 2023/12/11 11:11
 */
public class OAuth2PasswordAuthenticationToken extends BasicAuthenticationToken {

    @Getter
    private final String username;

    @Getter
    private final String password;

    public OAuth2PasswordAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Set<String> scopes, String username, String password) {
        super(authorizationGrantType, clientPrincipal, scopes);
        this.username = username;
        this.password = password;
    }
}
