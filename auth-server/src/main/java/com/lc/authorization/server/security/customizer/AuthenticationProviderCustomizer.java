package com.lc.authorization.server.security.customizer;

import com.lc.authorization.server.security.extension.password.OAuth2PasswordAuthenticationProvider;
import com.lc.authorization.server.security.extension.sms.OAuth2SmsAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/3/11 10:57
 */
public class AuthenticationProviderCustomizer implements Customizer<HttpSecurity> {
    /**
     * 注入自定义的认证请求处理方式
     * @param httpSecurity the input argument
     */
    @Override
    public void customize(HttpSecurity httpSecurity) {
        List<AuthenticationProvider> providerList = Arrays.asList(new OAuth2PasswordAuthenticationProvider(httpSecurity), new OAuth2SmsAuthenticationProvider(httpSecurity));
        providerList.forEach(httpSecurity::authenticationProvider);
    }
}
