package com.lc.framework.datasource.starter.creator.druid;

import com.alibaba.druid.filter.logging.LogFilter;
import com.lc.framework.datasource.starter.tool.DsConfigUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * <pre>
 *     druid日志配置工具
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 10:28
 */
@Slf4j
public class DruidLogConfigUtil {
    private static final Map<String, Method> METHODS = DsConfigUtil.getSetterMethods(LogFilter.class);

    /**
     * 根据当前的配置生成druid的日志filter
     *
     * @param clazz 日志类
     * @param map   配置
     * @return 日志filter
     */
    public static LogFilter initFilter(Class<? extends LogFilter> clazz, Map<String, Object> map) {
        try {
            LogFilter filter = clazz.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, Object> item : map.entrySet()) {
                String key = DsConfigUtil.lineToUpper(item.getKey());
                Method method = METHODS.get(key);
                if (method != null) {
                    try {
                        method.invoke(filter, DsConfigUtil.convertValue(method, item.getValue()));
                    } catch (Exception e) {
                        log.warn("druid {} set param {} error", clazz.getName(), key, e);
                    }
                } else {
                    log.warn("druid {} does not have param {}", clazz.getName(), key);
                }
            }
            return filter;
        } catch (Exception e) {
            return null;
        }
    }
}
