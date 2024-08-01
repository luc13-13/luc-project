package com.lc.auth.server.security.extension.password;

import com.lc.auth.server.security.extension.BasicAuthenticationConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;

import java.util.Set;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/11 14:50
 */
@Slf4j
public class OAuth2PasswordAuthenticationConverter extends BasicAuthenticationConverter<OAuth2PasswordAuthenticationToken> {

    @Override
    public boolean support(String grantType) {
        return AuthorizationGrantType.PASSWORD.getValue().equals(grantType);
    }

    @Override
    public OAuth2PasswordAuthenticationToken unauthenticatedToken(Authentication clientPrincipal, Set<String> requestedScopes, MultiValueMap<String, String> requestParameters) {
        return new OAuth2PasswordAuthenticationToken(AuthorizationGrantType.PASSWORD, clientPrincipal, requestedScopes, requestParameters.getFirst(OAuth2ParameterNames.USERNAME), requestParameters.getFirst(OAuth2ParameterNames.PASSWORD));
    }
}
