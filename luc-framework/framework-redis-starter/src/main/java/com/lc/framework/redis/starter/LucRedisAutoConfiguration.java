package com.lc.framework.redis.starter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.lc.framework.redis.starter.customizer.RedisJacksonCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.Objects;

/**
 * <pre>
 * Redis 自动配置类
 * </pre>
 * 
 * @author Lu Cheng
 * @date 2025/8/3 12:11
 * @version 1.0
 */
@Slf4j
@AutoConfiguration(after = {RedisConnectionFactory.class, ReactiveRedisConnectionFactory.class}, before = DataRedisAutoConfiguration.class)
public class LucRedisAutoConfiguration {

    /**
     * 创建并配置RedisTemplate实例
     * 
     * @param factory Redis连接工厂
     * @return 配置好的RedisTemplate实例
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory,
            ObjectProvider<RedisJacksonCustomizer> customizerProvider) {
        log.info("开启redis, servlet");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // key序列化方式
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        // value序列化方式
        JsonMapper.Builder jsonMapperBuilder = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .changeDefaultVisibility(
                        handler -> handler.withVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY))
                .changeDefaultPropertyInclusion(incl -> incl.withContentInclusion(JsonInclude.Include.NON_NULL)
                        .withValueInclusion(JsonInclude.Include.NON_NULL));

        this.customize(customizerProvider, jsonMapperBuilder);
        // 构建 JsonMapper
        JsonMapper jsonMapper = jsonMapperBuilder.build();
        JacksonJsonRedisSerializer<Object> jacksonJsonRedisSerializer = new JacksonJsonRedisSerializer<>(jsonMapper,
                Object.class);
        redisTemplate.setValueSerializer(jacksonJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jacksonJsonRedisSerializer);
        redisTemplate.setConnectionFactory(factory);

        return redisTemplate;
    }

    @Bean
    @Primary
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory,
            ObjectProvider<RedisJacksonCustomizer> customizerProvider) {
        log.info("开启redis, reactive");
        RedisSerializer<String> stringRedisSerializer = RedisSerializer.string();
        RedisSerializationContext.RedisSerializationContextBuilder<String, Object> serializationContext = RedisSerializationContext
                .newSerializationContext();
        // key序列化方式
        serializationContext
                .key(stringRedisSerializer)
                .hashKey(stringRedisSerializer)
        ;

        // value序列化方式
        JsonMapper.Builder jsonMapperBuilder = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
                .changeDefaultVisibility(handle -> handle.withVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY))
                .changeDefaultPropertyInclusion(incl -> incl.withContentInclusion(JsonInclude.Include.NON_NULL).withValueInclusion(JsonInclude.Include.NON_NULL));
        this.customize(customizerProvider, jsonMapperBuilder);
        JacksonJsonRedisSerializer<Object> jacksonJsonRedisSerializer = new JacksonJsonRedisSerializer<>(jsonMapperBuilder.build(), Object.class);
        serializationContext.value(jacksonJsonRedisSerializer)
                .hashValue(jacksonJsonRedisSerializer);
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext.build());
    }

    private void customize(ObjectProvider<RedisJacksonCustomizer> customizerProvider, JsonMapper.Builder jsonMapperBuilder) {
        customizerProvider.orderedStream().filter(Objects::nonNull)
                .forEach(customizer -> customizer.customize(jsonMapperBuilder));

    }

}
