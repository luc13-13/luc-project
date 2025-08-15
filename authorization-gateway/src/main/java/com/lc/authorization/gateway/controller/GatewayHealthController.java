package com.lc.authorization.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 网关健康检查和调试控制器
 *
 * @author Lu Cheng
 * @date 2025/8/15
 */
@RestController
@RequestMapping("/gateway")
@RequiredArgsConstructor
public class GatewayHealthController {

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Mono<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "authorization-gateway");
        return Mono.just(health);
    }

    /**
     * 获取当前认证用户信息（用于调试）
     */
    @GetMapping("/user-info")
    public Mono<Map<String, Object>> getUserInfo() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    Authentication authentication = securityContext.getAuthentication();
                    Map<String, Object> userInfo = new HashMap<>();
                    
                    if (authentication instanceof JwtAuthenticationToken) {
                        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
                        Jwt jwt = jwtAuth.getToken();
                        
                        userInfo.put("authenticated", true);
                        userInfo.put("username", jwt.getClaimAsString("username"));
                        userInfo.put("userId", jwt.getClaimAsString("user_id"));
                        userInfo.put("subject", jwt.getSubject());
                        userInfo.put("issuer", jwt.getIssuer());
                        userInfo.put("issuedAt", jwt.getIssuedAt());
                        userInfo.put("expiresAt", jwt.getExpiresAt());
                        userInfo.put("authorities", jwtAuth.getAuthorities());
                        userInfo.put("scopes", jwt.getClaimAsString("scope"));
                    } else {
                        userInfo.put("authenticated", false);
                        userInfo.put("authType", authentication != null ? authentication.getClass().getSimpleName() : "null");
                    }
                    
                    return userInfo;
                })
                .defaultIfEmpty(Map.of("authenticated", false, "message", "No security context"));
    }

    /**
     * JWT Token 验证状态检查
     */
    @GetMapping("/token-status")
    public Mono<Map<String, Object>> getTokenStatus() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    Authentication authentication = securityContext.getAuthentication();
                    Map<String, Object> tokenStatus = new HashMap<>();
                    
                    if (authentication instanceof JwtAuthenticationToken) {
                        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
                        Jwt jwt = jwtAuth.getToken();
                        
                        tokenStatus.put("valid", true);
                        tokenStatus.put("tokenType", "JWT");
                        tokenStatus.put("issuedAt", jwt.getIssuedAt());
                        tokenStatus.put("expiresAt", jwt.getExpiresAt());
                        tokenStatus.put("issuer", jwt.getIssuer());
                        tokenStatus.put("audience", jwt.getAudience());
                        
                        // 检查是否即将过期（30分钟内）
                        if (jwt.getExpiresAt() != null) {
                            long secondsUntilExpiry = jwt.getExpiresAt().getEpochSecond() - 
                                    java.time.Instant.now().getEpochSecond();
                            tokenStatus.put("secondsUntilExpiry", secondsUntilExpiry);
                            tokenStatus.put("willExpireSoon", secondsUntilExpiry < 1800); // 30分钟
                        }
                    } else {
                        tokenStatus.put("valid", false);
                        tokenStatus.put("reason", "No JWT token found");
                    }
                    
                    return tokenStatus;
                })
                .defaultIfEmpty(Map.of("valid", false, "reason", "No authentication context"));
    }
}
