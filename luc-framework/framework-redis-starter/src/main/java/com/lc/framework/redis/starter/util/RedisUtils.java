package com.lc.framework.redis.starter.util;

import jakarta.annotation.Nonnull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/3 13:00
 * @version : 1.0
 */
public class RedisUtils implements ApplicationContextAware {
    private static final Long SUCCESS = 1L;

    private static RedisTemplate<String, Object> redisTemplate;

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     */
    public boolean expire(String key, long time) {

        return expire(key, time, TimeUnit.SECONDS);
    }

    public static boolean expire(String key, long time, TimeUnit timeUnit) {
        Optional.ofNullable(redisTemplate)
                .filter(template -> time > 0)
                .ifPresent(template -> template.expire(key, time, timeUnit));
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        RedisUtils.redisTemplate = (RedisTemplate<String, Object>) applicationContext.getBean(RedisTemplate.class);
    }
}
