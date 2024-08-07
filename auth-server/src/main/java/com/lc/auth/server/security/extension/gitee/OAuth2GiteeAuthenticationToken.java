package com.lc.auth.server.security.extension.gitee;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/8/7 14:20
 */
public class OAuth2GiteeAuthenticationToken {
    public static final AuthorizationGrantType GITEE = new AuthorizationGrantType("gitee");
}
