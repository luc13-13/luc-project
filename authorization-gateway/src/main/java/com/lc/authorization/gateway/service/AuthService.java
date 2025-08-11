package com.lc.authorization.gateway.service;

import com.lc.authorization.gateway.config.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * 认证服务客户端 - 基于Spring Security集成
 */
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final WebClient webClient;
    
    @Value("${auth.service.url:http://127.0.0.1:8889}")
    private String authServiceUrl;
    
    public AuthService(WebClient webClient) {
        this.webClient = webClient;
    }
    
    /**
     * 验证token有效性
     * @param token 认证token
     * @return 认证结果
     */
    public Mono<AuthResponse> validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return Mono.just(new AuthResponse(false, false, "Token不能为空", null));
        }
        
        logger.debug("验证token: {}", token);
        
        return webClient.post()
                .uri(authServiceUrl + "/auth/validate")
                .header("luc-auth-token", token)
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .timeout(Duration.ofSeconds(5))
                .doOnSuccess(response -> logger.debug("Token验证结果: {}", response))
                .doOnError(error -> logger.error("Token验证失败: {}", error.getMessage()))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode().is4xxClientError()) {
                        return Mono.just(new AuthResponse(false, true, "Token无效或已过期", null));
                    }
                    return Mono.just(new AuthResponse(false, false, "认证服务不可用", null));
                })
                .onErrorReturn(new AuthResponse(false, false, "认证服务异常", null));
    }
    
    /**
     * 检查token是否有效且未过期
     * @param token 认证token
     * @return 是否有效
     */
    public Mono<Boolean> isTokenValid(String token) {
        return validateToken(token)
                .map(response -> response.valid() && !response.expired())
                .onErrorReturn(false);
    }
    
    /**
     * 获取token中的用户信息
     * @param token 认证token
     * @return 用户信息
     */
    public Mono<AuthResponse> getUserInfo(String token) {
        return validateToken(token)
                .filter(response -> response.valid() && !response.expired());
    }
}
