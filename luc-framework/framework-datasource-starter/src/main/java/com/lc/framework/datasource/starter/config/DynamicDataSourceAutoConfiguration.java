package com.lc.framework.datasource.starter.config;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.lc.framework.datasource.starter.DynamicDataSource;
import com.lc.framework.datasource.starter.creator.DataSourceCreator;
import com.lc.framework.datasource.starter.creator.druid.DruidDataSourceCreator;
import com.lc.framework.datasource.starter.creator.hikari.HikariDataSourceCreator;
import com.lc.framework.datasource.starter.creator.sharding.ShardingDataSourceCreator;
import com.lc.framework.datasource.starter.properties.DynamicDataSourceProperties;
import com.lc.framework.datasource.starter.provider.DynamicDataSourceProvider;
import com.lc.framework.datasource.starter.provider.YmlDynamicDataSourceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
@Slf4j
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(value = {DataSourceAutoConfiguration.class, MybatisPlusAutoConfiguration.class},
        name = {
                "com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure",
                "com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure"
        })
@Import(DynamicDataSourceAopConfiguration.class)
@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties
public class DynamicDataSourceAutoConfiguration {

    public static final int DRUID_ORDER = 1000;
    public static final int HIKARI_ORDER = 1000;
    public static final int SHARDING_ORDER = 1000;

    /**
     * 配置类的Bean必须用RefreshScope标识，才能更新对其的依赖
     * @return 配置类bean
     */
    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = DynamicDataSourceProperties.PREFIX)
    public DynamicDataSourceProperties dynamicDataSourceProperties() {
        return new DynamicDataSourceProperties();
    }



    /**
     * 提供给mybatis的数据源, 需要声明为@Primary，因为MybatisPlusAutoConfiguration标注了@ConditionalOnSingleCandidate(DataSource.class)，存在多个bean时需要有Primary才能满足条件注解
     */
    @Bean(name = "dynamicDataSource")
    @ConditionalOnMissingBean
    @RefreshScope
    public DataSource dynamicDataSource(List<DynamicDataSourceProvider> providers,
                                        DynamicDataSourceProperties dynamicDataSourceProperties) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource(providers);
        dynamicDataSource.setPrimary(dynamicDataSourceProperties.getPrimary());
        log.info("start to create DynamicDataSource with configuration: {}", dynamicDataSourceProperties);
        return dynamicDataSource;
    }

    @Bean
    @Order(0)
    @RefreshScope
    public DynamicDataSourceProvider ymlDynamicDataSourceProvider(List<DataSourceCreator> creators,
                                                                  DynamicDataSourceProperties dynamicDataSourceProperties) {
        log.info("YmlDynamicDataSourceProvider created, DataSourceProperties{}", dynamicDataSourceProperties.hashCode());
        return new YmlDynamicDataSourceProvider(dynamicDataSourceProperties.getDatasource(), creators);
    }

    @Bean
    @Order(DRUID_ORDER)
    @ConditionalOnMissingBean
    @RefreshScope
    public DruidDataSourceCreator druidDataSourceCreator(DynamicDataSourceProperties dynamicDataSourceProperties) {
        log.info("DruidDataSourceCreator created");
        return new DruidDataSourceCreator(dynamicDataSourceProperties.getDruid());
    }

    @Bean
    @Order(HIKARI_ORDER)
    @ConditionalOnMissingBean
    @RefreshScope
    public HikariDataSourceCreator hikariDataSourceCreator(DynamicDataSourceProperties dynamicDataSourceProperties) {
        log.info("HikariDataSourceCreator created");
        return new HikariDataSourceCreator(dynamicDataSourceProperties.getHikari());
    }

    @Bean
    @Order(SHARDING_ORDER)
    @ConditionalOnMissingBean
    @RefreshScope
    public ShardingDataSourceCreator shardingDataSourceCreator() {
        log.info("ShardingDataSourceCreator created");
        return new ShardingDataSourceCreator();
    }
}
