package com.lc.framework.datasource.starter.creator.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.lc.framework.datasource.starter.tool.DsConfigUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <pre>
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
}
