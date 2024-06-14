package com.lc.authorization.server.security.handler;

import com.lc.authorization.server.utils.SecurityUtils;
import com.lc.framework.redis.starter.utils.RedisHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/29 15:03
 */
@Component
public class OAuth2AuthorizationSuccessHandler implements AuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private RedisHelper redisHelper;

    // 访问/oauth2/authorize接口成功后，返回authorization_code时，将code与当前请求的jsessionid绑定
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication =
                (OAuth2AuthorizationCodeRequestAuthenticationToken) authentication;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(authorizationCodeRequestAuthentication.getRedirectUri())
                .queryParam(OAuth2ParameterNames.CODE, authorizationCodeRequestAuthentication.getAuthorizationCode().getTokenValue());
        if (StringUtils.hasText(authorizationCodeRequestAuthentication.getState())) {
            uriBuilder.queryParam(
                    OAuth2ParameterNames.STATE,
                    UriUtils.encode(authorizationCodeRequestAuthentication.getState(), StandardCharsets.UTF_8));
        }
        String tokenKey = SecurityUtils.getTokenKey(request);
        redisHelper.set(authorizationCodeRequestAuthentication.getAuthorizationCode().getTokenValue(), tokenKey, 300);
        String redirectUri = uriBuilder.build(true).toUriString();		// build(true) -> Components are explicitly encoded
        this.redirectWithCookie(request, response, redirectUri);
    }

    private void redirectWithCookie(HttpServletRequest request, HttpServletResponse response, String targetUrl) throws IOException {
        SecurityUtils.copyRequestCookieToResponse(request, response);
        this.redirectStrategy.sendRedirect(request, response, targetUrl);
    }
}
