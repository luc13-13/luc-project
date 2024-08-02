package com.lc.auth.gateway.security;

import com.lc.framework.redis.starter.utils.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;
import static com.lc.framework.core.constants.RequestHeaderConstants.ACCESS_TOKEN;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

/**
 * <pre>
 *     重写webflux资源服务器获取请求token的方法，根据请求头X-Access-Token从Redis中获取
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/8/2 11:04
 */
@Component
public class LucBearerServerAuthenticationConverter implements ServerAuthenticationConverter {

    @Autowired
    private RedisHelper redisHelper;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.fromCallable(() -> token(exchange.getRequest())).map((token) -> {
            if (token.isEmpty()) {
                BearerTokenError error = BearerTokenErrors.invalidToken("Bearer token is malformed");
                throw new OAuth2AuthenticationException(error);
            }
            return new BearerTokenAuthenticationToken(token);
        });
    }

    private String token(ServerHttpRequest request) {
        // 从请求头获取key
        String authorizationHeaderToken = resolveFromAuthorizationHeader(request.getHeaders());
        // 从请求参数获取key
        String parameterToken = resolveAccessTokenFromRequest(request);

        OAuth2AccessToken auth2AccessToken = null;

        if (authorizationHeaderToken != null) {
            if (parameterToken != null) {
                BearerTokenError error = BearerTokenErrors
                        .invalidRequest("Found multiple bearer tokens in the request! header: " + authorizationHeaderToken  + " queryParameter: " + parameterToken);
                throw new OAuth2AuthenticationException(error);
            }
            // 从redis中获取真正的tokenValue
            auth2AccessToken = redisHelper.hGet(ACCESS_TOKEN, authorizationHeaderToken);
        } else {
            if (parameterToken != null) {
                auth2AccessToken = redisHelper.hGet(ACCESS_TOKEN, parameterToken);
            }
        }
        if (auth2AccessToken == null || !BEARER.equals(auth2AccessToken.getTokenType())) {
            throw new OAuth2AuthenticationException(BearerTokenErrors.invalidToken("未找到token, tokenKey: " + authorizationHeaderToken));
        }

        return auth2AccessToken.getTokenValue();
    }

    private static String resolveAccessTokenFromRequest(ServerHttpRequest request) {
        List<String> parameterTokens = request.getQueryParams().get(ACCESS_TOKEN);
        if (CollectionUtils.isEmpty(parameterTokens)) {
            return null;
        }
        if (parameterTokens.size() == 1) {
            return parameterTokens.get(0);
        }

        BearerTokenError error = BearerTokenErrors.invalidRequest("Found multiple bearer tokens in the request");
        throw new OAuth2AuthenticationException(error);

    }

    private String resolveFromAuthorizationHeader(HttpHeaders headers) {
        return headers.getFirst(ACCESS_TOKEN);
    }
}
