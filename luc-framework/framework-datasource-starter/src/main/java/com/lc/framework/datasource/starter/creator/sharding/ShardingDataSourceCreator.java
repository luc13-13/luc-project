package com.lc.framework.datasource.starter.creator.sharding;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.lc.framework.datasource.starter.creator.DataSourceCreator;
import com.lc.framework.datasource.starter.properties.DataSourceConstants;
import com.lc.framework.datasource.starter.properties.DataSourceProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * <pre>
 *  分库分表数据源创建器
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 8:54
 */
@Slf4j
public class ShardingDataSourceCreator implements DataSourceCreator, EnvironmentAware {

    private Environment environment;


    @Override
    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        String shardingConfig = dataSourceProperty.getShardingConfig();
        log.info("ShardingDataSourceCreator加载配置:{}", shardingConfig);
        // 通过ResourceLoader获取配置
        DataSource dataSource = null;
        try {
            int index = shardingConfig.indexOf(":");
            if (index < 0) {
                // 默认从classpath加载
                dataSource = createByFile(shardingConfig);
            } else {
                String location = shardingConfig.substring(0, index);
                String name = shardingConfig.substring(index + 1);
                if ("nacos".equals(location)) {
                    dataSource = createByNacos(name);
                } else {
                    dataSource = createByFile(shardingConfig);
                }
            }
        } catch (SQLException | IOException | NacosException e) {
            log.error("creat ShardingSphereDataSource failed, caused by", new RuntimeException(e));
            return null;
        }
        return dataSource;
    }

    private DataSource createByFile(String path) throws IOException, SQLException {
        File shardingSphereConfigFile = ResourceUtils.getFile(path);
        return YamlShardingSphereDataSourceFactory.createDataSource(shardingSphereConfigFile);
    }

    private DataSource createByNacos(String dataId) throws NacosException, SQLException, IOException {
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

        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, StringUtils.hasText(configServer) ? configServer : nacosServer);
        properties.put(PropertyKeyConst.NAMESPACE, namespace);
        properties.put(PropertyKeyConst.USERNAME, StringUtils.hasText(configUsername) ? configUsername : nacosUsername);
        properties.put(PropertyKeyConst.PASSWORD, StringUtils.hasText(configPassword) ? configPassword : nacosPassword);

        ConfigService configService = NacosFactory.createConfigService(properties);
        String shardingConfig = configService.getConfig(dataId, group, 60000);
        return YamlShardingSphereDataSourceFactory.createDataSource(shardingConfig.getBytes());
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
