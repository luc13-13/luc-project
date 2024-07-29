package com.lc.authorization.server.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.authorization.server.security.handler.LoginTargetAuthenticationEntryPoint;
import com.lc.framework.redis.starter.customizer.ObjectMapperCustomizer;
import com.lc.framework.redis.starter.utils.RedisHelper;
import com.lc.framework.security.core.properties.SysCorsProperties;
import com.lc.framework.security.core.properties.SysSecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
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

        // 设置允许跨域的域名, 如果允许携带cookie的话,路径就不能写*号, *表示所有的域名都可以跨域访问
//        if (!CollectionUtils.isEmpty(corsProperties.getAllowedOrigins())) {
//            for (String allowedOrigin : corsProperties.getAllowedOrigins()) {
//                configuration.addAllowedOrigin(allowedOrigin);
//                configuration.addAllowedOrigin("http://127.0.0.1:8809");
//                configuration.addAllowedOrigin("http://127.0.0.1");
//                configuration.addAllowedOrigin("http://localhost");
//                configuration.addAllowedOrigin("http://192.168.1.102:5173");
//                configuration.addAllowedOrigin("http://192.168.119.1:5173");
//            }
//        }
        configuration.setAllowedOrigins(corsProperties.getAllowedOrigins());
        // 设置跨域访问可以携带cookie
        configuration.setAllowCredentials(corsProperties.isAllowCredentials());
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
