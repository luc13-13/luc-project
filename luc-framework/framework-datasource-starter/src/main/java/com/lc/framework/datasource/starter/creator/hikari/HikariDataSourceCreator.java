package com.lc.framework.datasource.starter.creator.hikari;

import com.lc.framework.datasource.starter.creator.DataSourceCreator;
import com.lc.framework.datasource.starter.properties.DataSourceConstants;
import com.lc.framework.datasource.starter.properties.DataSourceProperty;
import com.lc.framework.datasource.starter.tool.ConfigMergeCreator;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 10:30
 */
public class HikariDataSourceCreator implements DataSourceCreator {

    private static final ConfigMergeCreator<HikariCpConfig, HikariConfig> MERGE_CREATOR = new ConfigMergeCreator<>("HikariConfig", HikariCpConfig.class, HikariConfig.class);
    private static Method configCopyMethod = null;

    static {
        fetchMethod();
    }

    /**
     * to support springboot 1.5 and 2.x
     * HikariConfig 2.x use 'copyState' to copy config
     * HikariConfig 3.x use 'copyStateTo' to copy config
     */
    @SuppressWarnings("JavaReflectionMemberAccess")
    private static void fetchMethod() {
        Class<HikariConfig> hikariConfigClass = HikariConfig.class;
        try {
            configCopyMethod = hikariConfigClass.getMethod("copyState", hikariConfigClass);
            return;
        } catch (NoSuchMethodException ignored) {
        }

        try {
            configCopyMethod = hikariConfigClass.getMethod("copyStateTo", hikariConfigClass);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        throw new RuntimeException("HikariConfig does not has 'copyState' or 'copyStateTo' method!");
    }

    private final HikariCpConfig globalConfig;

    public HikariDataSourceCreator(HikariCpConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    @Override
    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        HikariConfig config = MERGE_CREATOR.create(globalConfig, dataSourceProperty.getHikari());
        config.setUsername(dataSourceProperty.getUsername());
        config.setPassword(dataSourceProperty.getPassword());
        config.setJdbcUrl(dataSourceProperty.getUrl());
        config.setPoolName(dataSourceProperty.getPoolName());
        String driverClassName = dataSourceProperty.getDriverClassName();
        if (StringUtils.hasText(driverClassName)) {
            config.setDriverClassName(driverClassName);
        }
        if (Boolean.FALSE.equals(dataSourceProperty.getLazy())) {
            return new HikariDataSource(config);
        }
        config.validate();
        HikariDataSource dataSource = new HikariDataSource();
        try {
            configCopyMethod.invoke(config, dataSource);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("HikariConfig failed to copy to HikariDataSource", e);
        }
        return dataSource;
    }

    /**
     * 创建Druid数据源失败时，可以继续尝试创建Hikari数据源
     */
    @Override
    public boolean support(Class<? extends DataSource> type) {
        return type == null || DataSourceConstants.HIKARI_DATASOURCE_TYPE.equals(type.getName());
    }
}
