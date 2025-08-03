package com.lc.auth.server.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.lc.auth.server.redis.customizer.ObjectMapperCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/3 12:11
 * @version : 1.0
 */
@AutoConfiguration(after = RedisConnectionFactory.class, before = RedisAutoConfiguration.class)
public class LucRedisAutoConfiguration {
    /**
     * 创建并配置RedisTemplate实例
     * @param factory Redis连接工厂
     * @return 配置好的RedisTemplate实例
     */
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory,
                                                       ObjectProvider<ObjectMapperCustomizer<ObjectMapper>> customizerProvider) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // key序列化方式
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        // value序列化方式
        ObjectMapper objectMapper = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .visibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .build();
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
        customizerProvider.orderedStream().forEach(customer -> customer.customize(objectMapper));
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(factory);

        return redisTemplate;
    }


}
