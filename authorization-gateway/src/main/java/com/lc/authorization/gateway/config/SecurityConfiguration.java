package com.lc.authorization.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Spring Security WebFlux 配置
 * 使用标准的 OAuth2 Resource Server JWT 认证
 *
 * @author : Lu Cheng
 * @date : 2025/8/15
 * @version : 1.0
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFiltersChain(ServerHttpSecurity http,
                                                          GatewaySecurityProperties securityProperties) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                // 使用标准的 OAuth2 Resource Server JWT 认证
                .oauth2ResourceServer(oAuth2ResourceServer ->
                        oAuth2ResourceServer
                                .jwt(Customizer.withDefaults())
                )
                .authorizeExchange(exchanges -> exchanges
                        // 白名单路径
                        .pathMatchers(
                                securityProperties.getWhitePaths().toArray(new String[0])  // 认证服务相关路径
                        ).permitAll()
                        // 其他所有请求都需要认证
                        .anyExchange().authenticated()
                )
                .build();
    }
}
