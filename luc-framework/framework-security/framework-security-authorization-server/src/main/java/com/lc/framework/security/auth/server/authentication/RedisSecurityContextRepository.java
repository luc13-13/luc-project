package com.lc.framework.security.auth.server.authentication;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.lc.framework.security.core.constants.OAuth2ParameterConstants.*;

@Slf4j
@AllArgsConstructor
@NullMarked
public class RedisSecurityContextRepository implements SecurityContextRepository {

    private final RedisTemplate<String, Object> sessionRedisTemplate;
    /**
     * 认证过期时间，单位秒
     */
    private final Long DEFAULT_TIMEOUT;


    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        String token = extractToken(request);

        if (StringUtils.hasText(token)) {
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
        if (!StringUtils.hasText(token)) {
            // 如果请求中没有token，生成新的token并返回给客户端
            token = generateToken();
            setTokenInResponse(response, token);
        }

        saveContextToRedis(token, context);
        log.debug("SecurityContext已保存到Redis: {}", token);
        request.setAttribute(AUTH_KEY, token);
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String token = extractToken(request);
        log.info("检查是否有context, key: {}", token);
        return StringUtils.hasText(token) && contextExistsInRedis(token);
    }

    /**
     * 从Redis加载SecurityContext
     */
    private @Nullable SecurityContext loadContextFromRedis(String token) {
        try {
            String key = SECURITY_CONTEXT_PREFIX + token;
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
            String key = SECURITY_CONTEXT_PREFIX + token;
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
            String key = SECURITY_CONTEXT_PREFIX + token;
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
        String token = request.getHeader(AUTH_KEY);
        if (StringUtils.hasText(token) && token.startsWith(BEARER_TOKEN_PREFIX)) {
            return token.substring(BEARER_TOKEN_PREFIX.length());
        }
        Cookie cookie;
        if (!StringUtils.hasText(token) && (cookie = WebUtils.getCookie(request, AUTH_KEY)) != null) {
            token = cookie.getValue();
        }
        return token;
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
        response.setHeader("Authorization", BEARER_TOKEN_PREFIX + token);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
    }
}