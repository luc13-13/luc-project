package com.lc.framework.datasource.starter.properties;

import com.lc.framework.datasource.starter.creator.druid.DruidConfig;
import com.lc.framework.datasource.starter.creator.hikari.HikariCpConfig;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 8:47
 */
@Data
//@ConfigurationProperties(prefix = DynamicDataSourceProperties.PREFIX)
public class DynamicDataSourceProperties {

    public static final String PREFIX = "spring.datasource.dynamic";

    /**
     * 主数据源名称，默认为master
     */
    private String primary = "master";

    /**
     * 是否只对public方法进行拦截，默认为true
     */
    private boolean allowedPublicOnly = true;

    /**
     * 配置文件中分组配置的所有数据源
     */
    private Map<String, DataSourceProperty> datasource = new LinkedHashMap<>();

    /**
     * druid全局配置
     */
    @NestedConfigurationProperty
    private DruidConfig druid = new DruidConfig();

    /**
     * hikari全局配置
     */
    @NestedConfigurationProperty
    private HikariCpConfig hikari = new HikariCpConfig();

    /**
     * AOP配置
     */
    @NestedConfigurationProperty
    private DynamicDatasourceAopProperties aop = new DynamicDatasourceAopProperties();
}
