package com.lc.auth.server.security.jwt;

import com.lc.auth.server.utils.SecurityUtils;
import com.lc.framework.redis.starter.utils.RedisHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.StringUtils;

import static com.lc.framework.core.constants.RequestHeaderConstants.ACCESS_TOKEN;

/**
 * <pre>
 *     从Redis中获取BearerToken, 不对外暴漏BearerToken值
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/8/5 15:14
 */
public class RedisBearerTokenResolver implements BearerTokenResolver {

    private final RedisHelper redisHelper;

    public RedisBearerTokenResolver(RedisHelper redisHelper) {
        this.redisHelper = redisHelper;
    }

    @Override
    public String resolve(HttpServletRequest request) {
        String parameterToken = resolveFromRequestParameters(request);
        String tokenKey = SecurityUtils.getTokenKey(request);
        if (StringUtils.hasText(tokenKey)) {
            if (parameterToken != null) {
                final BearerTokenError error = BearerTokenErrors
                        .invalidRequest("Found multiple bearer tokens in the request");
                throw new OAuth2AuthenticationException(error);
            }
            OAuth2AccessToken accessToken = redisHelper.hGet(ACCESS_TOKEN, tokenKey);
            return accessToken != null && OAuth2AccessToken.TokenType.BEARER.equals(accessToken.getTokenType()) ? accessToken.getTokenValue() : null;
        }
        return parameterToken;
    }

    private static String resolveFromRequestParameters(HttpServletRequest request) {
        String[] values = request.getParameterValues(ACCESS_TOKEN);
        if (values == null || values.length == 0) {
            return null;
        }
        if (values.length == 1) {
            return values[0];
        }
        BearerTokenError error = BearerTokenErrors.invalidRequest("Found multiple bearer tokens in the request");
        throw new OAuth2AuthenticationException(error);
    }
}
