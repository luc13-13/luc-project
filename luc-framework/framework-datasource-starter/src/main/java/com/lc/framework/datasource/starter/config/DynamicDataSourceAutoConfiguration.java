package com.lc.framework.datasource.starter.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.lc.framework.datasource.starter.DynamicDataSource;
import com.lc.framework.datasource.starter.creator.DataSourceCreator;
import com.lc.framework.datasource.starter.creator.druid.DruidDataSourceCreator;
import com.lc.framework.datasource.starter.creator.hikari.HikariDataSourceCreator;
import com.lc.framework.datasource.starter.creator.sharding.ShardingDataSourceCreator;
import com.lc.framework.datasource.starter.properties.DynamicDataSourceProperties;
import com.lc.framework.datasource.starter.provider.DynamicDataSourceProvider;
import com.lc.framework.datasource.starter.provider.YmlDynamicDataSourceProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
import java.util.List;

/**
 * <pre>
 *     动态数据源创建方法，要早于Spring、Mybatis和Druid的自动配置
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/8/12 10:49
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(value = {DataSourceAutoConfiguration.class, MybatisPlusAutoConfiguration.class},
        name = {
                "com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure",
                "com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure"
        })
@Import(DynamicDataSourceAopConfiguration.class)
@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DynamicDataSourceAutoConfiguration {

    public static final int DRUID_ORDER = 1000;
    public static final int HIKARI_ORDER = 1000;
    public static final int SHARDING_ORDER = 1000;


    private final DynamicDataSourceProperties dynamicDataSourceProperties;

    public DynamicDataSourceAutoConfiguration(DynamicDataSourceProperties dynamicDataSourceProperties) {
        this.dynamicDataSourceProperties = dynamicDataSourceProperties;
    }


    /**
     * 提供给mybatis的数据源, 需要声明为@Primary，因为MybatisPlusAutoConfiguration标注了@ConditionalOnSingleCandidate(DataSource.class)，存在多个bean时需要有Primary才能满足条件注解
     */
    @Bean(name = "dynamicDataSource")
    @ConditionalOnMissingBean
    public DataSource dynamicDataSource(List<DynamicDataSourceProvider> providers) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource(providers);
        dynamicDataSource.setPrimary(dynamicDataSourceProperties.getPrimary());
        return dynamicDataSource;
    }

    @Bean
    @Order(0)
    public DynamicDataSourceProvider ymlDynamicDataSourceProvider(List<DataSourceCreator> creators) {
        return new YmlDynamicDataSourceProvider(dynamicDataSourceProperties.getDatasource(), creators);
    }

    @Bean
    @Order(DRUID_ORDER)
    @ConditionalOnMissingBean
    public DruidDataSourceCreator druidDataSourceCreator() {
        return new DruidDataSourceCreator(dynamicDataSourceProperties.getDruid());
    }

    @Bean
    @Order(HIKARI_ORDER)
    @ConditionalOnMissingBean
    public HikariDataSourceCreator hikariDataSourceCreator() {
        return new HikariDataSourceCreator(dynamicDataSourceProperties.getHikari());
    }

    @Bean
    @Order(SHARDING_ORDER)
    @ConditionalOnMissingBean
    public ShardingDataSourceCreator shardingDataSourceCreator() {
        return new ShardingDataSourceCreator();
    }
}
