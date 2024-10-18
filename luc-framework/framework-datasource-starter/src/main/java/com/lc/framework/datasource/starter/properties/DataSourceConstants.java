package com.lc.framework.datasource.starter.properties;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/8/12 11:29
 */
public interface DataSourceConstants {
    String DEFAULT_DATASOURCE_NAME = "default";

    /**
     * druid数据源
     */
    String DRUID_DATASOURCE_TYPE = "com.alibaba.druid.pool.DruidDataSource";

    /**
     * hikari数据源
     */
    String HIKARI_DATASOURCE_TYPE = "com.zaxxer.hikari.HikariDataSource";

    /**
     * shardingsphere数据源
     */
    String SHARDING_DATASOURCE_TYPE = "org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource";



}
