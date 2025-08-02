package com.lc.auth.server.security.annotation;

import com.lc.auth.server.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <pre>
 *     开启认证服务器
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/2 21:50
 * @version : 1.0
 * @see SecurityAutoConfiguration
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(SecurityAutoConfiguration.class)
public @interface EnableAuthorizationServer {

}
