package com.lc.auth.server.security.config;

import com.lc.auth.server.security.extension.LucDaoAuthenticationProvider;
import com.lc.auth.server.security.handler.LoginFailureHandler;
import com.lc.auth.server.security.handler.LoginSuccessHandler;
import com.lc.auth.server.security.handler.LoginTargetAuthenticationEntryPoint;
import com.lc.auth.server.security.jwt.RedisBearerTokenResolver;
import com.lc.auth.server.security.repository.RedisSecurityContextRepository;
import com.lc.framework.redis.starter.utils.RedisHelper;
import com.lc.framework.security.core.properties.SysSecurityProperties;
import com.lc.framework.security.core.webflux.ServerAuthenticationDetailsSource;
import com.lc.framework.security.service.LoginUserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfigurationSource;

import static com.lc.framework.core.constants.RequestHeaderConstants.ACCESS_TOKEN;
import static org.springframework.boot.autoconfigure.security.SecurityProperties.BASIC_AUTH_ORDER;

/**
 * <pre>
 * 资源服务器安全策略设置，
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-10-19 09:29
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ResourceConfig {

    @Autowired
    private CorsConfigurationSource configurationSource;

    @Autowired
    private SysSecurityProperties sysSecurityProperties;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginUserDetailService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisHelper redisHelper;

    @Bean
    @Order(0)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          JwtDecoder jwtDecoder,
                                                          RedisSecurityContextRepository redisSecurityContextRepository,
                                                          LoginTargetAuthenticationEntryPoint loginTargetAuthenticationEntryPoint)
            throws Exception {
        http
                .authorizeHttpRequests(request -> {
                    // 配置白名单路径
                    if (!CollectionUtils.isEmpty(sysSecurityProperties.getWhitePaths())) {
                        for (String whiteUrl : sysSecurityProperties.getWhitePaths()) {
                            request.requestMatchers(AntPathRequestMatcher.antMatcher(whiteUrl)).permitAll();
                        }
                    }
                    request.anyRequest().authenticated();
                })
                // 认证信息从redis中获取
                .securityContext(securityContextConfig -> securityContextConfig.securityContextRepository(redisSecurityContextRepository))
                // 用自定义的认证信息获取方法
                .authenticationProvider(new LucDaoAuthenticationProvider(userDetailsService, passwordEncoder))
                // 资源服务器token获取和校验方式
                .oauth2ResourceServer(oauth2 -> oauth2
                        // 配置jwt解密方法
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(jwtDecoder)
                        )
                        .bearerTokenResolver(new RedisBearerTokenResolver(redisHelper))
                        .authenticationEntryPoint(loginTargetAuthenticationEntryPoint)
                )
                // 表单登录设置
                .formLogin(formLoginConfig -> formLoginConfig
                        .authenticationDetailsSource(new ServerAuthenticationDetailsSource())
                        // 前端登录页地址
                        .loginPage(sysSecurityProperties.getLoginPage())
                        // 后端登录接口
                        .loginProcessingUrl(sysSecurityProperties.getLoginApi())
                        .failureHandler(loginFailureHandler)
                        .successHandler(loginSuccessHandler))
                // 登出设置
                .logout(logoutConfig -> logoutConfig
                        // 删除cookie
                        .deleteCookies(ACCESS_TOKEN)
                        // 关闭session
                        .invalidateHttpSession(true)
                )
                .cors(corsConfig -> corsConfig.configurationSource(configurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
        ;
        SecurityFilterChain filterChain = http.build();
        log.info("资源服务器过滤链路配置：{}", filterChain.getFilters());
        return filterChain;
    }

    /**
     * 暴露静态资源的端点
     *
     */
    @Bean
    public WebSecurityCustomizer securityCustomizer(){
        return web -> web.ignoring().requestMatchers(
                AntPathRequestMatcher.antMatcher("/assets/**"),
                AntPathRequestMatcher.antMatcher("/webjars/**"),
                AntPathRequestMatcher.antMatcher("/actuator/**"),
                AntPathRequestMatcher.antMatcher("/css/**"),
                AntPathRequestMatcher.antMatcher("/error")
        );
    }

//    /**
//     * 增加oauth2
//     *
//     * @author Lu Cheng
//     * @date 2023/10/19
//     */
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
//    }
//
//    private ClientRegistration googleClientRegistration() {
//        return ClientRegistration.withRegistrationId("gitee")
//                .clientId("5f2347d5f004f353527c9f96ed162b97f461da0afc5589332172d5f2b78b71db")
//                .clientSecret("8bf85746f31139726b4bead33cedc8b7c63dae8068c025b8563c58c7990e3774")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
//                .scope("openid", "profile", "email", "address", "phone")
//                .authorizationUri("https://gitee.com/oauth/authorize")
//                .tokenUri("https://gitee.com/oauth2/token")
//                .userInfoUri("https://gitee.com/v5/user")
//                .userNameAttributeName(IdTokenClaimNames.SUB)
////                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
//                .clientName("gitee")
//                .build();
//    }
}
