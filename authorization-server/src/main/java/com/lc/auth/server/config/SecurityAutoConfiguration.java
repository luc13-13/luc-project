package com.lc.auth.server.config;

import com.lc.auth.server.security.authentication.LucAuthenticationConfiguration;
import com.lc.auth.server.security.authentication.extension.MultiTypeAuthenticationFilter;
import com.lc.auth.server.security.encoder.EncoderConfiguration;
import com.lc.auth.server.security.handler.LoginFailureHandler;
import com.lc.auth.server.security.handler.LoginSuccessHandler;
import com.lc.auth.server.security.handler.SpaCsrfTokenRequestHandler;
import com.lc.auth.server.security.jwt.JwtConfiguration;
import com.lc.auth.server.security.properties.LoginProperties;
import com.lc.auth.server.security.properties.SysSecurityProperties;
import jakarta.servlet.Filter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.util.CollectionUtils;

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
@AllArgsConstructor
@Import({JwtConfiguration.class, EncoderConfiguration.class, LucAuthenticationConfiguration.class})
@AutoConfiguration(after = {JwtConfiguration.class, EncoderConfiguration.class, LucAuthenticationConfiguration.class})
public class SecurityAutoConfiguration {

    private final SysSecurityProperties sysSecurityProperties;

    private final LoginProperties loginProperties;

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
     * 认证服务器默认安全过滤器链，处理登录、登出和OAuth2认证流程
     * @param userDetailsService 获取用户信息，校验密码
     * @param loginSuccessHandler 登陆成功处理器
     * @param securityContextRepositoryProvider 认证信息存储方式。支持Redis，默认为{@link RequestAttributeSecurityContextRepository}和{@link HttpSessionSecurityContextRepository}
     * @param authenticationProviders 认证提供类，向{@link ProviderManager}中注入
     * @param multiTypeAuthenticationFilters Extended login way. See {@link MultiTypeAuthenticationFilter}
     */
    @Bean
    @Order(2)
    public SecurityFilterChain authenticationSecurityFilterChain(HttpSecurity http,
                                                                 UserDetailsService userDetailsService,
                                                                 LoginSuccessHandler loginSuccessHandler,
                                                                 ObjectProvider<SecurityContextRepository> securityContextRepositoryProvider,
                                                                 ObjectProvider<AuthenticationProvider> authenticationProviders,
                                                                 ObjectProvider<MultiTypeAuthenticationFilter> multiTypeAuthenticationFilters) throws Exception {
        log.info("登陆页配置：{}", loginProperties);
        http
                .authorizeHttpRequests((authorize) -> {
                            if (!CollectionUtils.isEmpty(sysSecurityProperties.getWhitePaths())) {
                                log.info("设置访问白名单: {}", sysSecurityProperties.getWhitePaths());
                                for (String whitePath : sysSecurityProperties.getWhitePaths()) {
                                    authorize.requestMatchers(PathPatternRequestMatcher.withDefaults().matcher(whitePath))
                                            .permitAll();
                                }
                            }
                            authorize.anyRequest().authenticated();
                        }
                )
                // 优先获取注册的SecurityContextRepository
                .securityContext(context -> context
                        .securityContextRepository(securityContextRepositoryProvider.getIfAvailable(
                                () -> new DelegatingSecurityContextRepository(
                                        new RequestAttributeSecurityContextRepository(),
                                        new HttpSessionSecurityContextRepository())
                                )
                        )
                )
                // 配置 UserDetailsService
                .userDetailsService(userDetailsService)
                // 表单登录配置
                .formLogin(formLogin -> formLogin
                        .loginPage(loginProperties.getLoginPage())
                        .loginProcessingUrl("/login")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(new LoginFailureHandler())
                        .permitAll()
                )
//                 OAuth2第三方登录配置
                .oauth2Login(oauth2Login -> oauth2Login
                                .loginPage(loginProperties.getLoginPage())
                                .authorizationEndpoint(authorization -> authorization.baseUri("/oauth2/authorization"))
                                .successHandler(loginSuccessHandler)
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .userService(oAuth2UserService)
//                        )
//                                .defaultSuccessUrl(loginProperties.getDefaultOauth2SuccessUrl(), loginProperties.isAlwaysUseDefaultSuccessUrl())
                )
                // 登出配置，登出后默认跳转到首页
                .logout(logout -> logout
                        .logoutUrl(loginProperties.getLogoutUrl())
                        .logoutSuccessUrl(loginProperties.getLogoutSuccessUrl())
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                // CSRF 配置
                .csrf(csrf -> csrf
                        // 忽略 OAuth2 相关路径
                        .ignoringRequestMatchers(
                                PathPatternRequestMatcher.withDefaults().matcher("/oauth2/**"),
                                PathPatternRequestMatcher.withDefaults().matcher("/login/oauth2/**"),
                                PathPatternRequestMatcher.withDefaults().matcher("/api/csrf-token")
                        )
                        // 配置 CSRF token 存储方式
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                )
                // 前后端分离项目必须开启CORS，否则前端、gateway与认证服务不同源会被拒绝。同时提供CorsConfigurationSource, {@link }
                .cors(Customizer.withDefaults());

        // 添加拓展的登陆过滤器
        multiTypeAuthenticationFilters.ifAvailable(filter -> http.addFilterBefore((Filter) filter, (Class<? extends Filter>) UsernamePasswordAuthenticationFilter.class));
        // 添加拓展的认证提供者
        authenticationProviders.forEach(http::authenticationProvider);
        return http.build();
    }


}
