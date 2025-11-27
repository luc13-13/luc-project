package com.lc.authorization.gateway.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/9/9 09:17
 * @version : 1.0
 */
@Slf4j
public class RedisServerSecurityContextRepository implements ServerSecurityContextRepository {
    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    /**
     * 认证信息在redis中的key
     */
    public static final String SECURITY_CONTEXT_PREFIX = "spring:security:context:";
    /**
     * 返回给前端的请求头
     */
    public static final String AUTH_KEY = "luc_auth_token";


    public RedisServerSecurityContextRepository(ReactiveRedisTemplate<String, Object> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String key = exchange.getRequest().getHeaders().getFirst(AUTH_KEY);
        log.info("key:{}", key);
        return reactiveRedisTemplate.opsForValue().get(SECURITY_CONTEXT_PREFIX + key).mapNotNull(context -> {
            if (context instanceof SecurityContext) {
                log.info("reactive redis security repository load context: {}", context);
                return context;
            }
            return null;
        }).filter(Objects::nonNull).cast(SecurityContext.class);
    }
}
