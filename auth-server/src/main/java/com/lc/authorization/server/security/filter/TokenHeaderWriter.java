package com.lc.authorization.server.security.filter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.util.StringUtils;

import static com.lc.framework.core.constants.RequestHeaderConstants.JSESSIONID;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-10-26 14:52
 */
@Slf4j
public class TokenHeaderWriter implements HeaderWriter {
    @Override
    public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
        // 如果请求头中没有JSESSIONID属性，说明该请求是第一次访问， 向响应中增加JSESSIONID
        String jsessionid = request.getHeader(JSESSIONID);
        if(!StringUtils.hasLength(jsessionid) || jsessionid.equals("undefined") || jsessionid.equals("null")) {
            if (ArrayUtils.isNotEmpty(request.getCookies())) {
                for (Cookie cookie : request.getCookies()) {
                    if (JSESSIONID.equalsIgnoreCase(cookie.getName())) {
                        jsessionid = cookie.getValue();
                    }
                }
            }
            if(!StringUtils.hasLength(jsessionid) || jsessionid.equals("undefined") || jsessionid.equals("null")) {
                HttpSession session = request.getSession(false);
                jsessionid = session != null ? session.getId(): "";
            }
        }
        if (StringUtils.hasLength(jsessionid)) {
            log.info("向响应中写入JSESSIONID: {}", jsessionid);
            response.addHeader(JSESSIONID,  jsessionid);
        }
    }
}
