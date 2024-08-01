package com.lc.auth.server.security.extension.sms;

import com.lc.auth.server.security.extension.BasicAuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/3/11 10:59
 */
public class OAuth2SmsAuthenticationProvider extends BasicAuthenticationProvider<OAuth2SmsAuthenticationToken> {



    public OAuth2SmsAuthenticationProvider(HttpSecurity http) {
        super(http);
    }



    @Override
    public UsernamePasswordAuthenticationToken buildToken(OAuth2SmsAuthenticationToken resourceOwnerToken) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(resourceOwnerToken.getMobile(), null);;
        token.setDetails(OAuth2SmsAuthenticationConverter.SMS.getValue());
        return token;
    }
}
