package com.lc.framework.redis.starter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.lc.framework.redis.starter.customizer.JacksonModuleProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

import java.util.Collection;
import java.util.Objects;

/**
 * <pre>
 * 
 * <pre/>
 * 
 * @author : Lu Cheng
 * @date : 2025/8/3 12:11
 * @version : 1.0
 */
@Slf4j
@AutoConfiguration(after = RedisConnectionFactory.class, before = DataRedisAutoConfiguration.class)
public class LucRedisAutoConfiguration {
    /**
     * 创建并配置RedisTemplate实例
     * 
     * @param factory Redis连接工厂
     * @return 配置好的RedisTemplate实例
     */
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory,
            ObjectProvider<JacksonModuleProvider> customizerProvider) {
        log.info("开启redis");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // key序列化方式
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        // value序列化方式
        ObjectMapper objectMapper = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
                .changeDefaultVisibility(
                        handler -> handler.withVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY))
                .changeDefaultPropertyInclusion(incl -> incl.withContentInclusion(JsonInclude.Include.NON_NULL)
                        .withValueInclusion(JsonInclude.Include.NON_NULL))
                .addModules(customizerProvider.orderedStream().filter(Objects::nonNull)
                        .map(JacksonModuleProvider::getModules).flatMap(Collection::stream).toList())
                .activateDefaultTyping(BasicPolymorphicTypeValidator.builder()
                        // 允许所有数组类型
                        .allowIfSubTypeIsArray()
                        // 允许项目自有类型
                        .allowIfSubType("com.lc.")
                        // 允许 Spring Security 类型
                        .allowIfSubType("org.springframework.security.")
                        // 允许 Java 标准库类型
                        .allowIfSubType("java.")
                        .build(), DefaultTyping.NON_FINAL_AND_ENUMS, JsonTypeInfo.As.PROPERTY)
                .build();
        // 注入ObjectMapper配置方法
        JacksonJsonRedisSerializer<Object> jacksonJsonRedisSerializer = new JacksonJsonRedisSerializer<>(objectMapper,
                Object.class);
        redisTemplate.setValueSerializer(jacksonJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jacksonJsonRedisSerializer);
        redisTemplate.setConnectionFactory(factory);

        return redisTemplate;
    }

}
