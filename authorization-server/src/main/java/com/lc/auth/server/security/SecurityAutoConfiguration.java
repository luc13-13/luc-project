package com.lc.auth.server.security;

import com.lc.auth.server.security.authentication.LucAuthenticationConfiguration;
import com.lc.auth.server.security.authentication.extension.MultiTypeAuthenticationFilter;
import com.lc.auth.server.security.encoder.EncoderConfiguration;
import com.lc.auth.server.security.handler.LoginSuccessHandler;
import com.lc.auth.server.security.jwt.JwtConfiguration;
import com.lc.auth.server.security.properties.LoginProperties;
import com.lc.auth.server.security.properties.SysSecurityProperties;
import jakarta.servlet.Filter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
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
@EnableConfigurationProperties({SysSecurityProperties.class, LoginProperties.class})
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
     * 认证服务器默认安全过滤器链
     * 处理登录、登出和OAuth2认证流程
     */
    @Bean
    @Order(2)
    public SecurityFilterChain authenticationSecurityFilterChain(HttpSecurity http,
                                                                 ObjectProvider<SecurityContextRepository> securityContextRepositoryProvider,
                                                                 LoginSuccessHandler loginSuccessHandler,
                                                                 UserDetailsService userDetailsService,
                                                                 ObjectProvider<AuthenticationProvider> authenticationProviders,
                                                                 ObjectProvider<MultiTypeAuthenticationFilter> multiTypeAuthenticationFilters) throws Exception {
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
                        .successHandler(loginSuccessHandler)
                        .failureUrl(loginProperties.getLoginPage() + "?error")
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
                        .permitAll()
                )
                // CSRF 配置
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/oauth2/**"),
                                PathPatternRequestMatcher.withDefaults().matcher("/login/oauth2/**"))
                );
        // 添加拓展的登陆过滤器
        multiTypeAuthenticationFilters.ifAvailable(filter -> http.addFilterBefore((Filter) filter, (Class<? extends Filter>) UsernamePasswordAuthenticationFilter.class));
        // 添加拓展的认证提供者
        authenticationProviders.forEach(http::authenticationProvider);
        return http.build();
    }


}
