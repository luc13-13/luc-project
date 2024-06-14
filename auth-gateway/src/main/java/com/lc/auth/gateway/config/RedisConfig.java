package com.lc.auth.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.framework.redis.starter.customizer.ObjectMapperCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jackson2.SecurityJackson2Modules;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/11 15:30
 */
@Configuration
public class RedisConfig {
    @Bean
    public ObjectMapperCustomizer<ObjectMapper> redisSerializerCustomizer() {
        return objectMapper -> objectMapper.registerModules(SecurityJackson2Modules.getModules(getClass().getClassLoader()));
    }
}
