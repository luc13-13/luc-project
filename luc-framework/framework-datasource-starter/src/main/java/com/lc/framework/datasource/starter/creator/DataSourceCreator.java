package com.lc.framework.datasource.starter.creator;

import com.lc.framework.datasource.starter.properties.DataSourceProperty;

import javax.sql.DataSource;

/**
 * <pre>
 *     数据源创建接口，根据属性创建不同的数据源。创建顺序：druid > hikari
 *     如果需要创建其他数据源，例如druid、seata，实现该接口并创建Bean即可
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 9:07
 */
public interface DataSourceCreator {

    /**
     * 通过属性创建数据源
     * @param dataSourceProperty 当前数据源配置
     * @return 数据源对象
     */
    DataSource createDataSource(DataSourceProperty dataSourceProperty);

    /**
     * 校验是否支持对数据源的构建
     * @param type 数据源DataSource配置
     * @return true表示当前creator支持创建， false表示不支持
     */
    boolean support(Class<? extends DataSource> type);
}
