package com.lc.auth.server.redis.customizer;

/**
 * <pre>
 *     RedisTemplate创建过程可以通过改类来定制化
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/11 15:10
 */
@FunctionalInterface
public interface ObjectMapperCustomizer<T> {
    void customize(T t);
}
