package com.lc.framework.datasource.starter.creator.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.lc.framework.datasource.starter.creator.DataSourceCreator;
import com.lc.framework.datasource.starter.properties.DataSourceConstants;
import com.lc.framework.datasource.starter.properties.DataSourceProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import javax.sql.DataSource;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 9:07
 */
@Slf4j
public class DruidDataSourceCreator implements DataSourceCreator {

    private final DruidConfig globalDruidConfig;

    public DruidDataSourceCreator(DruidConfig globalDruidConfig) {
        this.globalDruidConfig = globalDruidConfig;
    }

    @Override
    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(dataSourceProperty.getUsername());
        dataSource.setPassword(dataSourceProperty.getPassword());
        dataSource.setUrl(dataSourceProperty.getUrl());
        dataSource.setName(dataSourceProperty.getPoolName());

        String driverClassName = dataSourceProperty.getDriverClassName();
        if (StringUtils.hasText(driverClassName)) {
            dataSource.setDriverClassName(driverClassName);
        }
        // 将druid全局配置合并到当前数据源配置中，优先使用当前数据源的配置
        DruidConfigUtil.applyGlobalAndCurrentDruidConfig(dataSource, globalDruidConfig, dataSourceProperty);
        return dataSource;
    }

    /**
     * 当未指定数据源类型或指定为Druid类型时，可以创建Druid数据源
     * @param type 数据源属性
     * @return true支持创建，false不支持创建
     */
    @Override
    public boolean support(Class<? extends DataSource> type) {
        return type == null || DataSourceConstants.DRUID_DATASOURCE_TYPE.equals(type.getName());
    }

    
}
