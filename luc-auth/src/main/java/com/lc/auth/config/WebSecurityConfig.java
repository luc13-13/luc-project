package com.lc.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

/**
 * <pre>
 * 资源服务器安全配置类
 * 处理API接口的安全配置和静态资源访问
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    /**
     * 认证服务器默认安全过滤器链
     * 处理登录、注册和OAuth2认证流程
     */
    @Bean
    @Order(2)
    public SecurityFilterChain authenticationSecurityFilterChain(HttpSecurity http,
                                                                 OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        // 认证相关的白名单路径
                        .requestMatchers(
                                PathPatternRequestMatcher.withDefaults().matcher("/"),
                                PathPatternRequestMatcher.withDefaults().matcher("/login"),
                                PathPatternRequestMatcher.withDefaults().matcher("/register"),
                                PathPatternRequestMatcher.withDefaults().matcher("/oauth2/**"),
                                PathPatternRequestMatcher.withDefaults().matcher("/css/**"),
                                PathPatternRequestMatcher.withDefaults().matcher("/js/**"),
                                PathPatternRequestMatcher.withDefaults().matcher("/images/**"),
                                PathPatternRequestMatcher.withDefaults().matcher("/static/**"),
                                PathPatternRequestMatcher.withDefaults().matcher("/favicon.ico"),
                                PathPatternRequestMatcher.withDefaults().matcher("/error"),
                                PathPatternRequestMatcher.withDefaults().matcher("/actuator/**"),
                                PathPatternRequestMatcher.withDefaults().matcher("/health"),
                                PathPatternRequestMatcher.withDefaults().matcher("/v3/api-docs/**"),
                                PathPatternRequestMatcher.withDefaults().matcher("/swagger-ui/**"),
                                PathPatternRequestMatcher.withDefaults().matcher("/doc.html"),
                                PathPatternRequestMatcher.withDefaults().matcher("/welcome"),
                                PathPatternRequestMatcher.withDefaults().matcher("/home"),
                                PathPatternRequestMatcher.withDefaults().matcher("/test/**"),
                                PathPatternRequestMatcher.withDefaults().matcher("/sms/**")
//                                PathPatternRequestMatcher.withDefaults().matcher("/api")
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // 表单登录配置
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/perform-login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                // OAuth2第三方登录配置
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                        .defaultSuccessUrl("/oauth2/login/success", true)
                )
                // 登出配置
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }


}
