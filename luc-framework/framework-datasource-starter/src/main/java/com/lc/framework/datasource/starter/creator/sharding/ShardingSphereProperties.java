package com.lc.framework.datasource.starter.creator.sharding;

import lombok.Data;
import org.apache.shardingsphere.infra.config.mode.ModeConfiguration;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.sql.DataSource;
import java.util.Map;

/**
 * <pre>
 *     shardingsphere-jdbc-5.3.x版本后，不再提供springboot的自动装配功能，只支持特殊格式的yml文件创建ShardingSphereDataSource，
 *     无法满足读取和修改配置的需求，因此使用此拓展配置文件
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/13 14:31
 */
@Data
public class ShardingSphereProperties {

    public static final String PREFIX = "spring.datasource.sharding-sphere";

    /**
     * 逻辑库名称
     */
    private String databaseName;

    /**
     * 模式配置
     */
    @NestedConfigurationProperty
    private ModeConfig mode = new ModeConfig();

    /**
     * 数据源集合<bn/>
     * key为逻辑库名称，value为数据库配置<br/>
     * 数据库配置支持DruidDataSource、HikariDataSource，数据源属性包括<br/>
     * dataSourceClassname、driverClassName、jdbcUrl/url(只有Druid数据源用url，其他数据源用jdbcUrl)、username、password、连接池的原生属性
     */
    private Map<String, Map<String, Object>> dataSources;

    /**
     * 规则配置
     */
    @NestedConfigurationProperty
    private RuleConfig rules = new RuleConfig();






}
