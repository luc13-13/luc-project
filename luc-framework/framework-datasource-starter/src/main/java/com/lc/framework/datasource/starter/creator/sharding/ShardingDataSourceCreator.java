package com.lc.framework.datasource.starter.creator.sharding;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.lc.framework.datasource.starter.creator.DataSourceCreator;
import com.lc.framework.datasource.starter.creator.druid.DruidConfigUtil;
import com.lc.framework.datasource.starter.properties.DataSourceConstants;
import com.lc.framework.datasource.starter.properties.DataSourceProperty;
import com.lc.framework.datasource.starter.properties.DynamicDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.driver.yaml.YamlJDBCConfiguration;
import org.apache.shardingsphere.infra.config.mode.ModeConfiguration;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.infra.util.yaml.YamlEngine;
import org.apache.shardingsphere.infra.yaml.config.swapper.mode.YamlModeConfigurationSwapper;
import org.apache.shardingsphere.infra.yaml.config.swapper.resource.YamlDataSourceConfigurationSwapper;
import org.apache.shardingsphere.infra.yaml.config.swapper.rule.YamlRuleConfigurationSwapperEngine;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import static com.lc.framework.datasource.starter.properties.DataSourceConstants.CONFIG_TYPE_NACOS;

/**
 * <pre>
 *  分库分表数据源创建器。
 *  目前支持Druid作为分库分表的基础数据源
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 8:54
 */
@Slf4j
public class ShardingDataSourceCreator implements DataSourceCreator, EnvironmentAware {

    private final DynamicDataSourceProperties globalProperties;

    private Environment environment;

    public ShardingDataSourceCreator(DynamicDataSourceProperties globalProperties) {
        this.globalProperties = globalProperties;
    }

    @Override
    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        String shardingConfig = dataSourceProperty.getShardingConfig();
        log.info("ShardingDataSourceCreator加载配置:{}", shardingConfig);
        // 通过ResourceLoader获取配置
        YamlJDBCConfiguration rootConfig;
        try {
            int index = shardingConfig.indexOf(":");
            if (index < 0) {
                // 默认从classpath加载
                rootConfig = createByFile(shardingConfig);
            } else {
                String location = shardingConfig.substring(0, index);
                String name = shardingConfig.substring(index + 1);
                if (CONFIG_TYPE_NACOS.equals(location)) {
                    rootConfig = createByNacos(name);
                } else {
                    rootConfig = createByFile(shardingConfig);
                }
            }
            // 创建DataSourceMap
            Map<String, DataSource> dataSourceMap = new YamlDataSourceConfigurationSwapper().swapToDataSources(rootConfig.getDataSources());
            // 根据DataSource类型，应用全局属性
            dataSourceMap.forEach((name, dataSource) -> {
                if (dataSource instanceof DruidDataSource) {
                    // 将druid全局配置合并到当前数据源配置中，优先使用当前数据源的配置
                    DruidConfigUtil.applyGlobalAndCurrentDruidConfig((DruidDataSource) dataSource, globalProperties.getDruid(), dataSourceProperty);
                } else {
                    log.info("当前数据源无法被应用全局属性，{}", dataSource.getClass().getName());
                }
            });
            // 获取模式配置
            ModeConfiguration modeConfig = null == rootConfig.getMode() ? null : new YamlModeConfigurationSwapper().swapToObject(rootConfig.getMode());
            rootConfig.rebuild();
            // 获取规则配置
            Collection<RuleConfiguration> ruleConfigs = new YamlRuleConfigurationSwapperEngine().swapToRuleConfigurations(rootConfig.getRules());
            return ShardingSphereDataSourceFactory.createDataSource(rootConfig.getDatabaseName(), modeConfig, dataSourceMap, ruleConfigs, rootConfig.getProps());
        } catch (IOException | NacosException | SQLException e) {
            log.error("creat ShardingSphereDataSource failed, caused by", new RuntimeException(e));
            return null;
        }
    }

    private YamlJDBCConfiguration createByFile(String path) throws IOException {
        File shardingSphereConfigFile = ResourceUtils.getFile(path);
        return YamlEngine.unmarshal(shardingSphereConfigFile, YamlJDBCConfiguration.class);
    }

    /**
     * 从nacos加载配置文件
     * @param dataId 配置文件dataId
     * @return 解析后的配置
     */
    private YamlJDBCConfiguration createByNacos(String dataId) throws NacosException, IOException {
        // nacos地址
        String configServer = environment.getProperty("spring.cloud.nacos.config.server-addr");
        String nacosServer = environment.getProperty("spring.cloud.nacos.server-addr");

        // 用户名
        String configUsername = environment.getProperty("spring.cloud.nacos.config.username");
        String nacosUsername = environment.getProperty("spring.cloud.nacos.username");

        // 密码
        String configPassword = environment.getProperty("spring.cloud.nacos.config.password");
        String nacosPassword = environment.getProperty("spring.cloud.nacos.password");

        // namespace
        String namespace = environment.getProperty("spring.cloud.nacos.config.namespace");

        // group
        String group = environment.getProperty("spring.cloud.nacos.config.group");
        if (!StringUtils.hasText(group)) {
            group = Constants.DEFAULT_GROUP;
        }

        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, StringUtils.hasText(configServer) ? configServer : nacosServer);
        properties.put(PropertyKeyConst.NAMESPACE, StringUtils.hasText(namespace) ? namespace : Constants.DEFAULT_NAMESPACE_ID);
        properties.put(PropertyKeyConst.USERNAME, StringUtils.hasText(configUsername) ? configUsername : nacosUsername);
        properties.put(PropertyKeyConst.PASSWORD, StringUtils.hasText(configPassword) ? configPassword : nacosPassword);

        ConfigService configService = NacosFactory.createConfigService(properties);
        String shardingConfig = configService.getConfig(dataId, group, 60000);
        YamlJDBCConfiguration rootConfig = YamlEngine.unmarshal(shardingConfig.getBytes(), YamlJDBCConfiguration.class);
        // 遍历datasourceMap，设置连接池属性
        if (CollectionUtils.isEmpty(rootConfig.getDataSources())) {
            throw new RuntimeException("datasource required no null in ShardingSphereDataSource");
        }
        return rootConfig;
    }

    @Override
    public boolean support(Class<? extends DataSource> type) {
        return type != null && DataSourceConstants.SHARDING_DATASOURCE_TYPE.equals(type.getName());
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
