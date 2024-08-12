package com.lc.framework.datasource.starter.config;

import com.lc.framework.datasource.starter.sharding.ShardingJdbcProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/8/12 17:22
 */
@AutoConfiguration(after = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(ShardingJdbcProperties.class)
public class ShardingJdbcAutoConfiguration {
    @Bean
    public DataSource sharding() {
        return null;
    }
}
