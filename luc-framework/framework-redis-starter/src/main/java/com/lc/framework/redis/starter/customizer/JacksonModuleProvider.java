package com.lc.framework.redis.starter.customizer;

import tools.jackson.databind.JacksonModule;

import java.util.List;

/**
 * <pre>
 *     RedisTemplate创建过程可以通过改类来定制化
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/11 15:10
 */
@FunctionalInterface
public interface JacksonModuleProvider {
    List<JacksonModule> getModules();
}
