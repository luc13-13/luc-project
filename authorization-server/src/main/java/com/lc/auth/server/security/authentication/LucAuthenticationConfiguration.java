package com.lc.auth.server.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.auth.server.redis.customizer.ObjectMapperCustomizer;
import com.lc.auth.server.security.authentication.extension.MultiTypeAuthenticationFilter;
import com.lc.auth.server.security.authentication.extension.RedisSecurityContextRepository;
import com.lc.auth.server.security.authentication.extension.sms.SmsAuthenticationConverter;
import com.lc.auth.server.security.authentication.extension.sms.SmsAuthenticationProvider;
import com.lc.auth.server.security.authentication.extension.sms.SmsCodeService;
import com.lc.auth.server.security.core.LoginUserDetailService;
import com.lc.auth.server.security.properties.LoginProperties;
import com.lc.auth.server.security.properties.SysCorsProperties;
import com.lc.auth.server.security.properties.SysSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.client.jackson2.OAuth2ClientJackson2Module;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.List;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/3 17:16
 * @version : 1.0
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties({SysSecurityProperties.class, LoginProperties.class, SysCorsProperties.class})
public class LucAuthenticationConfiguration {

    /**
     * 注册客户端仓库
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder,
                                                                 JdbcTemplate jdbcTemplate) {

        JdbcRegisteredClientRepository repository = new JdbcRegisteredClientRepository(jdbcTemplate);
        // 网关客户端
        RegisteredClient gatewayClient = RegisteredClient.withId("gateway-client")
                .clientId("gateway-client")
                .clientSecret(passwordEncoder.encode("secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("http://127.0.0.1:8809/login/oauth2/code/gateway-client")
                .redirectUri("http://localhost:8809/login/oauth2/code/gateway-client")
                .redirectUri("http://127.0.0.1:8809/swagger-ui/oauth2-redirect.html")
                .redirectUri("http://localhost:8809/swagger-ui/oauth2-redirect.html")
                .postLogoutRedirectUri("http://127.0.0.1:8809/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope("read")
                .scope("write")
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(2))
                        .refreshTokenTimeToLive(Duration.ofDays(7))
                        .build())
                .build();

        // API文档客户端
        RegisteredClient apiDocClient = RegisteredClient.withId("openapi-client")
                .clientId("openapi-client")
                .clientSecret(passwordEncoder.encode("secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://127.0.0.1:8889/swagger-ui/oauth2-redirect.html")
                .redirectUri("http://localhost:8889/swagger-ui/oauth2-redirect.html")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope("read")
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(2))
                        .refreshTokenTimeToLive(Duration.ofDays(7))
                        .build())
                .build();

        repository.save(gatewayClient);
        repository.save(apiDocClient);
        return repository;
    }

    /**
     * 记录客户端授权信息
     * @param jdbcTemplate 数据库操作类
     * @param repository 客户端查询仓库
     */
    @Bean
    public OAuth2AuthorizationService oauth2AuthorizationService(JdbcTemplate jdbcTemplate,
                                                                 RegisteredClientRepository repository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, repository);
    }

    /**
     * 记录客户端授权确认信息
     * @param jdbcTemplate 数据库操作类
     * @param repository 客户端查询仓库
     */
    @Bean
    public OAuth2AuthorizationConsentService oauth2AuthorizationConsentService(JdbcTemplate jdbcTemplate,
                                                                        RegisteredClientRepository repository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, repository);
    }

    @Bean
    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnProperty(prefix = SysSecurityProperties.PREFIX, name = "enable-redis", havingValue = "true")
    public SecurityContextRepository redisSecurityContextRepository(RedisTemplate<String, Object> redisTemplate,
                                                                    SysSecurityProperties sysSecurityProperties) {
        log.info("开启RedisSecurityContextRepository");
        return new RedisSecurityContextRepository(redisTemplate, sysSecurityProperties.getTokenTimeToLive().toSeconds());
    }

    /**
     * 开启拓展认证方式
     *
     */
    @Bean
    @ConditionalOnBean(SmsAuthenticationConverter.class)
    public MultiTypeAuthenticationFilter multiTypeAuthenticationFilter(List<AuthenticationConverter> converters) {
        return  new MultiTypeAuthenticationFilter(converters);
    }

    @Bean
    @ConditionalOnMissingBean(SmsCodeService.class)
    @ConditionalOnProperty(prefix = LoginProperties.PREFIX, name = "enable-sms-login", havingValue = "true")
    public SmsCodeService smsCodeService(RedisTemplate<String, Object> redisTemplate) {
        return new SmsCodeService(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(SmsAuthenticationProvider.class)
    @ConditionalOnProperty(prefix = LoginProperties.PREFIX, name = "enable-sms-login", havingValue = "true")
    public AuthenticationProvider smsAuthenticationProvider(SmsCodeService smsCodeService,
                                                            LoginUserDetailService loginUserDetailService) {
        return new SmsAuthenticationProvider(smsCodeService, loginUserDetailService);
    }

    /**
     * 向redisTemplate中添加SpringSecurity相关类的序列化支持
     */
    @Bean
    public ObjectMapperCustomizer<ObjectMapper> redisSerializerCustomizer() {
        return objectMapper -> objectMapper
                .registerModules(SecurityJackson2Modules.getModules(getClass().getClassLoader()))
                .registerModules(new OAuth2ClientJackson2Module())
                .registerModules(new CoreJackson2Module());
    }

    @Bean
    @ConditionalOnProperty(prefix = SysCorsProperties.PREFIX, value = "enabled", havingValue = "true")
    public CorsConfigurationSource corsConfigurationSource(SysCorsProperties corsProperties) {
        // 初始化cors配置对象
        CorsConfiguration configuration = new CorsConfiguration();
        // 设置跨域访问可以携带cookie
        configuration.setAllowCredentials(corsProperties.isAllowCredentials());
        // allowCredentials=true时，origin不可以用*匹配，需要设置originPattern
        configuration.setAllowedOriginPatterns(corsProperties.getAllowedOriginPatterns());
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
}
