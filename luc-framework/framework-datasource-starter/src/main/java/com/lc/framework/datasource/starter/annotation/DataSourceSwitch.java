package com.lc.framework.datasource.starter.annotation;

import com.lc.framework.datasource.starter.properties.DataSourceConstants;

import java.lang.annotation.*;

/**
 * <pre>
 *     用于切换到ShardingSphereDriver驱动的DataSource
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/8/12 11:14
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceSwitch {
    /**
     * 选用的数据源名称，默认为空，选用首选数据源
     */
    String value() default DataSourceConstants.DEFAULT_DATASOURCE_NAME;
}
