package com.lc.auth.server.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;

import java.io.IOException;

import static com.lc.framework.core.constants.RequestHeaderConstants.*;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/8/2 10:06
 */
public class ClientAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final SecurityContextRepository securityContextRepository;

    public ClientAuthenticationSuccessHandler(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 创建信息SecurityContext
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        // 保存至上下文
        SecurityContextHolder.setContext(securityContext);
        // 保存至repository
        if (authentication.getPrincipal().equals(KNIFE4J_CLIENT_ID)) {
            request.setAttribute(ACCESS_TOKEN, KNIFE4J_TOKEN_KEY);
        }
        securityContextRepository.saveContext(securityContext, request, response);
    }
}
