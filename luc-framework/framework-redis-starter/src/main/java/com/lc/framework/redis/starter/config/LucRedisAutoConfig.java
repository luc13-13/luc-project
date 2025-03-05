package com.lc.framework.redis.starter.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.lc.framework.redis.starter.customizer.ObjectMapperCustomizer;
import com.lc.framework.redis.starter.utils.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <pre>
 *     redis自动装配类，引入framework-luc-redis-starter后，根据Spring加载配置类的设计
 *     {@link AutoConfigurationImportSelector#selectImports}，读取"META-INF/spring-autoconfigure-metadata.properties"
 *     文件中设置的过滤条件，加载{@value SpringFactoriesLoader#FACTORIES_RESOURCE_LOCATION}中声明的自动装配类，{@see AutoConfigurationMetadataLoader#PATH}
 * </pre>
 * @Author : Lu Cheng
 * @Date : 2022/11/18 20:21
 * @Version : 1.0
 */
@AutoConfiguration(after = RedisConnectionFactory.class)
public class LucRedisAutoConfig {

    @Value(value = "${spring.application.name}")
    private String REDIS_KEY_PREFIX;

    @Autowired(required = false)
    private List<ObjectMapperCustomizer<ObjectMapper>> objectMapperCustomizers;


    @Bean("jsonRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        template.setConnectionFactory(factory);
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);

//         Jackson valueSerializer
        ObjectMapper om = JsonMapper.builder().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).build();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(om.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 注入定制化的配置
        if (!CollectionUtils.isEmpty(objectMapperCustomizers)) {
            for (ObjectMapperCustomizer<ObjectMapper> customizer : objectMapperCustomizers) {
                customizer.customize(om);
            }
        }
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(om, Object.class);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean("redisHelper")
    public RedisHelper redisUtils(@Qualifier("jsonRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        return new RedisHelper(redisTemplate, 3600, REDIS_KEY_PREFIX);
    }

    @Bean
    @ConditionalOnMissingBean(ReactiveRedisTemplate.class)
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(LettuceConnectionFactory factory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Object> builder = RedisSerializationContext
                .newSerializationContext(keySerializer);
        RedisSerializationContext<String, Object> context = builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }

//    /**
//     * 由于SimpleSession中包含transient字段，导致无法正常完成json序列化，处理该类对象时不
//     *
//     * @author Lu Cheng
//     * @create 2023/9/22
//     */
//    @Bean("sessionRedisTemplate")
//    public RedisTemplate<String, Object> sessionRedisTemplate(RedisConnectionFactory factory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        StringRedisSerializer keySerializer = new StringRedisSerializer();
//        template.setConnectionFactory(factory);
//        template.setKeySerializer(keySerializer);
//        template.setHashKeySerializer(keySerializer);
//        return template;
//    }
//
//    @Bean("sessionRedisHelper")
//    public RedisHelper sessionRedisUtils(@Qualifier("sessionRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
//        return new RedisHelper(redisTemplate, 3600, REDIS_KEY_PREFIX);
//    }
}
