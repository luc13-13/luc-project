package com.lc.auth.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 基于Redis的OAuth2授权服务
 * 将OAuth2授权信息存储在Redis中，支持分布式部署
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String AUTHORIZATION_KEY_PREFIX = "oauth2:authorization:";
    private static final String TOKEN_KEY_PREFIX = "oauth2:token:";
    private static final long DEFAULT_TIMEOUT = 30; // 默认30分钟过期

    @Override
    public void save(OAuth2Authorization authorization) {
        log.debug("保存OAuth2授权信息: id={}", authorization.getId());
        
        try {
            String key = AUTHORIZATION_KEY_PREFIX + authorization.getId();
            String value = serializeAuthorization(authorization);
            
            // 设置过期时间
            Duration timeout = getAuthorizationTimeout(authorization);
            redisTemplate.opsForValue().set(key, value, timeout.toMinutes(), TimeUnit.MINUTES);
            
            // 为不同类型的token创建索引
            saveTokenIndex(authorization);
            
        } catch (Exception e) {
            log.error("保存OAuth2授权信息失败: id={}", authorization.getId(), e);
            throw new RuntimeException("保存授权信息失败", e);
        }
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        log.debug("删除OAuth2授权信息: id={}", authorization.getId());
        
        try {
            String key = AUTHORIZATION_KEY_PREFIX + authorization.getId();
            redisTemplate.delete(key);
            
            // 删除token索引
            removeTokenIndex(authorization);
            
        } catch (Exception e) {
            log.error("删除OAuth2授权信息失败: id={}", authorization.getId(), e);
        }
    }

    @Override
    @Nullable
    public OAuth2Authorization findById(String id) {
        log.debug("根据ID查找OAuth2授权信息: id={}", id);
        
        try {
            String key = AUTHORIZATION_KEY_PREFIX + id;
            String value = redisTemplate.opsForValue().get(key);
            
            if (StringUtils.hasText(value)) {
                return deserializeAuthorization(value);
            }
            
        } catch (Exception e) {
            log.error("查找OAuth2授权信息失败: id={}", id, e);
        }
        
        return null;
    }

    @Override
    @Nullable
    public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
        log.debug("根据Token查找OAuth2授权信息: token={}, tokenType={}", token, tokenType);
        
        try {
            String indexKey = TOKEN_KEY_PREFIX + token;
            String authorizationId = redisTemplate.opsForValue().get(indexKey);
            
            if (StringUtils.hasText(authorizationId)) {
                return findById(authorizationId);
            }
            
        } catch (Exception e) {
            log.error("根据Token查找OAuth2授权信息失败: token={}", token, e);
        }
        
        return null;
    }

    /**
     * 序列化授权信息
     */
    private String serializeAuthorization(OAuth2Authorization authorization) throws Exception {
        // 这里简化处理，实际项目中需要更复杂的序列化逻辑
        Map<String, Object> data = Map.of(
                "id", authorization.getId(),
                "registeredClientId", authorization.getRegisteredClientId(),
                "principalName", authorization.getPrincipalName(),
                "authorizationGrantType", authorization.getAuthorizationGrantType().getValue(),
                "authorizedScopes", authorization.getAuthorizedScopes(),
                "attributes", authorization.getAttributes()
        );
        return objectMapper.writeValueAsString(data);
    }

    /**
     * 反序列化授权信息
     */
    private OAuth2Authorization deserializeAuthorization(String data) throws Exception {
        Map<String, Object> authData = objectMapper.readValue(data, new TypeReference<>() {
        });
        
        // 这里简化处理，实际项目中需要完整的反序列化逻辑
        // 由于OAuth2Authorization的构建比较复杂，这里返回null
        // 在实际项目中需要完整实现
        log.warn("OAuth2Authorization反序列化暂未完整实现");
        return null;
    }

    /**
     * 保存Token索引
     */
    private void saveTokenIndex(OAuth2Authorization authorization) {
        // 为授权码创建索引
        if (authorization.getToken(OAuth2ParameterNames.CODE) != null) {
            String code = authorization.getToken(OAuth2ParameterNames.CODE).getToken().getTokenValue();
            String indexKey = TOKEN_KEY_PREFIX + code;
            redisTemplate.opsForValue().set(indexKey, authorization.getId(), 10, TimeUnit.MINUTES);
        }
        
        // 为访问令牌创建索引
        if (authorization.getAccessToken() != null) {
            String accessToken = authorization.getAccessToken().getToken().getTokenValue();
            String indexKey = TOKEN_KEY_PREFIX + accessToken;
            redisTemplate.opsForValue().set(indexKey, authorization.getId(), 2, TimeUnit.HOURS);
        }
        
        // 为刷新令牌创建索引
        if (authorization.getRefreshToken() != null) {
            String refreshToken = authorization.getRefreshToken().getToken().getTokenValue();
            String indexKey = TOKEN_KEY_PREFIX + refreshToken;
            redisTemplate.opsForValue().set(indexKey, authorization.getId(), 7, TimeUnit.DAYS);
        }
    }

    /**
     * 删除Token索引
     */
    private void removeTokenIndex(OAuth2Authorization authorization) {
        // 删除各种token的索引
        if (authorization.getToken(OAuth2ParameterNames.CODE) != null) {
            String code = authorization.getToken(OAuth2ParameterNames.CODE).getToken().getTokenValue();
            redisTemplate.delete(TOKEN_KEY_PREFIX + code);
        }
        
        if (authorization.getAccessToken() != null) {
            String accessToken = authorization.getAccessToken().getToken().getTokenValue();
            redisTemplate.delete(TOKEN_KEY_PREFIX + accessToken);
        }
        
        if (authorization.getRefreshToken() != null) {
            String refreshToken = authorization.getRefreshToken().getToken().getTokenValue();
            redisTemplate.delete(TOKEN_KEY_PREFIX + refreshToken);
        }
    }

    /**
     * 获取授权超时时间
     */
    private Duration getAuthorizationTimeout(OAuth2Authorization authorization) {
        // 根据token类型设置不同的过期时间
        if (authorization.getAccessToken() != null) {
            return Duration.ofHours(2);
        } else if (authorization.getRefreshToken() != null) {
            return Duration.ofDays(7);
        } else {
            return Duration.ofMinutes(DEFAULT_TIMEOUT);
        }
    }
}
