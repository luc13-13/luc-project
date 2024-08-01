package com.lc.auth.server.security.config;

import com.lc.auth.server.security.handler.LoginFailureHandler;
import com.lc.auth.server.security.handler.LoginSuccessHandler;
import com.lc.framework.security.core.properties.SysSecurityProperties;
import com.lc.framework.security.core.webflux.ServerAuthenticationDetailsSource;
import io.jsonwebtoken.lang.Assert;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * <pre>
 *     处理FormLogin登录失败
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/12 17:30
 */
public class FormLoginConfigurer extends AbstractHttpConfigurer<FormLoginConfigurer, HttpSecurity> {
    private final SysSecurityProperties sysSecurityProperties;

    private final LoginSuccessHandler loginSuccessHandler;

    private final LoginFailureHandler loginFailureHandler;

    public FormLoginConfigurer(SysSecurityProperties sysSecurityProperties, LoginSuccessHandler loginSuccessHandler, LoginFailureHandler loginFailureHandler) {
        Assert.notNull(sysSecurityProperties, "error: SysSecurityProperties required when use FormLogin");
        Assert.hasText(sysSecurityProperties.getLoginPage(), "error: Login page uri required when use FormLogin! ");
        this.sysSecurityProperties = sysSecurityProperties;
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailureHandler = loginFailureHandler;
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.formLogin(formLoginConfig -> formLoginConfig
                        // 为了兼容webflux作为资源服务器，自定义AuthenticationDetailsSource
                        .authenticationDetailsSource(new ServerAuthenticationDetailsSource())
                        // 要求配置登录页地址
                        .loginPage(sysSecurityProperties.getLoginPage())
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler)
                );
//        http.formLogin(Customizer.withDefaults());
    }
}
