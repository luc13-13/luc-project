package com.lc.framework.datasource.starter.creator.druid;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.CommonsLogFilter;
import com.alibaba.druid.filter.logging.Log4j2Filter;
import com.alibaba.druid.filter.logging.Log4jFilter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.lc.framework.datasource.starter.properties.DataSourceProperty;
import com.lc.framework.datasource.starter.tool.ConfigMergeCreator;
import com.lc.framework.datasource.starter.tool.DsConfigUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;

/**
 * <pre>
 *     Druid配置工具类
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 10:07
 */
@Slf4j
public class DruidConfigUtil {

    private static final String FILTERS = "druid.filters";

    private static final String CONFIG_STR = "config";
    private static final String STAT_STR = "stat";

    private static final Map<String, PropertyDescriptor> CONFIG_DESCRIPTOR_MAP = DsConfigUtil.getPropertyDescriptorMap(DruidConfig.class);
    private static final Map<String, PropertyDescriptor> DATASOURCE_DESCRIPTOR_MAP = DsConfigUtil.getPropertyDescriptorMap(DruidDataSource.class);

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

    /**
     * 将当前Druid属性覆盖全局属性并应用到目标数据源
     * @param dataSource 目标数据源
     * @param globalDruidConfig 全局的druid属性
     * @param dataSourceProperty 当前数据源的Druid属性
     */
    public static void applyGlobalAndCurrentDruidConfig(DruidDataSource dataSource, DruidConfig globalDruidConfig, DataSourceProperty dataSourceProperty) {
        dataSource.setName(dataSourceProperty.getPoolName());

        DruidConfig mergedConfig = DruidConfigUtil.mergeConfig(globalDruidConfig, dataSourceProperty.getDruid());

        Properties properties = DruidConfigUtil.toProperties(mergedConfig);

        // 设置druid的filter
        String configFilters = properties.getProperty("druid.filters");
        List<Filter> proxyFilters = DruidConfigUtil.initFilters(mergedConfig, configFilters);
        dataSource.setProxyFilters(proxyFilters);
        // 设置其他druid属性
        DruidConfigUtil.applyAnotherProperties(dataSource, properties);
        //连接参数单独设置
        dataSource.setConnectProperties(mergedConfig.getConnectionProperties());
        //设置druid内置properties不支持的的参数
        DruidConfigUtil.applyInnerProperties(dataSource, mergedConfig);

        if (Boolean.FALSE.equals(dataSourceProperty.getLazy())) {
            try {
                dataSource.init();
            } catch (SQLException e) {
                throw new RuntimeException("druid create error", e);
            }
        }
    }

    /**
     * 设置druid内置properties不支持的的参数
     */
    public static void applyInnerProperties(DruidDataSource dataSource, DruidConfig config) {
        for (String param : PARAMS) {
            setValue(dataSource, param, config);
        }
    }

    public static void applyAnotherProperties(DruidDataSource dataSource, Properties properties) {
        try {
            // 设置其他druid属性
            configMethod.invoke(dataSource, properties);
        } catch (Exception ignore) {

        }
    }

    public static DruidConfig mergeConfig(DruidConfig global, DruidConfig current) {
        return MERGE_CREATOR.create(global, current);
    }

    /**
     * 根据全局配置和本地配置结合转换为Properties
     *
     * @param config 当前配置
     * @return Druid配置
     */
    public static Properties toProperties(@NonNull DruidConfig config) {
        Properties properties = new Properties();
        for (Map.Entry<String, PropertyDescriptor> entry : CONFIG_DESCRIPTOR_MAP.entrySet()) {
            String key = entry.getKey();
            PropertyDescriptor descriptor = entry.getValue();
            Method readMethod = descriptor.getReadMethod();
            Class<?> returnType = readMethod.getReturnType();
            if (List.class.isAssignableFrom(returnType)
                    || Set.class.isAssignableFrom(returnType)
                    || Map.class.isAssignableFrom(returnType)
                    || Properties.class.isAssignableFrom(returnType)) {
                continue;
            }
            try {
                Object cValue = readMethod.invoke(config);
                if (cValue != null) {
                    properties.setProperty("druid." + key, String.valueOf(cValue));
                }
            } catch (Exception e) {
                log.warn("druid current could not set  [" + key + " ]", e);
            }
        }

        //filters单独处理，默认了stat
        String filters = config.getFilters();
        if (filters == null) {
            filters = STAT_STR;
        }
        String publicKey = config.getPublicKey();
        boolean configFilterExist = publicKey != null && !publicKey.isEmpty();
        if (publicKey != null && !publicKey.isEmpty() && !filters.contains(CONFIG_STR)) {
            filters += "," + CONFIG_STR;
        }
        properties.setProperty(FILTERS, filters);

        Properties connectProperties = Optional.ofNullable(config.getConnectionProperties())
                .orElse(new Properties());
        if (configFilterExist) {
            connectProperties.setProperty("config.decrypt", Boolean.TRUE.toString());
            connectProperties.setProperty("config.decrypt.key", publicKey);
        }
        config.setConnectionProperties(connectProperties);
        return properties;
    }

    /**
     * 设置DruidDataSource的值
     *
     * @param dataSource DruidDataSource
     * @param field      字段
     * @param c          当前配置
     */
    public static void setValue(DruidDataSource dataSource, String field, DruidConfig c) {
        try {
            Method configReadMethod = CONFIG_DESCRIPTOR_MAP.get(field).getReadMethod();
            Object value = configReadMethod.invoke(c);
            if (value != null) {
                PropertyDescriptor descriptor = DATASOURCE_DESCRIPTOR_MAP.get(field);
                if (descriptor == null) {
                    log.warn("druid current not support [" + field + " ]");
                    return;
                }
                Method writeMethod = descriptor.getWriteMethod();
                if (writeMethod == null) {
                    log.warn("druid current could not set  [" + field + " ]");
                    return;
                }
                writeMethod.invoke(dataSource, value);
            }
        } catch (Exception e) {
            log.warn("druid current  set  [" + field + " ] error");
        }
    }

    public static List<Filter> initFilters(DruidConfig config, String filters) {
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
