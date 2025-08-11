package com.lc.authorization.gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.authorization.gateway.config.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 全局异常处理器
 * 处理认证和授权异常
 */
@Component
@Order(-1) // 确保在默认异常处理器之前执行
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        
        if (ex instanceof AuthenticationException) {
            return handleAuthenticationException(response, (AuthenticationException) ex);
        } else if (ex instanceof AccessDeniedException) {
            return handleAccessDeniedException(response, (AccessDeniedException) ex);
        } else {
            return handleGenericException(response, ex);
        }
    }
    
    /**
     * 处理认证异常
     */
    private Mono<Void> handleAuthenticationException(ServerHttpResponse response, AuthenticationException ex) {
        logger.warn("认证失败: {}", ex.getMessage());
        
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        
        AuthResponse errorResponse = new AuthResponse(false, false, "认证失败: " + ex.getMessage(), null);
        return writeResponse(response, errorResponse);
    }
    
    /**
     * 处理授权异常
     */
    private Mono<Void> handleAccessDeniedException(ServerHttpResponse response, AccessDeniedException ex) {
        logger.warn("访问被拒绝: {}", ex.getMessage());
        
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        
        AuthResponse errorResponse = new AuthResponse(false, false, "访问被拒绝: " + ex.getMessage(), null);
        return writeResponse(response, errorResponse);
    }
    
    /**
     * 处理其他异常
     */
    private Mono<Void> handleGenericException(ServerHttpResponse response, Throwable ex) {
        logger.error("服务器内部错误: {}", ex.getMessage(), ex);
        
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        
        AuthResponse errorResponse = new AuthResponse(false, false, "服务器内部错误", null);
        return writeResponse(response, errorResponse);
    }
    
    /**
     * 写入响应
     */
    private Mono<Void> writeResponse(ServerHttpResponse response, AuthResponse errorResponse) {
        try {
            String body = objectMapper.writeValueAsString(errorResponse);
            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            logger.error("序列化错误响应失败", e);
            String fallbackBody = "{\"valid\":false,\"expired\":false,\"message\":\"系统错误\"}";
            DataBuffer buffer = response.bufferFactory().wrap(fallbackBody.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        }
    }
}
