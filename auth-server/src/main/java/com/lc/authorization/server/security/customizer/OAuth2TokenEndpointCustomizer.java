package com.lc.authorization.server.security.customizer;

import com.lc.authorization.server.security.extension.password.OAuth2PasswordAuthenticationConverter;
import com.lc.authorization.server.security.extension.sms.OAuth2SmsAuthenticationConverter;
import org.springframework.security.config.Customizer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenEndpointConfigurer;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeRequestAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Arrays;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/3/11 14:16
 */
public class OAuth2TokenEndpointCustomizer implements Customizer<OAuth2TokenEndpointConfigurer> {
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    public OAuth2TokenEndpointCustomizer(AuthenticationSuccessHandler authenticationSuccessHandler, AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Override
    public void customize(OAuth2TokenEndpointConfigurer oAuth2TokenEndpointConfigurer) {
        oAuth2TokenEndpointConfigurer
                .accessTokenRequestConverters(converters -> converters.addAll(Arrays.asList(
                        // 内置的刷新tokenConverter
                        new OAuth2RefreshTokenAuthenticationConverter(),
                        // 内置的客户端凭据Converter
                        new OAuth2ClientCredentialsAuthenticationConverter(),
                        // 内置的授权码Converter
                        new OAuth2AuthorizationCodeAuthenticationConverter(),
                        // 内置的授权码请求Converter
                        new OAuth2AuthorizationCodeRequestAuthenticationConverter(),
                        // 自定义的用户名密码获取token方式
                        new OAuth2PasswordAuthenticationConverter(),
                        new OAuth2SmsAuthenticationConverter()
                )))
                .accessTokenResponseHandler(authenticationSuccessHandler)
                .errorResponseHandler(authenticationFailureHandler);
    }
}
