package com.lc.framework.datasource.starter.annotation;

import java.lang.annotation.*;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/8/12 17:29
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String value() default "";
}
