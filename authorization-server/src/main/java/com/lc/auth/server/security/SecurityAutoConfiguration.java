package com.lc.auth.server.security;

import com.lc.auth.server.security.encoder.EncoderConfiguration;
import com.lc.auth.server.security.jwt.JwtConfiguration;
import com.lc.auth.server.security.properties.LoginProperties;
import com.lc.auth.server.security.properties.SysSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.UUID;

/**
 * <pre>
 *     认证服务器自动装配。
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/2 16:57
 * @version : 1.0
 */
@Slf4j
@EnableWebSecurity
@Import({JwtConfiguration.class, EncoderConfiguration.class})
@AutoConfiguration(after = {JwtConfiguration.class, EncoderConfiguration.class})
@EnableConfigurationProperties({SysSecurityProperties.class, LoginProperties.class})
public class SecurityAutoConfiguration {

    private final SysSecurityProperties sysSecurityProperties;

    private final LoginProperties loginProperties;

    public SecurityAutoConfiguration(SysSecurityProperties sysSecurityProperties, LoginProperties loginProperties) {
        this.sysSecurityProperties = sysSecurityProperties;
        this.loginProperties = loginProperties;
    }

    /**
     * Spring Authorization Server 安全接口过滤器链。
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, authorizationServer -> authorizationServer
                        .oidc(Customizer.withDefaults())
                )
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                // Redirect to the login page when not authenticated from the authorization endpoint
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint(loginProperties.getLoginPage()),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                // Accept access tokens for User Info and/or Client Registration
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(Customizer.withDefaults()));

        return http.build();
    }

    /**
     * 认证服务器默认安全过滤器链
     * 处理登录、登出和OAuth2认证流程
     */
    @Bean
    @Order(2)
    public SecurityFilterChain authenticationSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("登陆页配置：{}", loginProperties);
        http
                .authorizeHttpRequests((authorize) -> {
                            if (!CollectionUtils.isEmpty(sysSecurityProperties.getWhitePaths())) {
                                log.info("设置访问白名单: {}", sysSecurityProperties.getWhitePaths());
                                for (String whitePath : sysSecurityProperties.getWhitePaths()) {
                                    authorize.requestMatchers(PathPatternRequestMatcher.withDefaults().matcher(whitePath));
                                }
                            }
                            authorize.anyRequest().authenticated();
                        }
                )
                // 表单登录配置
                .formLogin(formLogin -> formLogin
                        .loginPage(loginProperties.getLoginPage())
                        .defaultSuccessUrl(loginProperties.getDefaultSuccessUrl(), loginProperties.isAlwaysUseDefaultSuccessUrl())
                        .failureUrl(loginProperties.getLoginPage() + "?error")
                        .permitAll()
                )
//                 OAuth2第三方登录配置
                .oauth2Login(oauth2Login -> oauth2Login
                                .loginPage(loginProperties.getLoginPage())
                                .authorizationEndpoint(authorization -> authorization.baseUri("/oauth2/authorization"))
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .userService(oAuth2UserService)
//                        )
                                .defaultSuccessUrl(loginProperties.getDefaultOauth2SuccessUrl(), loginProperties.isAlwaysUseDefaultSuccessUrl())
                )
                // 登出配置，登出后默认跳转到首页
                .logout(logout -> logout
                        .logoutUrl(loginProperties.getLogoutUrl())
                        .logoutSuccessUrl(loginProperties.getLogoutSuccessUrl())
                        .permitAll()
                );

        return http.build();
    }

    /**
     * 注册客户端仓库
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
        // 网关客户端
        RegisteredClient gatewayClient = RegisteredClient.withId(UUID.randomUUID().toString())
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
                        .requireAuthorizationConsent(false)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(2))
                        .refreshTokenTimeToLive(Duration.ofDays(7))
                        .build())
                .build();

        // API文档客户端
        RegisteredClient apiDocClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("api-doc-client")
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

        return new InMemoryRegisteredClientRepository(gatewayClient, apiDocClient);
    }

    @Bean
    public ApplicationRunner securityApplicationRunner() {
        return args -> log.info("登陆页: {}", loginProperties.getLoginPage());
    }
}
