package com.lc.authorization.server.security.extension;

import com.lc.framework.core.constants.StringConstants;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 * 自定义认证方式的基础Token, 封装用户权限
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-12-07 09:29
 */
public abstract class BasicAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private final AuthorizationGrantType authorizationGrantType;

    @Getter
    private final Authentication clientPrincipal;

    @Getter
    private final Set<String> scopes;

    @Getter
    private final Map<String, Object> additionalParameters;


    public BasicAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                    Authentication clientPrincipal,
                                    Set<String> scopes, Map<String, Object> additionalParameters) {
        super(null);
        Assert.notNull(authorizationGrantType, "authorizationGrantType cannot be null");
        Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");
        this.authorizationGrantType = authorizationGrantType;
        this.clientPrincipal = clientPrincipal;
        this.scopes = scopes;
        this.additionalParameters = Collections.unmodifiableMap(additionalParameters != null ? additionalParameters : Collections.emptyMap());
    }

    @Override
    public Object getCredentials() {
        return StringConstants.EMPTY_STRING;
    }

    public Object getPrincipal() {
        return this.clientPrincipal;
    }
}
