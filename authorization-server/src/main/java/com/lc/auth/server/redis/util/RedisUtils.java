package com.lc.auth.server.redis.util;

import com.lc.framework.web.utils.SpringBeanUtil;
import lombok.experimental.UtilityClass;
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
@UtilityClass
public class RedisUtils {
    private static final Long SUCCESS = 1L;

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     */
    public boolean expire(String key, long time) {

        return expire(key, time, TimeUnit.SECONDS);
    }

    public boolean expire(String key, long time, TimeUnit timeUnit) {
        RedisTemplate<String, Object> redisTemplate = SpringBeanUtil.getBean(RedisTemplate.class);
        Optional.ofNullable(redisTemplate)
                .filter(template -> time > 0)
                .ifPresent(template -> template.expire(key, time, timeUnit));
        return true;
    }
}
