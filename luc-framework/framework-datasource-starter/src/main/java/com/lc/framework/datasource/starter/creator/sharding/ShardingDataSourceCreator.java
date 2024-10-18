package com.lc.framework.datasource.starter.creator.sharding;

import com.lc.framework.datasource.starter.creator.DataSourceCreator;
import com.lc.framework.datasource.starter.properties.DataSourceConstants;
import com.lc.framework.datasource.starter.properties.DataSourceProperty;
import lombok.extern.slf4j.Slf4j;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * <pre>
 *  分库分表数据源创建器
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 8:54
 */
@Slf4j
public class ShardingDataSourceCreator implements DataSourceCreator {
    @Override
    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        String ymlConfigPath = dataSourceProperty.getShardingConfig();
        log.info("ShardingDataSourceCreator加载配置:{}", ymlConfigPath);
        File shardingSphereConfigFile = new File(ymlConfigPath);
        DataSource dataSource = null;
//        try {
////            dataSource = YamlShardingSphereDataSourceFactory.createDataSource(shardingSphereConfigFile);
//        } catch (SQLException | IOException e) {
//            log.warn("creat ShardingSphereDataSource failed, caused by", new RuntimeException(e));
//        }
        return dataSource;
    }

    @Override
    public boolean support(DataSourceProperty dataSourceProperty) {
        return dataSourceProperty.getSharding()
                && dataSourceProperty.getShardingConfig() != null
                && dataSourceProperty.getType() != null
                && DataSourceConstants.SHARDING_DATASOURCE_TYPE.equals(dataSourceProperty.getType().getName());
    }
}
