package com.lc.authorization.gateway.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lc.authorization.gateway.security.RedisServerSecurityContextRepository;
import com.lc.framework.redis.starter.customizer.RedisJacksonCustomizer;
import com.lc.framework.security.core.user.LoginUserDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.jackson.CoreJacksonModule;
import org.springframework.security.jackson.SecurityJacksonModules;
import org.springframework.security.web.server.SecurityWebFilterChain;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

import static tools.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;

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
                                                          GatewaySecurityProperties securityProperties,
                                                          ReactiveRedisTemplate<String, Object> reactiveRedisTemplate) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .securityContextRepository(new RedisServerSecurityContextRepository(reactiveRedisTemplate))
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

    /**
     * 向redisTemplate中添加SpringSecurity相关类的序列化支持
     */
    @Bean
    public RedisJacksonCustomizer redisSerializerCustomizer() {
        BasicPolymorphicTypeValidator.Builder typeValidatorBuilder =  BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(SecurityContextImpl.class)
                .allowIfSubType(LoginUserDetail.class);
        return builder -> builder
                .addModules(new CoreJacksonModule())
                .addModules(SecurityJacksonModules.getModules(getClass().getClassLoader(), typeValidatorBuilder))
                .activateDefaultTyping(typeValidatorBuilder.build(), DefaultTyping.NON_FINAL_AND_ENUMS, JsonTypeInfo.As.PROPERTY)
                .enable(ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                .disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
    }
}
