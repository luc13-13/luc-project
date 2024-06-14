package com.lc.authorization.server.security.repository;

import com.lc.authorization.server.security.SupplierDeferredSecurityContext;
import com.lc.authorization.server.utils.SecurityUtils;
import com.lc.framework.redis.starter.utils.RedisHelper;
import com.lc.framework.security.core.properties.SysSecurityProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.UUID;
import java.util.function.Supplier;

import static com.lc.framework.core.mvc.RequestHeaderConstants.ACCESS_TOKEN;

/**
 * 基于redis存储认证信息
 *
 * @author vains
 */
@Slf4j
@Component
public class RedisSecurityContextRepository implements SecurityContextRepository {

    private final RedisHelper redisHelper;

    private SysSecurityProperties sysSecurityProperties;

    public static final String SECURITY_CONTEXT_CACHE_PREFIX = "security_context";

    public RedisSecurityContextRepository(RedisHelper redisHelper,
                                          SysSecurityProperties sysSecurityProperties) {
        this.redisHelper = redisHelper;
        this.sysSecurityProperties = sysSecurityProperties;
    }

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();


    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
//        HttpServletRequest request = requestResponseHolder.getRequest();
//        return readSecurityContextFromRedis(request);
        // 方法已过时，使用 loadDeferredContext 方法
        throw new UnsupportedOperationException("Method deprecated.");
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        String tokenKey = UUID.randomUUID().toString();
        if (ObjectUtils.isEmpty(tokenKey)) {
            return;
        }

        // 如果当前的context是空的，则移除
        SecurityContext emptyContext = this.securityContextHolderStrategy.createEmptyContext();
        if (emptyContext.equals(context)) {
            redisHelper.expired(tokenKey, SECURITY_CONTEXT_CACHE_PREFIX);
        } else {
            // 保存认证信息
            redisHelper.set(tokenKey, context, SECURITY_CONTEXT_CACHE_PREFIX);
        }
        // 向request中加入tokenKey
        request.setAttribute(ACCESS_TOKEN, tokenKey);
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String jsessionid = SecurityUtils.getTokenKey(request);
        if (ObjectUtils.isEmpty(jsessionid)) {
            return false;
        }
        boolean res = redisHelper.hasKey(jsessionid, SECURITY_CONTEXT_CACHE_PREFIX);
        log.info("检查当前请求是否已经认证{}: {}", jsessionid, res);
        // 检验当前请求是否有认证信息
        return res;
    }

    @Override
    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
        Supplier<SecurityContext> supplier = () -> readSecurityContextFromRedis(request);
        return new SupplierDeferredSecurityContext(supplier, this.securityContextHolderStrategy);
    }

    /**
     * 从redis中获取认证信息
     *
     * @param request 当前请求
     * @return 认证信息
     */
    private SecurityContext readSecurityContextFromRedis(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        String tokenKey = SecurityUtils.getTokenKey(request);
        if (ObjectUtils.isEmpty(tokenKey)) {
            log.info("从redis中加载context失败， tokenKey为空");
            return null;
        }
        // 根据缓存id获取认证信息
        return redisHelper.get(tokenKey, SECURITY_CONTEXT_CACHE_PREFIX);
    }
}
