package com.lc.framework.redis.starter.customizer;

import tools.jackson.databind.json.JsonMapper;

/**
 * <pre>
 *     RedisTemplate创建过程可以通过改类来定制化
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/11 15:10
 */
@FunctionalInterface
public interface RedisJacksonCustomizer {
    void customize(JsonMapper.Builder  builder);
}
