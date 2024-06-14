package com.lc.framework.security.annotation;

import java.lang.annotation.*;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/3/8 17:00
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inner {
    String value() default "";
}
