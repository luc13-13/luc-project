package com.lc.auth.gateway.security;

import com.lc.framework.redis.starter.utils.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import reactor.core.publisher.Mono;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/8 16:47
 */
@Slf4j
public class LucAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    private final ServerWebExchangeMatcher whitePathMatcher;
    private final RedisHelper redisHelper;

    public LucAuthorizationManager(ServerWebExchangeMatcher whitePathMatcher, RedisHelper redisHelper) {
        this.whitePathMatcher = whitePathMatcher;
        this.redisHelper = redisHelper;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        //
        ServerHttpRequest request = context.getExchange().getRequest();
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(true));
        }
        String uri = request.getURI().getPath();
        // 白名单已在路径匹配时进行放行，此处校验当前用户是否拥有访问当前路径的权限
//        String jsessionid = WebFluxUtils.getHeaderValue(request, JSESSIONID);
        // （1）获取X-Access-Token：根据请求JSESSIONID从redis获取
//        Authentication accessToken = redisHelper.hGet(ACCESS_TOKEN, jsessionid);
        // （2）获取X-Refresh-Token：根据请求JSESSIONID从redis获取
        log.info("校验是否认证:{}", uri);
        return authentication
                .map(obj -> {
                    log.info("开始认证——是否认证：{}, credentials: {}", obj.isAuthenticated(), obj.getCredentials());
                    return new AuthorizationDecision(true);
                });
    }
}
