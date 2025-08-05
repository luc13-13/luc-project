package com.lc.auth.server.security.authentication.extension.sms;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/4 16:12
 * @version : 1.0
 */
public class SmsAuthenticationConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        return null;
    }
}
