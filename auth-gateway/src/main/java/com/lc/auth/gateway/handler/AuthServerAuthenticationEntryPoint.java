package com.lc.auth.gateway.handler;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.lc.framework.core.mvc.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <pre>
 *     未携带token、token过期访问时的处理策略， 参考{@link RedirectServerAuthenticationEntryPoint}
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/9 9:19
 */
@Slf4j
@Component
public class AuthServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        // 捕获到AuthenticationException时直接返回认证失效
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set("Access-Control-Allow-Origin", "*");
        response.getHeaders().set("Cache-Control", "no-cache");
        DataBuffer buffer = response.bufferFactory().wrap(JSON.toJSONString(WebResult.error("认证过期", 401, "认证过期")).getBytes(Charsets.UTF_8));
        return response.writeWith(Mono.just(buffer).doFinally(signalType -> {
            log.info("接收到信号: {}, 关闭buffer", signalType);
            DataBufferUtils.release(buffer);
        }));
//                ServerResponse
//                .status(HttpStatus.UNAUTHORIZED)
//                .headers(headers -> {
//                    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//                    headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, StringConstants.ASTERISK);
//                    headers.set(HttpHeaders.CACHE_CONTROL, "no-cache");
//                })
//                .bodyValue(WebResult.error("认证失效", 401, "认证失效")).then();
    }

}
