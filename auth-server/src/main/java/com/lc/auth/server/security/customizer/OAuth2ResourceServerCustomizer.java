package com.lc.auth.server.security.customizer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

/**
 * <pre>
 *     资源服务器定制化配置：
 *     1、jwt配置
 *     2、BearerToken配置
 *     3、OpaqueToken配置
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/8/5 16:19
 */
public class OAuth2ResourceServerCustomizer implements Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>> {
    @Override
    public void customize(OAuth2ResourceServerConfigurer<HttpSecurity> config) {
//        config.bearerTokenResolver()
    }
}
