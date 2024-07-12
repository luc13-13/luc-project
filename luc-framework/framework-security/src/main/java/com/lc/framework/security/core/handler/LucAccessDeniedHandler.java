package com.lc.framework.security.core.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.framework.core.mvc.StatusConstants;
import com.lc.framework.core.mvc.WebResult;
import com.nimbusds.common.contenttype.ContentType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.awt.*;
import java.io.IOException;

import static com.lc.framework.core.mvc.StatusConstants.FORBIDDEN;

/**
 * <pre>
 *     提供给资源服务器——无权限的处理器
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/6/18 10:12
 */
public class LucAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(FORBIDDEN.getCode());
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), WebResult.error(FORBIDDEN));
    }
}
