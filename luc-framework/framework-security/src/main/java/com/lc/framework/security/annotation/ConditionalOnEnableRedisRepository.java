package com.lc.framework.security.annotation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *     是否开启RedisRepository的判断条件， 默认开启
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/8/5 16:16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@ConditionalOnProperty(value = "sys.security.redis-repository.enabled", matchIfMissing = true)
public @interface ConditionalOnEnableRedisRepository {

}
