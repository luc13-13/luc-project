package com.lc.auth.server.security.handler;

import com.lc.auth.server.domain.dto.LoginSuccessDTO;
import com.lc.auth.server.utils.SecurityUtils;
import com.lc.framework.security.core.LoginUserDetail;
import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.web.utils.WebUtil;
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

import static com.lc.framework.core.constants.RequestHeaderConstants.ACCESS_TOKEN;
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

    public LoginSuccessHandler() {
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 转为UserDetail
        UserDetails userDetails = null;
        if (authentication.getPrincipal() instanceof LoginUserDetail) {
            userDetails = (UserDetails) authentication.getPrincipal();
        };
        // 创建tokenKey,
        String tokenKey = SecurityUtils.getTokenKey(request);
        log.info("登陆成功, tokenKey为：{}", tokenKey);
        WebResult<LoginSuccessDTO> result = WebResult.successData(LoginSuccessDTO.builder()
                        .username(Objects.nonNull(userDetails) ? userDetails.getUsername() : "undefined")
                        .token(tokenKey)
                .build());
        response.addCookie(new Cookie(ACCESS_TOKEN, tokenKey));
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        WebUtil.makeResponse(response, MediaType.APPLICATION_JSON_VALUE, SUCCESS, result);
    }
}
