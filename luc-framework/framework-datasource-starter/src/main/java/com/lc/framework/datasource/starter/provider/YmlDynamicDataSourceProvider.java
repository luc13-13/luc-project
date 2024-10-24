package com.lc.framework.datasource.starter.provider;


import com.lc.framework.datasource.starter.creator.DataSourceCreator;
import com.lc.framework.datasource.starter.properties.DataSourceProperty;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     通过yml配置文件加载数据源
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/15 14:00
 */
@Slf4j
public class YmlDynamicDataSourceProvider implements DynamicDataSourceProvider{

    /**
     * 数据源配置
     */
    private final Map<String, DataSourceProperty> dataSourcePropertyMap;

    /**
     * 数据源创建器
     */
    private final List<DataSourceCreator> dataSourceCreators;

    public YmlDynamicDataSourceProvider(Map<String, DataSourceProperty> dataSourcePropertyMap, List<DataSourceCreator> dataSourceCreators) {
        this.dataSourcePropertyMap = dataSourcePropertyMap;
        this.dataSourceCreators = dataSourceCreators;
    }

    @Override
    public Map<String, DataSource> loadDataSources() {
        Map<String, DataSource> dataSourceMap = new HashMap<>(dataSourcePropertyMap.size() * 2);
        for (Map.Entry<String, DataSourceProperty> entry : dataSourcePropertyMap.entrySet()) {
            String dataSourceName = entry.getKey();
            DataSourceProperty property = entry.getValue();
            String poolName = property.getPoolName();
            if (poolName == null || "".equals(poolName)) {
                property.setPoolName(dataSourceName);
            }
            for (DataSourceCreator creator : dataSourceCreators) {
                if (creator.support(property.getType())) {
                    DataSource dataSource = creator.createDataSource(property);
                    if (dataSource != null) {
                        log.info("DataSource of type {} has been created", dataSource.getClass().getName());
                        dataSourceMap.put(dataSourceName, dataSource);
                    }
                    break;
                }
            }
        }
        return dataSourceMap;
    }
}
