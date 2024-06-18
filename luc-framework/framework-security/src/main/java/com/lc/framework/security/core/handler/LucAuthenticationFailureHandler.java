package com.lc.framework.security.core.handler;

import com.lc.framework.core.mvc.StatusConstants;
import com.lc.framework.core.mvc.WebResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;
import java.io.IOException;

/**
 * <pre>
 *     认证失败处理器，用于处理AuthenticationException，一般被/login、/oauth2/token接口抛出，可被处理为系统异常500
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/6/18 10:24
 */
public class LucAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final HttpMessageConverter<Object> accessTokenResponseConverter = new MappingJackson2HttpMessageConverter();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        String errorMessage;
        if (exception instanceof OAuth2AuthenticationException oauth2Exception) {
            errorMessage = oauth2Exception.getError() != null && StringUtils.hasText(oauth2Exception.getError().getDescription()) ?
                    oauth2Exception.getError().getDescription() : exception.getLocalizedMessage();
        } else {
            errorMessage = exception.getLocalizedMessage();
        }
        WebResult<String> errorResult = WebResult.error(StatusConstants.BIZ_ERROR, errorMessage);
        accessTokenResponseConverter.write(errorResult, MediaType.APPLICATION_JSON, httpResponse);
    }
}
