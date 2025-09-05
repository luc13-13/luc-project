package com.lc.auth.server.security.handler;

import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.web.utils.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

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
        WebUtil.makeResponse(response, MediaType.APPLICATION_JSON_VALUE, HttpStatus.BAD_REQUEST.value(), WebResult.error(exception.getMessage()));
    }
}
