package com.lc.authorization.gateway.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Token认证转换器
 * 从请求中提取token并转换为Authentication对象
 */
@Component
public class TokenAuthenticationConverter implements ServerAuthenticationConverter {
    
    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationConverter.class);
    
    private static final String TOKEN_HEADER = "luc-auth-token";
    private static final String BEARER_PREFIX = "Bearer ";
    
    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.fromCallable(() -> {
            String token = extractToken(exchange);
            if (token != null && !token.trim().isEmpty()) {
                logger.debug("从请求中提取到token");
                // 创建未认证的Authentication对象，将在AuthenticationManager中进行验证
                return new UsernamePasswordAuthenticationToken(token, token);
            }
            return null;
        });
    }
    
    /**
     * 从请求中提取token
     */
    private String extractToken(ServerWebExchange exchange) {
        // 1. 优先从header中获取
        String authHeader = exchange.getRequest().getHeaders().getFirst(TOKEN_HEADER);
        if (authHeader != null && !authHeader.trim().isEmpty()) {
            // 支持Bearer格式
            if (authHeader.startsWith(BEARER_PREFIX)) {
                return authHeader.substring(BEARER_PREFIX.length()).trim();
            }
            return authHeader.trim();
        }
        
        // 2. 从Authorization header中获取
        String authorization = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
            return authorization.substring(BEARER_PREFIX.length()).trim();
        }
        
        // 3. 从查询参数中获取
        String tokenParam = exchange.getRequest().getQueryParams().getFirst("token");
        if (tokenParam != null && !tokenParam.trim().isEmpty()) {
            return tokenParam.trim();
        }
        
        return null;
    }
}
