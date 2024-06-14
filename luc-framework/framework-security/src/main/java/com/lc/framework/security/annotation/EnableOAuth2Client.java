package com.lc.framework.security.annotation;

import com.lc.framework.security.resource.ResourceServerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2024/6/1 14:56
 * @version : 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ResourceServerConfig.class)
public @interface EnableOAuth2Client {
}
