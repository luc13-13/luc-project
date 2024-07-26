package com.lc.authorization.server.security.config;


import com.lc.authorization.server.property.SysTokenProperties;
import com.lc.authorization.server.security.customizer.AuthenticationProviderCustomizer;
import com.lc.authorization.server.security.customizer.OAuth2TokenEndpointCustomizer;
import com.lc.authorization.server.security.extension.DaoAuthenticationProvider;
import com.lc.authorization.server.security.filter.TokenHeaderWriter;
import com.lc.authorization.server.security.handler.*;
import com.lc.authorization.server.security.repository.RedisSecurityContextRepository;
import com.lc.authorization.server.utils.SecurityUtils;
import com.lc.framework.redis.starter.utils.RedisHelper;
import com.lc.framework.security.core.properties.SysSecurityProperties;
import com.lc.framework.security.service.LoginUserDetailService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.filter.CorsFilter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.*;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.*;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-10-19 09:29
 */
@Slf4j
@Configuration
@AutoConfigureAfter(BeanConfig.class)
@EnableConfigurationProperties({SysSecurityProperties.class, SysTokenProperties.class})
public class AuthorizationConfig {

    @Autowired
    private RedisHelper redisHelper;

    @Autowired
    private CorsFilter corsFilter;

    @Lazy
    @Autowired
    private LoginUserDetailService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OAuth2TokenSuccessHandler oAuth2TokenSuccessHandler;


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      OAuth2AuthorizationConsentService authorizationConsentService,
                                                                      OAuth2AuthorizationService authorizationService,
                                                                      RedisSecurityContextRepository redisSecurityContextRepository,
                                                                      LoginTargetAuthenticationEntryPoint loginTargetAuthenticationEntryPoint,
                                                                      JwtDecoder jwtDecoder)
            throws Exception {


        // 自定义放行路径
        SecurityUtils.applyDefaultSecurity(http, new AntPathRequestMatcher[]{
                AntPathRequestMatcher.antMatcher("/login"),
                AntPathRequestMatcher.antMatcher("/css/**"),
                AntPathRequestMatcher.antMatcher("/error"),
                AntPathRequestMatcher.antMatcher("/*/api-docs/**"),
                AntPathRequestMatcher.antMatcher("/*/*.html"),
                AntPathRequestMatcher.antMatcher("/favicon.ico"),
                AntPathRequestMatcher.antMatcher("/v3/api-docs/default"),
                AntPathRequestMatcher.antMatcher("/user/detail")});
        http
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 自定义的客户端认证方法, 默认对/oauth2/token(获取token), /oauth2/revoke(注销token), /oauth2/introspect(校验token有效性), /oauth2/device_authorization接口进行拦截， 均为POST方法
                .clientAuthentication(
                        Customizer.withDefaults()
//                        clientAuthentication -> clientAuthentication.authenticationConverter(new OAuth2PasswordAuthenticationConverter())
                )
                // 注入自定义的授权方式Converter, 所有经过/oauth2/token接口的请求都经过OAuth2TokenEndpointFilter过滤器
                // 该过滤器要求provider提供OAuth2AccessTokenAuthenticationToken
                // 获取token必须在登陆之后， 因为OAuth2TokenEndPointFilter在鉴权过滤器之后，获取不到权限则直接报错
                .tokenEndpoint(new OAuth2TokenEndpointCustomizer(oAuth2TokenSuccessHandler, null))
                // 设置自定义的授权码确认页面地址, 拦截/oauth2/authorize, 对应OAuth2AuthorizationEndpointFilter
//                .authorizationEndpoint(
//                        authorizationEndpoint -> authorizationEndpoint
//                                // 注入自定义的授权成功handler，保存code与jsessionid的关系
//                                .authorizationResponseHandler(oAuth2AuthorizationSuccessHandler)
//                )
//                .authorizationConsentService(authorizationConsentService)
                // Enable OpenID Connect 1.0
                .oidc(Customizer.withDefaults())
                .authorizationService(authorizationService)
                .authorizationServerMetadataEndpoint(customizer -> customizer
                        .authorizationServerMetadataCustomizer(meta -> meta.grantTypes(grantType -> grantType.addAll(List.of("password", "sms", "gitee"))))
                )
        ;
        http
                .rememberMe(rememberMeConfig -> rememberMeConfig
                        .tokenRepository(new JdbcTokenRepositoryImpl())
                        .alwaysRemember(false))
                // 添加默认请求头
                .headers(conf -> conf.addHeaderWriter(new TokenHeaderWriter()))
                // 配置跨域
                .addFilter(corsFilter)
                // Accept access tokens for User Info and/or Client Registration
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder)))
                // 提供表单登录
//                .formLogin(formLoginConfig -> formLoginConfig
//                        // 避免在Authentication序列化时引入HttpServlet, 导致网关反序列化失败问题
//                        .authenticationDetailsSource(new ServerAuthenticationDetailsSource())
//                        .loginPage(sysSecurityProperties.getLoginPage())
//                        .loginProcessingUrl(sysSecurityProperties.getLoginApi())
//                        .failureHandler(loginFailureHandler)
//                        .successHandler(loginSuccessHandler))
                // 提供oauth2登录
//                .oauth2Login(Customizer.withDefaults())
        ;

        http
                .csrf(AbstractHttpConfigurer::disable)
                // Redirect to the login page when not authenticated from the authorization endpoint
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                loginTargetAuthenticationEntryPoint,
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML, MediaType.APPLICATION_JSON)))
                // 注入redis存储SecurityContext, 防止分布式情境下不同node之间数据不同步的问题
                .securityContext(contextConfig -> contextConfig.securityContextRepository(redisSecurityContextRepository))
        ;
        DefaultSecurityFilterChain filterChain = http.build();
        // 注入自定义的Provider, 为了获取http中的属性，需要在进行http.build()之后进行
        new AuthenticationProviderCustomizer().customize(http);
        log.info("授权服务器过滤链路配置：{}", filterChain.getFilters());
        return filterChain;
    }

    // DaoAuthenticationProvider需要通过builder注入
    @Autowired
    public void authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
        // 注入自定义的UserDetailService
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService, passwordEncoder);
        builder.authenticationProvider(daoAuthenticationProvider);
    }

    /**
     * `
     * 配置客户端Repository
     *
     * @param jdbcTemplate db 数据源信息
     * @return 基于数据库的repository
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate,
                                                                 PasswordEncoder passwordEncoder) {
        // 基于db存储客户端，可以提前向db中插入客户端信息
        RegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);

        RegisteredClient gatewayClient = RegisteredClient
                .withId("gateway-client")
                .clientId("gateway-client")
                .clientName("gateway-client")
                .clientIdIssuedAt(Instant.now())
                .scopes(scope -> scope.addAll(List.of("read", "write", "openid", "profile", "all")))
                .authorizationGrantTypes(types -> types.addAll((List.of(AUTHORIZATION_CODE, REFRESH_TOKEN, CLIENT_CREDENTIALS, PASSWORD, JWT_BEARER))))
                .clientAuthenticationMethods(method -> method.addAll(List.of(CLIENT_SECRET_BASIC, CLIENT_SECRET_POST, CLIENT_SECRET_JWT, PRIVATE_KEY_JWT)))
                .redirectUris(url -> url.addAll(List.of("http://127.0.0.1:8809/login/oauth2/code/gateway-client", "http://localhost:8809/login/oauth2/code/gateway-client")))
                .clientSecret(passwordEncoder.encode("secret"))
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.of(60, ChronoUnit.SECONDS))
                        .refreshTokenTimeToLive(Duration.of(30, ChronoUnit.DAYS))
                        .build())
                .build();

        if (Objects.isNull(registeredClientRepository.findByClientId(gatewayClient.getClientId()))) {
            registeredClientRepository.save(gatewayClient);
        }
        return registeredClientRepository;
    }

    /**
     * 配置基于db的授权确认管理服务
     *
     * @param jdbcTemplate               db数据源信息
     * @param registeredClientRepository 客户端repository
     * @return JdbcOAuth2AuthorizationConsentService
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
                                                                         RegisteredClientRepository registeredClientRepository) {
        // 基于db的授权确认管理服务，还有一个基于内存的服务实现InMemoryOAuth2AuthorizationConsentService
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 采用自定义的ServerAuthenticationDetails后，redis和jdbc加载Authentication时要求ServerAuthenticationDetails是Jackson支持的序列化类，
     * 需要向OAuth2AuthorizationService的objectMapper中注入
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
                                                           RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public OAuth2AuthorizedClientService auth2AuthorizedClientService(JdbcTemplate jdbcTemplate,
                                                                      ClientRegistrationRepository clientRegistrationRepository) {
        return new JdbcOAuth2AuthorizedClientService(jdbcTemplate, clientRegistrationRepository);
    }

    /**
     * for signing access tokens.
     * 持久化到redis中， 避免重启后无法验证已颁发的jwt
     *
     * @author Lu Cheng
     * @date 2023/10/19
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() throws ParseException {
        String jwkString = redisHelper.get("jwk_set");
        if (jwkString == null) {
            KeyPair keyPair = generateRsaKey();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAKey rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();
            JWKSet jwkSet = new JWKSet(rsaKey);
            redisHelper.set("jwk_set", jwkSet.toString(false), -1);
            return new ImmutableJWKSet<>(jwkSet);
        }
        return new ImmutableJWKSet<>(JWKSet.parse(jwkString));
    }

    /**
     * generated on startup used to create the JWKSource
     *
     * @author Lu Cheng
     * @date 2023/10/19
     */
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /**
     * for decoding signed access tokens.
     *
     * @author Lu Cheng
     * @date 2023/10/19
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * configure Spring Authorization Server.
     *
     * @author Lu Cheng
     * @date 2023/10/19
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .authorizationEndpoint("/oauth2/authorize")
                .tokenEndpoint("/oauth2/token")
                .issuer("http://127.0.0.1:8889")
                .build();
    }
}
