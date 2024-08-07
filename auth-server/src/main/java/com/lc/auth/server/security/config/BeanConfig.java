package com.lc.auth.server.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.auth.server.security.handler.LoginTargetAuthenticationEntryPoint;
import com.lc.framework.redis.starter.customizer.ObjectMapperCustomizer;
import com.lc.framework.redis.starter.utils.RedisHelper;
import com.lc.framework.security.core.properties.SysCorsProperties;
import com.lc.framework.security.core.properties.SysSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.client.jackson2.OAuth2ClientJackson2Module;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2023/10/21 11:17
 * @version : 1.0
 */
@Slf4j
@Configuration
public class BeanConfig {

    private final SysSecurityProperties sysSecurityProperties;

    public BeanConfig(SysSecurityProperties sysSecurityProperties) {
        this.sysSecurityProperties = sysSecurityProperties;
    }

    @Bean
    public LoginTargetAuthenticationEntryPoint loginTargetAuthenticationEntryPoint(RedisHelper redisHelper) {
        return new LoginTargetAuthenticationEntryPoint(sysSecurityProperties.getLoginPage(),
                sysSecurityProperties.getDeviceActiveUrl(),
                redisHelper);
    }

    /**
     * 配置密码解析器，使用BCrypt的方式对密码进行加密和验证
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(value = "sys.cors.enabled")
    public CorsConfigurationSource configurationSource(SysCorsProperties corsProperties) {
        // 初始化cors配置对象
        CorsConfiguration configuration = new CorsConfiguration();
        // 设置跨域访问可以携带cookie
        configuration.setAllowCredentials(corsProperties.isAllowCredentials());
        // allowCredentials=true时，origin不可以用*匹配，需要设置originPattern
        configuration.setAllowedOriginPatterns(corsProperties.getAllowedOriginPatterns());
        // 允许所有的请求方法 ==> GET POST PUT Delete
        configuration.setAllowedMethods(corsProperties.getAllowedMethods());
        // 允许携带任何头信息
        configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
        // 初始化cors配置源对象
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        // 给配置源对象设置过滤的参数
        // 参数一: 过滤的路径 == > 所有的路径都要求校验是否跨域
        // 参数二: 配置类
        configurationSource.registerCorsConfiguration("/**", configuration);
        return configurationSource;
    }

    // 向redisTemplate中添加SpringSecurity相关类的序列化支持
    @Bean
    public ObjectMapperCustomizer<ObjectMapper> redisSerializerCustomizer() {
        return objectMapper -> objectMapper
                .registerModules(SecurityJackson2Modules.getModules(getClass().getClassLoader()))
                .registerModules(new OAuth2ClientJackson2Module())
                .registerModules(new CoreJackson2Module())
                ;
    }
}
