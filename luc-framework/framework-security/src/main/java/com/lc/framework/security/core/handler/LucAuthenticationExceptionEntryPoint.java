package com.lc.framework.security.core.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.framework.core.mvc.WebResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static com.lc.framework.core.mvc.StatusConstants.UNAUTHORIZED;

/**
 * <pre>
 *     未登录访问认证端口时会抛出401异常，这里将异常抛给前端，前端处理登录页跳转和登录后重定向逻辑
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/6/18 10:25
 */
@Slf4j
public class LucAuthenticationExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.debug("资源服务器异常，{}", authException.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(UNAUTHORIZED.getCode());
        mapper.writeValue(response.getOutputStream(), WebResult.error(UNAUTHORIZED));
    }
}
