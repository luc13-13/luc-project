package com.lc.framework.security.core.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * <pre>
 *     授权码获取成功处理器，访问接口/oauth2/authorize成功返回授权码时被调用
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/6/18 10:50
 */
public class AuthorizationCodeSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

    }
}
