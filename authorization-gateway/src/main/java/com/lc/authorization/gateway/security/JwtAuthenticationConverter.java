package com.lc.authorization.gateway.security;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * JWT 认证转换器
 * 将 JWT token 转换为 Spring Security 的 Authentication 对象
 *
 * @author Lu Cheng
 * @date 2025/8/15
 */
@Slf4j
@Component
public class JwtAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    @Override
    public Mono<AbstractAuthenticationToken> convert(@NotNull Jwt jwt) {
        // 从 JWT 中提取权限信息
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        log.info("authorities: {}", authorities);
        // 创建认证 token
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(jwt, authorities);
        log.info("authenticationToken: {}", authenticationToken);
        return Mono.just(authenticationToken);
    }

    /**
     * 从 JWT 中提取权限信息
     */
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        if (jwt == null) {
            return  Collections.emptyList();
        }
        // 从 JWT 的 scope 声明中提取权限
        Object scopeClaim = jwt.getClaim("scope");
        if (scopeClaim instanceof String) {
            String scopes = (String) scopeClaim;
            return Stream.of(scopes.split(" "))
                    .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope))
                    .collect(Collectors.toList());
        }
        
        // 从 JWT 的 authorities 声明中提取权限（如果有的话）
        Object authoritiesClaim = jwt.getClaim("authorities");
        if (authoritiesClaim instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> authorities = (List<String>) authoritiesClaim;
            return authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        
        // 默认返回基础权限
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
