package com.lc.authorization.gateway.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.jackson2.SecurityJackson2Modules;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/9/9 09:42
 * @version : 1.0
 */
@Configuration
public class ReactiveRedisConfiguration {

    @Bean
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory,
            ObjectProvider<ObjectMapperCustomizer<ObjectMapper>> customizerProvider) {
        RedisSerializer<String> stringRedisSerializer = RedisSerializer.string();
        RedisSerializationContext.RedisSerializationContextBuilder<String, Object> serializationContext = RedisSerializationContext
                .newSerializationContext();
        // key序列化方式
        serializationContext
                .key(stringRedisSerializer)
                .hashKey(stringRedisSerializer)
        ;

        // value序列化方式
        ObjectMapper objectMapper = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .visibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .build();
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        customizerProvider.orderedStream().forEach(customer -> customer.customize(objectMapper));
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
        serializationContext.value(jackson2JsonRedisSerializer)
                .hashValue(jackson2JsonRedisSerializer);
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext.build());
    }

    @Bean
    public ObjectMapperCustomizer<ObjectMapper> redisSerializerCustomizer() {
        return objectMapper -> objectMapper
                .registerModules(new JavaTimeModule())
                .registerModules(SecurityJackson2Modules.getModules(getClass().getClassLoader()))
//                .registerModules(new OAuth2AuthorizationServerJackson2Module())
//                .registerModules(new OAuth2ClientJackson2Module())
                .registerModules(new CoreJackson2Module());
    }

    @FunctionalInterface
    public interface ObjectMapperCustomizer<T> {
        void customize(T t);
    }
}
