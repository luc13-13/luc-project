package com.lc.framework.security.annotation;

import com.lc.framework.security.resource.ResourceServerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/4/18 10:48
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ResourceServerConfig.class)
public @interface EnableResourceServer {
}
