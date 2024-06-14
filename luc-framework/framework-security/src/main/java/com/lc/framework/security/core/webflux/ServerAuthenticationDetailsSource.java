package com.lc.framework.security.core.webflux;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationDetailsSource;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/11 16:22
 */
public class ServerAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, ServerAuthenticationDetails> {
    @Override
    public ServerAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new ServerAuthenticationDetails(context.getRemoteAddr(), extractSessionId(context));
    }

    private String extractSessionId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? session.getId() : null;
    }
}
