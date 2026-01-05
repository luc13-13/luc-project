package com.lc.framework.security.auth.server.handler;

import tools.jackson.databind.ObjectMapper;
import com.lc.framework.core.mvc.WebResult;
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
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.info("登陆失败");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        objectMapper.writeValue(response.getOutputStream(), WebResult.error(exception.getMessage()));
    }
}
