package com.lc.framework.redis.starter.customizer;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/11 15:10
 */
@FunctionalInterface
public interface ObjectMapperCustomizer<T> {
    void customize(T t);

    static <T> ObjectMapperCustomizer<T> withDefaults() {
        return t -> {
        };
    }
}
