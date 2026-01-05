package com.lc.authorization.gateway.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lc.framework.redis.starter.customizer.RedisJacksonCustomizer;
import com.lc.framework.security.core.user.LoginUserDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.jackson.CoreJacksonModule;
import org.springframework.security.jackson.SecurityJacksonModules;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

import static tools.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/9/9 09:42
 * @version : 1.0
 */
@Configuration
public class ReactiveRedisConfiguration {

//    @Bean
//    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(
//            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory,
//            ObjectProvider<JacksonModuleProvider> customizerProvider) {
//        RedisSerializer<String> stringRedisSerializer = RedisSerializer.string();
//        RedisSerializationContext.RedisSerializationContextBuilder<String, Object> serializationContext = RedisSerializationContext
//                .newSerializationContext();
//        // key序列化方式
//        serializationContext
//                .key(stringRedisSerializer)
//                .hashKey(stringRedisSerializer)
//        ;
//
//        // value序列化方式
//        ObjectMapper objectMapper = JsonMapper.builder()
//                .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
//                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
//                .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
//                .addModules(customizerProvider.orderedStream().filter(Objects::nonNull).map(JacksonModuleProvider::getModules).flatMap(Collection::stream).toList())
//                .changeDefaultVisibility(handle -> handle.withVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY))
//                .changeDefaultPropertyInclusion(incl -> incl.withContentInclusion(JsonInclude.Include.NON_NULL).withValueInclusion(JsonInclude.Include.NON_NULL))
//                .activateDefaultTyping(BasicPolymorphicTypeValidator.builder()
//                        .allowIfBaseType("com.lc")
//                        .build(), DefaultTyping.NON_FINAL_AND_ENUMS, JsonTypeInfo.As.PROPERTY)
//                .build();
//        JacksonJsonRedisSerializer<Object> jacksonJsonRedisSerializer = new JacksonJsonRedisSerializer<>(objectMapper, Object.class);
//        serializationContext.value(jacksonJsonRedisSerializer)
//                .hashValue(jacksonJsonRedisSerializer);
//        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext.build());
//    }

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
