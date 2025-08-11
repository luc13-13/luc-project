package com.lc.authorization.gateway.config;

import com.lc.authorization.gateway.security.CustomReactiveAuthenticationManager;
import com.lc.authorization.gateway.security.TokenAuthenticationConverter;
import com.lc.authorization.gateway.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Spring Security WebFlux 配置
 *
 * @author : Lu Cheng
 * @date : 2025/8/7 18:21
 * @version : 1.0
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFiltersChain(ServerHttpSecurity http) {
        return http
                // 禁用CSRF，因为我们使用token认证
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // 禁用表单登录
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                // 禁用HTTP Basic认证
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                // 配置路径访问权限
                .authorizeExchange(exchanges -> exchanges
                        // 允许健康检查和认证相关接口
                        .pathMatchers("/health", "/actuator/**", "/auth/login", "/auth/register").permitAll()
                        // 其他所有请求都需要认证
                        .anyExchange().authenticated()
                )
                .build();
    }

    /**
     * 创建认证过滤器
     */
    @Bean
    public AuthenticationWebFilter authenticationWebFilter(CustomReactiveAuthenticationManager authenticationManager,
                                                           TokenAuthenticationConverter authenticationConverter) {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(authenticationManager);
        filter.setServerAuthenticationConverter(authenticationConverter);
        return filter;
    }

    /**
     * WebClient Bean配置
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024)).build();
    }

    @Bean
    public AuthService authService(WebClient webClient) {
        return new AuthService(webClient);
    }

    @Bean
    public CustomReactiveAuthenticationManager authenticationManager(AuthService authService) {
        return new CustomReactiveAuthenticationManager(authService);
    }
}
