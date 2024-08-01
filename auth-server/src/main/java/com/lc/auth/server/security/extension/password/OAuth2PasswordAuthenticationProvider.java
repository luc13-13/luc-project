package com.lc.auth.server.security.extension.password;

import com.lc.auth.server.security.extension.BasicAuthenticationProvider;
import com.nimbusds.oauth2.sdk.GrantType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/11 11:11
 */
public class OAuth2PasswordAuthenticationProvider extends BasicAuthenticationProvider<OAuth2PasswordAuthenticationToken> {


    public OAuth2PasswordAuthenticationProvider(HttpSecurity http) {
        super(http);
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(OAuth2PasswordAuthenticationToken resourceOwnerToken) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(resourceOwnerToken.getUsername(), resourceOwnerToken.getPassword());
        // 在附加信息中放入token类型， 以便DaoProvider中查询用户与校验
        token.setDetails(GrantType.PASSWORD.getValue());
        return token;
    }

    @Override
    public void checkRegisteredClient(RegisteredClient registeredClient) {

    }
}
