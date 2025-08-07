package com.lc.auth.server.security.handler;

import com.lc.framework.core.mvc.StatusConstants;
import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.web.utils.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

import static com.lc.framework.core.mvc.StatusConstants.LOGIN_FAILURE;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/5 14:51
 * @version : 1.0
 */
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.info("登陆失败");
        WebUtil.makeResponse(response, MediaType.APPLICATION_JSON_VALUE, StatusConstants.CODE_LOGIN_FAILURE, WebResult.error(LOGIN_FAILURE));
    }
}
