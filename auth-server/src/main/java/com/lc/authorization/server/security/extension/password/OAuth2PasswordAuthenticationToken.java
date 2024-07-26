package com.lc.authorization.server.security.extension.password;

import com.lc.authorization.server.security.extension.BasicAuthenticationToken;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
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

    public OAuth2PasswordAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Set<String> scopes, String username, String password, Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
        this.username = username;
        this.password = password;
    }

    public OAuth2PasswordAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Set<String> scopes, String username, String password) {
        this(authorizationGrantType, clientPrincipal, scopes,  username, password, null);
    }
}
