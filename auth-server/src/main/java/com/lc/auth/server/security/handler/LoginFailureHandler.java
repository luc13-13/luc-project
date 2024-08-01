package com.lc.auth.server.security.handler;

import com.lc.framework.core.mvc.StatusConstants;
import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.web.utils.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

import static com.lc.framework.core.mvc.StatusConstants.LOGIN_FAILURE;

/**
 * <pre>
 *  处理表单登录失败异常，抛出状态码{@link StatusConstants#LOGIN_FAILURE}
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-10-19 11:21
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        WebUtil.makeResponse(response, MediaType.APPLICATION_JSON_VALUE, StatusConstants.CODE_LOGIN_FAILURE, WebResult.error(LOGIN_FAILURE));
    }
}
