package com.lc.authorization.gateway.security;

import tools.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tools.jackson.core.JacksonException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 认证失败处理器
 * 当 JWT token 验证失败时返回统一的错误响应
 *
 * @author Lu Cheng
 * @date 2025/8/15
 */
@Component
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        // 设置响应状态码
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        // 构建错误响应
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", HttpStatus.UNAUTHORIZED.value());
        errorResponse.put("message", "认证失败：" + getErrorMessage(ex));
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("path", exchange.getRequest().getPath().value());

        try {
            String responseBody = objectMapper.writeValueAsString(errorResponse);
            DataBuffer buffer = exchange.getResponse().bufferFactory()
                    .wrap(responseBody.getBytes(StandardCharsets.UTF_8));
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (JacksonException e) {
            // 如果 JSON 序列化失败，返回简单的错误信息
            String fallbackResponse = "{\"code\":401,\"message\":\"认证失败\"}";
            DataBuffer buffer = exchange.getResponse().bufferFactory()
                    .wrap(fallbackResponse.getBytes(StandardCharsets.UTF_8));
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }
    }

    /**
     * 根据异常类型返回具体的错误信息
     */
    private String getErrorMessage(AuthenticationException ex) {
        String message = ex.getMessage();
        if (message == null) {
            return "无效的访问令牌";
        }
        
        // 根据不同的异常类型返回用户友好的错误信息
        if (message.contains("expired")) {
            return "访问令牌已过期，请重新登录";
        } else if (message.contains("invalid")) {
            return "无效的访问令牌";
        } else if (message.contains("malformed")) {
            return "访问令牌格式错误";
        } else {
            return "认证失败，请检查访问令牌";
        }
    }
}
