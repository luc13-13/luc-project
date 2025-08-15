package com.lc.authorization.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

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
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .oauth2ResourceServer(rs -> rs.jwt(Customizer.withDefaults()))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/health", "/actuator/**", "/login", "/register", "/sms/code", "/sms/login").permitAll()
                        .anyExchange().authenticated()
                )
                .build();
    }
}
