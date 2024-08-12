package com.lc.framework.datasource.starter.annotation;

import com.lc.framework.datasource.starter.config.DynamicDataSourceAutoConfiguration;
import com.lc.framework.datasource.starter.config.ShardingJdbcAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/8/12 17:21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ShardingJdbcAutoConfiguration.class)
public @interface EnableShardingJdbc {
}
