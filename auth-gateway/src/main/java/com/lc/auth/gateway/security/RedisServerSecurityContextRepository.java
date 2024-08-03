package com.lc.auth.gateway.security;

import com.lc.auth.gateway.utils.WebFluxUtils;
import com.lc.framework.redis.starter.utils.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static com.lc.framework.core.constants.RequestHeaderConstants.ACCESS_TOKEN;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/19 16:00
 */
@Slf4j
@Component
public class RedisServerSecurityContextRepository implements ServerSecurityContextRepository {


    @Autowired
    private RedisHelper redisHelper;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        ServerHttpRequest request = exchange.getRequest();
        String tokenKey = WebFluxUtils.getHeaderValue(request, ACCESS_TOKEN);
        log.info("网关保存认证信息：{}, context: {}", tokenKey,context);
        return Mono.<Void>defer(() -> {
                    log.info("写入SecurityContext");
                    redisHelper.set(tokenKey, context);
                    return Mono.empty();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.parallel())
                .then();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String tokenKey = WebFluxUtils.getHeaderValue(request, ACCESS_TOKEN);
        SecurityContext context = null;
        if (StringUtils.hasText(tokenKey)) {
            context = redisHelper.expired(tokenKey, "security_context", 3600);
        }
//        if (context == null) {
//            context = new SecurityContextImpl();
//            AnonymousAuthenticationToken anonymous = new AnonymousAuthenticationToken("key", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
//            context.setAuthentication(anonymous);
//        }
        log.info("网关获取tokenKey: {} 的认证信息, {}", tokenKey, context);
        return Mono.justOrEmpty(context);
    }
}
