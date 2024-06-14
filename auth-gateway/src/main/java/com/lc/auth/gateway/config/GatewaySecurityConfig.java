package com.lc.auth.gateway.config;

import com.lc.auth.gateway.config.properties.LucGatewayProperties;
import com.lc.auth.gateway.handler.AuthServerAccessDeniedHandler;
import com.lc.auth.gateway.handler.AuthServerAuthenticationEntryPoint;
import com.lc.auth.gateway.security.LucAuthorizationManager;
import com.lc.auth.gateway.security.RedisServerSecurityContextRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.savedrequest.CookieServerRequestCache;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/5 14:17
 */
@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@EnableConfigurationProperties({LucGatewayProperties.class})
@RefreshScope
public class GatewaySecurityConfig {

    @Autowired
    private LucAuthorizationManager lucAuthorizationManager;

    @Autowired
    private AuthServerAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AuthServerAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private LucGatewayProperties gatewayProperties;

    @Autowired
    private RedisServerSecurityContextRepository serverSecurityContextRepository;

    @Bean
    @RefreshScope
    public SecurityWebFilterChain defaultSecurityFilterChain(ServerHttpSecurity http,
                                                             RedirectServerAuthenticationSuccessHandler authenticationSuccessHandler,
                                                             RedirectServerAuthenticationFailureHandler authenticationFailureHandler) {
        String[] whiteUrl = new String[CollectionUtils.isEmpty(gatewayProperties.getWhiteUrl()) ? 0: gatewayProperties.getWhiteUrl().size()];
        if (!CollectionUtils.isEmpty(gatewayProperties.getWhiteUrl())){
            whiteUrl = gatewayProperties.getWhiteUrl().toArray(whiteUrl);
            log.info("white url: {}", Arrays.asList(whiteUrl));
            log.info("white url: {}", gatewayProperties.getWhiteUrl());
        }

        String[] finalWhiteUrl = whiteUrl;
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                // 路径拦截设置
                .authorizeExchange(authorize -> authorize
                        // 白名单放行
                        .pathMatchers(finalWhiteUrl).permitAll()
                        // 其他所有路径都要认证
                        .anyExchange().access(lucAuthorizationManager)
                )
                // 处理AuthenticationException与AccessDeniedException
                .exceptionHandling(exceptionConfig -> exceptionConfig
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                // 配置请求缓存，保存在cookie中
                .requestCache(requestCache -> requestCache.requestCache(new CookieServerRequestCache()))
                .oauth2Login(
//                        Customizer.withDefaults()
                        oAuth2LoginSpec -> oAuth2LoginSpec
                        .authenticationSuccessHandler(authenticationSuccessHandler)
                        .authenticationFailureHandler(authenticationFailureHandler)
                )
                .oauth2ResourceServer(resourceServer -> resourceServer
//                        .authenticationEntryPoint(authenticationEntryPoint)
                        .jwt(Customizer.withDefaults())
                )
                .securityContextRepository(serverSecurityContextRepository)
        ;
        SecurityWebFilterChain filterChain = http.build();
        filterChain.getWebFilters().subscribe(filter -> log.info("网关安全链路：{}", filter));
        return filterChain;
    }

//    @Bean
//    public ReactiveJwtDecoder jwtDecoder() {
//        return
//    }

    @Bean
    public RedirectServerAuthenticationSuccessHandler authenticationSuccessHandler() {
        RedirectServerAuthenticationSuccessHandler handler = new RedirectServerAuthenticationSuccessHandler();
        handler.setRequestCache(new CookieServerRequestCache());
        return handler;
    }

    @Bean
    public RedirectServerAuthenticationFailureHandler authenticationFailureHandler() {
        return new RedirectServerAuthenticationFailureHandler("/login?error");
    }

//    public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
//
//    }
//
//    public MapReactiveUserDetailsService mapReactiveUserDetailsService() {
//
//    }
}
