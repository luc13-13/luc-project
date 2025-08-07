package com.lc.auth.server.security.handler;

import com.lc.framework.web.utils.WebUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.lc.auth.server.security.authentication.extension.RedisSecurityContextRepository.TOKEN_HEADER;
import static com.lc.framework.core.mvc.StatusConstants.SUCCESS;

/**
 * <pre>
 *   只处理FormLogin登录成功，
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-10-19 11:17
 */
@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String tokenKey = request.getHeader(TOKEN_HEADER);
        if (!StringUtils.hasText(tokenKey)) {
            tokenKey = request.getAttribute(TOKEN_HEADER).toString();
        }
        log.info("登陆成功，用户: {}, tokenKey: {}", authentication.getName(), tokenKey);
        // 设置响应头返回 token（不使用 Cookie）
        if (StringUtils.hasText(tokenKey)) {
            Cookie tokenCookie = new Cookie(TOKEN_HEADER, tokenKey);
            tokenCookie.setPath("/");
            response.addCookie(tokenCookie);

            response.setHeader(TOKEN_HEADER, tokenKey);
            // 设置 CORS 头，允许前端读取自定义响应头
            response.setHeader("Access-Control-Expose-Headers", TOKEN_HEADER);
            log.info("设置响应头 {}: {}", TOKEN_HEADER, tokenKey);
        } else {
            log.warn("tokenKey 为空，无法设置响应头");
        }

        // 检查请求是否期望 JSON 响应
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            // 返回 JSON 响应
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            WebUtil.makeResponse(response, MediaType.APPLICATION_JSON_VALUE, SUCCESS, authentication);
        } else {
            // 重定向到首页
            log.info("重定向到首页，tokenKey: {}", tokenKey);
            response.sendRedirect("/");
        }
    }
}
