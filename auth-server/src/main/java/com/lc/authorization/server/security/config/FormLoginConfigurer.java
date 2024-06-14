package com.lc.authorization.server.security.config;

import com.lc.authorization.server.property.SysSecurityProperties;
import com.lc.authorization.server.security.handler.LoginFailureHandler;
import com.lc.authorization.server.security.handler.LoginSuccessHandler;
import com.lc.framework.security.core.ServerAuthenticationDetailsSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * <pre>
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
        this.sysSecurityProperties = sysSecurityProperties;
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailureHandler = loginFailureHandler;
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.formLogin(formLoginConfig -> formLoginConfig
                        .authenticationDetailsSource(new ServerAuthenticationDetailsSource())
//                // 暂时不指定登陆页面地址， 使用spring提供的默认地址
                        .loginPage(sysSecurityProperties.getLoginPage())
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler)
                );
//        http.formLogin(Customizer.withDefaults());
    }
}
