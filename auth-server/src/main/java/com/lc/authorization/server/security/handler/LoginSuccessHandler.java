package com.lc.authorization.server.security.handler;

import com.lc.authorization.server.domain.dto.LoginSuccessDTO;
import com.lc.authorization.server.utils.JsonUtils;
import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.security.core.LoginUserDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.lc.framework.core.mvc.RequestHeaderConstants.ACCESS_TOKEN;

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

    public LoginSuccessHandler() {
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 返回用户信息
        log.info("登陆成功");
        // 转为UserDetail
        UserDetails userDetails = null;
        if (authentication.getPrincipal() instanceof LoginUserDetail) {
            userDetails = (UserDetails) authentication.getPrincipal();
        };
        String tokenKey = Objects.isNull(request.getAttribute(ACCESS_TOKEN)) ? null : request.getAttribute(ACCESS_TOKEN).toString();
        WebResult<LoginSuccessDTO> result = WebResult.successData(LoginSuccessDTO.builder()
                        .username(Objects.nonNull(userDetails) ? userDetails.getUsername() : "undefined")
                        .token(tokenKey)
                .build());
        response.addCookie(new Cookie(ACCESS_TOKEN, tokenKey));
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(JsonUtils.objectCovertToJson(result));
        response.getWriter().flush();
    }
}
