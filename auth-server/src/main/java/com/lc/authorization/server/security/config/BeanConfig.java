package com.lc.authorization.server.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.authorization.server.property.SysSecurityProperties;
import com.lc.authorization.server.security.filter.TokenHeaderWriter;
import com.lc.authorization.server.security.handler.LoginTargetAuthenticationEntryPoint;
import com.lc.framework.redis.starter.customizer.ObjectMapperCustomizer;
import com.lc.framework.redis.starter.utils.RedisHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2023/10/21 11:17
 * @version : 1.0
 */
@Configuration
public class BeanConfig {

    @Value(value = "${spring.application.name}")
    private String REDIS_KEY_PREFIX;


    private final SysSecurityProperties sysSecurityProperties;

    public BeanConfig(SysSecurityProperties sysSecurityProperties) {
        this.sysSecurityProperties = sysSecurityProperties;
    }


    @Bean
    public TokenHeaderWriter tokenHeaderWriter() {
        return new TokenHeaderWriter();
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

//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//        UserDetails userDetails = User
//                .withUsername("admin")
//                .password(encoder.encode("123456"))
//                .roles("USER", "ADMIN")
//                .authorities("all")
//                .build();
//
//        return new LoginUserDetailServiceImpl(userDetails);
//    }

//    @Bean
//    public RedisHelper redisHelper(RedisTemplate<String, Object> redisTemplate) {
//        return new RedisHelper(redisTemplate, 3600, REDIS_KEY_PREFIX);
//    }

//    @Bean
//    public RedisTemplate<String, Object> securityRedisTemplate(LettuceConnectionFactory factory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        StringRedisSerializer keySerializer = new StringRedisSerializer();
//        GenericFastJsonRedisSerializer valueSerializer = new GenericFastJsonRedisSerializer();
//        template.setConnectionFactory(factory);
//        template.setKeySerializer(keySerializer);
//        template.setHashKeySerializer(keySerializer);
//
////         Jackson valueSerializer
//        ObjectMapper om = JsonMapper.builder().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
//                .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
//                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).build();
//        // 注入Spring Security中的class, 解决UsernamePasswordAuthenticationToken缺少无参构造函数无法反序列化的问题
//        om.registerModules(SecurityJackson2Modules.getModules(getClass().getClassLoader()));
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.activateDefaultTyping(om.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(om, Object.class);
//        // 将value的序列化方式都改为jackson2Json， 否则无法反序列化出Token中的Principal
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//        return template;
//    }

    // 向redisTemplate中添加SpringSecurity相关类的序列化支持
    @Bean
    public ObjectMapperCustomizer<ObjectMapper> redisSerializerCustomizer() {
        return objectMapper -> objectMapper.registerModules(SecurityJackson2Modules.getModules(getClass().getClassLoader()));
    }
}
