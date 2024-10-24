package com.lc.framework.datasource.starter.creator.druid;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.CommonsLogFilter;
import com.alibaba.druid.filter.logging.Log4j2Filter;
import com.alibaba.druid.filter.logging.Log4jFilter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.lc.framework.datasource.starter.creator.DataSourceCreator;
import com.lc.framework.datasource.starter.properties.DataSourceConstants;
import com.lc.framework.datasource.starter.properties.DataSourceProperty;
import com.lc.framework.datasource.starter.tool.ConfigMergeCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 9:07
 */
@Slf4j
public class DruidDataSourceCreator implements DataSourceCreator {

    private static final ConfigMergeCreator<DruidConfig, DruidConfig> MERGE_CREATOR = new ConfigMergeCreator<>("DruidConfig", DruidConfig.class, DruidConfig.class);
    private static final Set<String> PARAMS = new HashSet<>();
    private static Method configMethod = null;

    static {
        fetchMethod();
    }

    static {
        PARAMS.add("defaultCatalog");
        PARAMS.add("defaultAutoCommit");
        PARAMS.add("defaultReadOnly");
        PARAMS.add("defaultTransactionIsolation");
        PARAMS.add("testOnReturn");
        PARAMS.add("validationQueryTimeout");
        PARAMS.add("sharePreparedStatements");
        PARAMS.add("connectionErrorRetryAttempts");
        PARAMS.add("breakAfterAcquireFailure");
        PARAMS.add("removeAbandonedTimeoutMillis");
        PARAMS.add("removeAbandoned");
        PARAMS.add("logAbandoned");
        PARAMS.add("queryTimeout");
        PARAMS.add("transactionQueryTimeout");
        PARAMS.add("timeBetweenConnectErrorMillis");
        PARAMS.add("connectTimeout");
        PARAMS.add("socketTimeout");
    }

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
        // 单个数据源的druid配置
        DruidConfig config = dataSourceProperty.getDruid();
        // 将druid全局配置合并到当前数据源配置中，优先使用当前数据源的配置
        DruidConfig mergedConfig = MERGE_CREATOR.create(globalDruidConfig, config);

        Properties properties = DruidConfigUtil.toProperties(mergedConfig);

        // 设置druid的filter
        String configFilters = properties.getProperty("druid.filters");
        List<Filter> proxyFilters = this.initFilters(mergedConfig, configFilters);
        dataSource.setProxyFilters(proxyFilters);
        try {
            // 设置其他druid属性
            configMethod.invoke(dataSource, properties);
        } catch (Exception ignore) {

        }
        //连接参数单独设置
        dataSource.setConnectProperties(mergedConfig.getConnectionProperties());
        //设置druid内置properties不支持的的参数
        for (String param : PARAMS) {
            DruidConfigUtil.setValue(dataSource, param, mergedConfig);
        }

        if (Boolean.FALSE.equals(dataSourceProperty.getLazy())) {
            try {
                dataSource.init();
            } catch (SQLException e) {
                throw new RuntimeException("druid create error", e);
            }
        }
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

    private List<Filter> initFilters(DruidConfig config, String filters) {
        List<Filter> proxyFilters = new ArrayList<>(2);
        if (StringUtils.hasText(filters)) {
            String[] filterItems = filters.split(",");
            for (String filter : filterItems) {
                switch (filter) {
                    case "stat":
                        proxyFilters.add(DruidStatConfigUtil.toStatFilter(config.getStat()));
                        break;
                    case "wall":
                        Map<String, Object> configWall = config.getWall();
                        WallConfig wallConfig = DruidWallConfigUtil.toWallConfig(configWall);
                        WallFilter wallFilter = new WallFilter();
                        wallFilter.setConfig(wallConfig);
                        String dbType = (String) configWall.get("db-type");
                        wallFilter.setDbType(dbType);
                        proxyFilters.add(wallFilter);
                        break;
                    case "slf4j":
                        proxyFilters.add(DruidLogConfigUtil.initFilter(Slf4jLogFilter.class, config.getSlf4j()));
                        break;
                    case "commons-log":
                        proxyFilters.add(DruidLogConfigUtil.initFilter(CommonsLogFilter.class, config.getCommonsLog()));
                        break;
                    case "log4j":
                        proxyFilters.add(DruidLogConfigUtil.initFilter(Log4jFilter.class, config.getLog4j()));
                        break;
                    case "log4j2":
                        proxyFilters.add(DruidLogConfigUtil.initFilter(Log4j2Filter.class, config.getLog4j2()));
                        break;
                    default:
                        log.warn("dynamic-datasource current not support [{}]", filter);
                }
            }
        }
        return proxyFilters;
    }

    /**
     * <pre>
     *     Druid since 1.2.17 use 'configFromProperties' to copy config
     *     Druid < 1.2.17 use 'configFromProperty' to copy config
     * </pre>
     *
     */
    private static void fetchMethod() {
        Class<DruidDataSource> aClass = DruidDataSource.class;
        try {
            configMethod = aClass.getMethod("configFromPropeties", Properties.class);
            return;
        } catch (NoSuchMethodException ignored) {
            log.warn("method 'configFromPropeties' not available cause Druid < 1.2.17");
        }

        try {
            configMethod = aClass.getMethod("configFromPropety", Properties.class);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        throw new RuntimeException("Druid does not has 'configFromProperties' or 'configFromPropety' method!");
    }
}
