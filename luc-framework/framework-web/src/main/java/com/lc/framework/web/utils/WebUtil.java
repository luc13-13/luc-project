package com.lc.framework.web.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lc.framework.core.constants.RequestHeaderConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Optional;

/**
 * <pre>
 *     处理Servlet的工具类
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/7/12 9:44
 */
@UtilityClass
public class WebUtil {

    public void makeResponse(HttpServletResponse response, String contentType, int status, Object value) throws IOException {
        response.setContentType(contentType);
        response.setStatus(status);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(new JavaTimeModule());
        mapper.writeValue(response.getOutputStream(), value);
    }


    /**
     * 获取当前线程的ServletRequestAttributes
     */
    public ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public Optional<HttpServletRequest> getRequest() {
        return Optional.of(getRequestAttributes().getRequest());
    }

    /**
     * 获取当前userId
     */
    public String getUserId() {
        if (getRequest().isPresent()) {
            return  getRequest().get().getHeader(RequestHeaderConstants.USER_ID);
        }
        return null;
    }

    /**
     * 获取当前roleId
     */
    public String getRoleId() {
        if (getRequest().isPresent()) {
            return  getRequest().get().getHeader(RequestHeaderConstants.ROLE_ID);
        }
        return null;
    }
}
