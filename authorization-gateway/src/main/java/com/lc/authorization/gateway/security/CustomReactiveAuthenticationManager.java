package com.lc.authorization.gateway.security;

import com.lc.authorization.gateway.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * 自定义响应式认证管理器
 * 集成认证服务进行token验证
 */
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomReactiveAuthenticationManager.class);
    
    private final AuthService authService;
    
    public CustomReactiveAuthenticationManager(AuthService authService) {
        this.authService = authService;
    }
    
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        
        logger.debug("开始认证token: {}", token);
        
        return authService.validateToken(token)
                .flatMap(authResponse -> {
                    if (!authResponse.valid()) {
                        logger.warn("Token验证失败: {}", authResponse.message());
                        return Mono.empty(); // 返回空表示认证失败
                    }

                    if (authResponse.expired()) {
                        logger.warn("Token已过期");
                        return Mono.empty(); // 返回空表示认证失败
                    }

                    logger.debug("Token验证成功，用户: {}", authResponse.username());

                    // 创建认证成功的Authentication对象
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    authResponse.username(),
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                            );

                    // 将用户信息存储在details中，供后续使用
                    authToken.setDetails(authResponse);

                    return Mono.just(authToken);
                })
                .cast(Authentication.class)
                .doOnError(error -> logger.error("认证过程中发生错误: {}", error.getMessage()))
                .onErrorResume(error -> Mono.empty()); // 发生错误时返回空，表示认证失败
    }
}
