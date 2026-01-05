package com.lc.framework.security.auth.server.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.lc.framework.core.mvc.StatusConstants.SUCCESS;
import static com.lc.framework.security.core.constants.OAuth2ParameterConstants.AUTH_KEY;

/**
 * <pre>
 *   处理/login，被MultiTypeAuthenticationFilter、UsernamePasswordAuthenticationFilter调用;
 *   处理/login/oauth2/code/**，被OAuth2LoginAuthenticationFilter调用
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-10-19 11:17
 */
@Slf4j
@NullMarked
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String tokenKey = request.getHeader(AUTH_KEY);
        if (!StringUtils.hasText(tokenKey)) {
            tokenKey = request.getAttribute(AUTH_KEY).toString();
        }
        log.info("登陆成功，用户: {}, tokenKey: {}, responseKey: {}", authentication.getName(), tokenKey, response.getHeader(AUTH_KEY));
        // 设置响应头返回 token（不使用 Cookie）
        if (StringUtils.hasText(tokenKey)) {
            Cookie tokenCookie = new Cookie(AUTH_KEY, tokenKey);
            tokenCookie.setPath("/");
            response.addCookie(tokenCookie);

            response.setHeader(AUTH_KEY, tokenKey);
            // 设置 CORS 头，允许前端读取自定义响应头
            response.setHeader("Access-Control-Expose-Headers", AUTH_KEY);
            log.info("设置响应头 {}: {}", AUTH_KEY, tokenKey);
        } else {
            log.warn("tokenKey 为空，无法设置响应头");
        }

        // 检查请求是否期望 JSON 响应
        String acceptHeader = request.getHeader("Accept");

        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            // 返回 JSON 响应
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(SUCCESS);
            objectMapper.writeValue(response.getOutputStream(), tokenKey);
        } else {
            // 获取原始请求的redirect参数
            String redirectUrl = request.getParameter("redirect");
            if (StringUtils.hasText(redirectUrl)) {
                // 如果有redirect参数，重定向到指定地址并携带token
                String callbackUrl = redirectUrl + (redirectUrl.contains("?") ? "&" : "?") + "token=" + tokenKey;
                log.info("重定向到指定地址: {}", callbackUrl);
                response.sendRedirect(callbackUrl);
            } else {
                // 默认重定向到vben-ele前端回调页面
                log.info("重定向到前端回调页面，tokenKey: {}", tokenKey);
                response.sendRedirect("http://127.0.0.1/oauth2/callback?token=" + tokenKey);
            }
        }
    }
}
