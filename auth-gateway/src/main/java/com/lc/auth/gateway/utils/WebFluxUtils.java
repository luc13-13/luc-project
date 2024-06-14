package com.lc.auth.gateway.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/8 15:17
 */
@Slf4j
public class WebFluxUtils {
    /**
     * 获取请求头中的参数
     * @param request http请求
     * @param headerName 目标请求头
     * @return 返回空字符串说明request中没有目标请求头， 否则返回目标请求头的值
     */
    public static String getHeaderValue(ServerHttpRequest request, String headerName){
        HttpHeaders headers = request.getHeaders();
        // 请求头为空则返回空字符串
        if(headers.isEmpty()) {
            return "";
        }
        String headerValue = headers.getFirst(headerName);
        log.info("获取header{}:{}",headerName,headerValue);
        HttpCookie cookie;
        if (!StringUtils.hasLength(headerValue) && (cookie = request.getCookies().getFirst(headerName)) != null) {
            headerValue = cookie.getValue();
        }
        return headerValue;
        // 如果请求头中没有headerName, 则去请求参数中查找
    }
}
