package com.lc.authorization.server.security.config;

import com.lc.authorization.server.security.filter.TokenHeaderWriter;
import com.lc.authorization.server.security.handler.LoginFailureHandler;
import com.lc.authorization.server.security.handler.LoginSuccessHandler;
import com.lc.authorization.server.security.handler.LoginTargetAuthenticationEntryPoint;
import com.lc.authorization.server.security.repository.RedisSecurityContextRepository;
import com.lc.authorization.server.utils.SecurityUtils;
import com.lc.framework.security.core.properties.SysSecurityProperties;
import com.lc.framework.security.core.webflux.ServerAuthenticationDetailsSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

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
    private SysSecurityProperties sysSecurityProperties;

    @Autowired
    private TokenHeaderWriter tokenHeaderWriter;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Bean
    @Order(BASIC_AUTH_ORDER)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          CorsFilter corsFilter,
                                                          RedisSecurityContextRepository redisSecurityContextRepository,
                                                          LoginTargetAuthenticationEntryPoint loginTargetAuthenticationEntryPoint)
            throws Exception {
        AntPathRequestMatcher[] whiteRequests = new AntPathRequestMatcher[]{
                AntPathRequestMatcher.antMatcher("/assets/**"),
                AntPathRequestMatcher.antMatcher("/webjars/**"),
                AntPathRequestMatcher.antMatcher("/gitee/authorize/**"),
                AntPathRequestMatcher.antMatcher("/gitee/code/**"),
                AntPathRequestMatcher.antMatcher("/configuration"),
//                AntPathRequestMatcher.antMatcher("/oauth2/token/**"),
                AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
                AntPathRequestMatcher.antMatcher("/v3/api-docs/default"),
                AntPathRequestMatcher.antMatcher("/swagger-ui/index.html"),
                AntPathRequestMatcher.antMatcher("/v3/api-docs/swagger-config"),
                AntPathRequestMatcher.antMatcher("/doc.html"),
                AntPathRequestMatcher.antMatcher("/login"),
                AntPathRequestMatcher.antMatcher("/captcha"),
                AntPathRequestMatcher.antMatcher("/index"),
                AntPathRequestMatcher.antMatcher("/error"),
                AntPathRequestMatcher.antMatcher("/favicon.ico")
        };
        http
                // 添加默认请求头
                .headers(conf -> conf.addHeaderWriter(tokenHeaderWriter))
                .addFilter(corsFilter)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(whiteRequests).permitAll()
                        .anyRequest().authenticated()
                )
//                .oauth2ResourceServer(oauth2 -> oauth2
                        // 自定义根据token获取用户信息OAuth2AuthenticatedPrincipal的方法
//                        .opaqueToken(token -> token.introspector())
                        // 定制资源服务器的状态返回码，捕捉到认证异常时返回401未认证错误或403无权限
//                        .authenticationEntryPoint()
//                        .bearerTokenResolver()
//                )
                .oauth2Login(Customizer.withDefaults())
                .formLogin(formLoginConfig -> formLoginConfig
                        .authenticationDetailsSource(new ServerAuthenticationDetailsSource())
                        .loginPage(sysSecurityProperties.getLoginPage())
                        .loginProcessingUrl(sysSecurityProperties.getLoginApi())
                        .failureHandler(loginFailureHandler)
                        .successHandler(loginSuccessHandler))
                .sessionManagement(session -> session.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::none))
        ;
        SecurityUtils.applyDefault(http,
                loginTargetAuthenticationEntryPoint,
                redisSecurityContextRepository
        );
        SecurityFilterChain filterChain = http.build();
        log.info("资源服务器过滤链路配置：{}", filterChain.getFilters());
        return filterChain;
    }

    /**
     * 增加oauth2
     *
     * @author Lu Cheng
     * @date 2023/10/19
     */
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }

    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("gitee")
                .clientId("5f2347d5f004f353527c9f96ed162b97f461da0afc5589332172d5f2b78b71db")
                .clientSecret("8bf85746f31139726b4bead33cedc8b7c63dae8068c025b8563c58c7990e3774")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "email", "address", "phone")
                .authorizationUri("https://gitee.com/oauth/authorize")
                .tokenUri("https://gitee.com/oauth2/token")
                .userInfoUri("https://gitee.com/v5/user")
                .userNameAttributeName(IdTokenClaimNames.SUB)
//                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("gitee")
                .build();
    }
}
