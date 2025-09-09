package com.lc.framework.security.auth.server.authentication.extension.sms;

import com.lc.framework.security.auth.server.utils.OAuth2EndpointUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.MultiValueMap;

import static com.lc.framework.security.core.constants.OAuth2ParameterConstants.*;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/4 16:12
 * @version : 1.0
 */
public class SmsAuthenticationConverter implements AuthenticationConverter {

    public AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    @Override
    public Authentication convert(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getFormParameters(request);
        if (!SMS_CODE.equals(parameters.getFirst(LOGIN_TYPE))) {
            return null;
        }
        String phone = parameters.getFirst(SPRING_SECURITY_FORM_PHONE_KEY);
        String code = parameters.getFirst(OAuth2ParameterNames.CODE);
        // 构建未认证的Authentication
        SmsAuthenticationToken unAuthenticatedToken = SmsAuthenticationToken.unauthenticated(phone, code);
        // 保存remote Address 和 sessionId
        unAuthenticatedToken.setDetails(authenticationDetailsSource.buildDetails(request));
        return unAuthenticatedToken;
    }


}
