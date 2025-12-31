package com.lc.authorization.gateway.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.lc.framework.security.core.customizer.JacksonModuleProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson.CoreJacksonModule;
import org.springframework.security.jackson.SecurityJacksonModules;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
            ObjectProvider<JacksonModuleProvider> customizerProvider) {
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
                .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
                .addModules(customizerProvider.orderedStream().filter(Objects::nonNull).map(JacksonModuleProvider::getModules).flatMap(Collection::stream).toList())
                .changeDefaultVisibility(handle -> handle.withVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY))
                .changeDefaultPropertyInclusion(incl -> incl.withContentInclusion(JsonInclude.Include.NON_NULL).withValueInclusion(JsonInclude.Include.NON_NULL))
                .activateDefaultTyping(BasicPolymorphicTypeValidator.builder()
                        .allowIfBaseType("com.lc")
                        .build(), DefaultTyping.NON_FINAL_AND_ENUMS, JsonTypeInfo.As.PROPERTY)
                .build();
        JacksonJsonRedisSerializer<Object> jacksonJsonRedisSerializer = new JacksonJsonRedisSerializer<>(objectMapper, Object.class);
        serializationContext.value(jacksonJsonRedisSerializer)
                .hashValue(jacksonJsonRedisSerializer);
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext.build());
    }

    @Bean
    public JacksonModuleProvider redisSerializerCustomizer() {
        List<JacksonModule> appendedModules = new ArrayList<>(SecurityJacksonModules.getModules(getClass().getClassLoader()));
        appendedModules.add(new CoreJacksonModule());
        return () -> appendedModules;
    }
}
