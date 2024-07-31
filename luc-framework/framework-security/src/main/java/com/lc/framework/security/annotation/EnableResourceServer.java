package com.lc.framework.security.annotation;

import com.lc.framework.security.ResourceServerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/7/31 10:14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ResourceServerAutoConfiguration.class)
public @interface EnableResourceServer {
}
