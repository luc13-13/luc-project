package com.lc.auth.gateway.interceptor;

import com.lc.auth.gateway.utils.ReactiveRequestContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.lc.framework.core.constants.RequestHeaderConstants.*;

/**
 * 解决网关调用鉴权服务时，请求cookie被忽略的问题。
 * @Author : Lu Cheng
 * @Date : 2022/11/1 20:32
 * @Version : 1.0
 */
@Component
@Slf4j
public class FeignCookieInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {

        //从全局缓存ConcurrentHashMap中根据请求id获取请求 TODO:在请求上游补充请求id
        ServerHttpRequest request = ReactiveRequestContextHolder.get();
        HttpHeaders headers = request.getHeaders();
        log.info("拦截器获取request:{}",request);
        requestTemplate.header(cookie, headers.containsKey(cookie) ? headers.getFirst(cookie) : Strings.EMPTY);
        requestTemplate.header(ACCESS_TOKEN, StringUtils.hasLength(headers.getFirst(ACCESS_TOKEN)) ? headers.getFirst(ACCESS_TOKEN) : Strings.EMPTY);
        requestTemplate.header(REFRESH_TOKEN, StringUtils.hasLength(headers.getFirst(REFRESH_TOKEN)) ? headers.getFirst(REFRESH_TOKEN) : Strings.EMPTY);
        requestTemplate.header(JSESSIONID, StringUtils.hasLength(headers.getFirst(JSESSIONID)) ? headers.getFirst(JSESSIONID) : Strings.EMPTY);
        requestTemplate.header(REQUEST_ID, StringUtils.hasLength(headers.getFirst(REQUEST_ID)) ? headers.getFirst(REQUEST_ID) :  Strings.EMPTY);
        log.info("填充后的request:{}", requestTemplate);
    }
}
