package com.lc.framework.datasource.starter.annotation;

import com.lc.framework.datasource.starter.config.DynamicDataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <pre>
 *     开启多数据库配置的注解
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/8/12 14:30
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DynamicDataSourceAutoConfiguration.class)
public @interface EnableDynamicDataSource {
}
