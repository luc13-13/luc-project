package com.lc.authorization.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * JWT 认证全局过滤器
 * 在请求转发到后端服务之前，将用户信息添加到请求头中
 *
 * @author Lu Cheng
 * @date 2025/8/15
 */
@Slf4j
@Component
public class JwtAuthenticationGlobalFilter implements GlobalFilter, Ordered {

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USERNAME_HEADER = "X-Username";
    private static final String USER_AUTHORITIES_HEADER = "X-User-Authorities";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .cast(org.springframework.security.core.context.SecurityContext.class)
                .map(org.springframework.security.core.context.SecurityContext::getAuthentication)
                .cast(Authentication.class)
                .filter(auth -> auth instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .map(jwtAuth -> {
                    Jwt jwt = jwtAuth.getToken();
                    log.info("jwt claims: {}", jwt.getClaims());
                    
                    // 从 JWT 中提取用户信息
                    String userId = extractUserId(jwt);
                    String username = extractUsername(jwt);
                    String authorities = extractAuthorities(jwtAuth);
                    
                    log.debug("JWT认证成功，用户: {}, ID: {}, 权限: {}", username, userId, authorities);
                    
                    // 构建新的请求，添加用户信息到请求头
                    ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
                    if (StringUtils.hasText(userId)) {
                        requestBuilder.header(USER_ID_HEADER, userId);
                    }
                    if (StringUtils.hasText(username)) {
                        requestBuilder.header(USERNAME_HEADER, username);
                    }
                    if (StringUtils.hasText(authorities)) {
                        requestBuilder.header(USER_AUTHORITIES_HEADER, authorities);
                    }
                    
                    return exchange.mutate().request(requestBuilder.build()).build();
                })
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }

    /**
     * 从 JWT 中提取用户ID
     */
    private String extractUserId(Jwt jwt) {
        // 尝试从不同的声明中获取用户ID
        Object userIdClaim = jwt.getClaim("user_id");
        if (userIdClaim != null) {
            return userIdClaim.toString();
        }
        
        // 如果没有 user_id，尝试使用 sub（subject）
        String subject = jwt.getSubject();
        if (StringUtils.hasText(subject)) {
            return subject;
        }
        
        return null;
    }

    /**
     * 从 JWT 中提取用户名
     */
    private String extractUsername(Jwt jwt) {
        // 尝试从不同的声明中获取用户名
        Object usernameClaim = jwt.getClaim("username");
        if (usernameClaim != null) {
            return usernameClaim.toString();
        }
        
        // 尝试从 preferred_username 获取
        Object preferredUsernameClaim = jwt.getClaim("preferred_username");
        if (preferredUsernameClaim != null) {
            return preferredUsernameClaim.toString();
        }
        
        // 如果没有用户名，使用 subject
        return jwt.getSubject();
    }

    /**
     * 从认证对象中提取权限信息
     */
    private String extractAuthorities(JwtAuthenticationToken jwtAuth) {
        return jwtAuth.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }

    @Override
    public int getOrder() {
        // 设置在 Redis 认证过滤器之后执行
        return -100;
    }
}
