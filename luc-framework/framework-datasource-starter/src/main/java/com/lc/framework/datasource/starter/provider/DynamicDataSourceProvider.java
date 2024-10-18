package com.lc.framework.datasource.starter.provider;

import javax.sql.DataSource;
import java.util.Map;

/**
 * <pre>
 *     加载DataSource接口， 默认实现为从yml配置文件中加载
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/15 13:48
 */
public interface DynamicDataSourceProvider {

    /**
     * 加载所有数据源
     * @return Map，key为数据源名称，value为具体的数据源实现
     */
    Map<String, DataSource> loadDataSources();
}
