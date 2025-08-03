package com.lc.auth.server.security.authentication;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
public class RedisSecurityContextRepository implements SecurityContextRepository {

    private final RedisTemplate<String, Object> sessionRedisTemplate;
    /**
     * 认证过期时间，单位秒
     */
    private final Long DEFAULT_TIMEOUT;
    /**
     * 认证信息在redis中的key
     */
    private static final String CONTEXT_PREFIX = "spring:security:context:";
    /**
     * 返回给前端的请求头
     */
    public static final String TOKEN_HEADER = "luc-auth-token";
    /**
     * 请求头中token的前缀
     */
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        String token = extractToken(request);

        if (token != null) {
            SecurityContext context = loadContextFromRedis(token);
            if (context != null) {
                log.debug("从Redis加载SecurityContext: {}", token);
                return context;
            }
        }
        log.info("返回空的context");
        return SecurityContextHolder.createEmptyContext();
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        if (context.getAuthentication() == null || !context.getAuthentication().isAuthenticated()) {
            return;
        }

        String token = extractToken(request);
        if (token == null) {
            // 如果请求中没有token，生成新的token并返回给客户端
            token = generateToken();
            setTokenInResponse(response, token);
        }

        saveContextToRedis(token, context);
        log.debug("SecurityContext已保存到Redis: {}", token);
        request.setAttribute(TOKEN_HEADER, token);
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String token = extractToken(request);
        log.info("检查是否有context, key: {}", token);
        return token != null && contextExistsInRedis(token);
    }

    /**
     * 从Redis加载SecurityContext
     */
    private SecurityContext loadContextFromRedis(String token) {
        try {
            String key = CONTEXT_PREFIX + token;
            Object contextObj = sessionRedisTemplate.opsForValue().get(key);
            if (contextObj instanceof SecurityContext context) {
                // 刷新过期时间
                sessionRedisTemplate.expire(key, DEFAULT_TIMEOUT, TimeUnit.SECONDS);
                return context;
            }
        } catch (Exception e) {
            log.error("从Redis加载SecurityContext失败: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 保存SecurityContext到Redis
     */
    private void saveContextToRedis(String token, SecurityContext context) {
        try {
            String key = CONTEXT_PREFIX + token;
            sessionRedisTemplate.opsForValue().set(key, context, DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("保存SecurityContext到Redis失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 检查Redis中是否存在SecurityContext
     */
    private boolean contextExistsInRedis(String token) {
        try {
            String key = CONTEXT_PREFIX + token;
            return sessionRedisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("检查Redis中SecurityContext存在性失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 从请求头中提取token
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return Arrays.stream(request.getCookies()).filter(it -> TOKEN_HEADER.equals(it.getName())).map(Cookie::getValue).findFirst().orElse(null);
    }

    /**
     * 生成新的token
     */
    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "") +
                System.currentTimeMillis() +
                RandomStringUtils.secure().nextAlphanumeric(8);
    }

    /**
     * 在响应头中设置token
     */
    private void setTokenInResponse(HttpServletResponse response, String token) {
        response.setHeader("Authorization", TOKEN_PREFIX + token);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
    }

    /**
     * 删除Redis中的SecurityContext
     */
    public void removeContext(String token) {
        if (token != null) {
            try {
                String key = CONTEXT_PREFIX + token;
                sessionRedisTemplate.delete(key);
                log.debug("SecurityContext已从Redis删除: {}", token);
            } catch (Exception e) {
                log.error("从Redis删除SecurityContext失败: {}", e.getMessage(), e);
            }
        }
    }
}