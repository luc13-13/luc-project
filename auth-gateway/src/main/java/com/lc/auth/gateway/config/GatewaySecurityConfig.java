package com.lc.auth.gateway.config;

import com.lc.auth.gateway.handler.AuthServerAccessDeniedHandler;
import com.lc.auth.gateway.handler.AuthServerAuthenticationEntryPoint;
import com.lc.auth.gateway.security.LucAuthorizationManager;
import com.lc.auth.gateway.security.LucBearerServerAuthenticationConverter;
import com.lc.auth.gateway.security.RedisServerSecurityContextRepository;
import com.lc.framework.redis.starter.utils.RedisHelper;
import com.lc.framework.security.core.properties.SysSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.savedrequest.CookieServerRequestCache;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

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
//@RefreshScope
public class GatewaySecurityConfig {

    @Autowired
    private RedisHelper redisHelper;

    @Autowired
    private AuthServerAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AuthServerAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private SysSecurityProperties sysSecurityProperties;

    @Autowired
    private RedisServerSecurityContextRepository serverSecurityContextRepository;

    @Autowired
    private LucBearerServerAuthenticationConverter bearerServerAuthenticationConverter;

//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    @Bean
//    @ConditionalOnProperty(name = "sys.security.white-paths", matchIfMissing = false)
//    public SecurityWebFilterChain apiSecurity(ServerHttpSecurity http) {
//        String[] whiteUrl = new String[CollectionUtils.isEmpty(sysSecurityProperties.getWhitePaths()) ? 0: sysSecurityProperties.getWhitePaths().size()];
//        if (!CollectionUtils.isEmpty(sysSecurityProperties.getWhitePaths())){
//            whiteUrl = sysSecurityProperties.getWhitePaths().toArray(whiteUrl);
//            log.info("white url: {}", Arrays.asList(whiteUrl));
//            log.info("white url: {}", sysSecurityProperties.getWhitePaths());
//        }
//
//        String[] finalWhiteUrl = whiteUrl;
//        http.securityMatcher(ServerWebExchangeMatchers.pathMatchers(finalWhiteUrl))
//                .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll());
//        return http.build();
//    }

    @Bean
    public SecurityWebFilterChain defaultSecurityFilterChain(ServerHttpSecurity http,
                                                             RedirectServerAuthenticationSuccessHandler authenticationSuccessHandler,
                                                             RedirectServerAuthenticationFailureHandler authenticationFailureHandler) {
        String[] whiteUrl = new String[CollectionUtils.isEmpty(sysSecurityProperties.getWhitePaths()) ? 0: sysSecurityProperties.getWhitePaths().size()];
        if (!CollectionUtils.isEmpty(sysSecurityProperties.getWhitePaths())){
            whiteUrl = sysSecurityProperties.getWhitePaths().toArray(whiteUrl);
            log.info("white url: {}", Arrays.asList(whiteUrl));
            log.info("white url: {}", sysSecurityProperties.getWhitePaths());
        }

        String[] finalWhiteUrl = whiteUrl;
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                // 处理AuthenticationException与AccessDeniedException
                .exceptionHandling(exceptionConfig -> exceptionConfig
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                // 配置请求缓存，保存在cookie中
                .requestCache(requestCache -> requestCache.requestCache(new CookieServerRequestCache()))
                .oauth2ResourceServer(resourceServer -> resourceServer
                                .jwt(Customizer.withDefaults())
                                .bearerTokenConverter(bearerServerAuthenticationConverter)
                )
                .securityContextRepository(serverSecurityContextRepository)
                // 路径拦截设置
                .authorizeExchange(authorize -> authorize
                        // 白名单放行
                        .pathMatchers(finalWhiteUrl).permitAll()
                        // 其他所有路径都要认证
                        .anyExchange().access(new LucAuthorizationManager(ServerWebExchangeMatchers.pathMatchers(finalWhiteUrl), redisHelper))
                )
        ;
        SecurityWebFilterChain filterChain = http.build();
        filterChain.getWebFilters().subscribe(filter -> log.info("网关安全链路：{}", filter));
        return filterChain;
    }

//    public Converter<Jwt, Mono<AbstractAuthenticationToken>> converter() {
//
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
