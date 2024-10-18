package com.lc.framework.datasource.starter.creator.druid;

import com.alibaba.druid.filter.stat.StatFilter;
import com.lc.framework.datasource.starter.tool.DsConfigUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * <pre>
 *     druid监控配置工具
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 10:26
 */
@Slf4j
public class DruidStatConfigUtil {
    private static final Map<String, Method> METHODS = DsConfigUtil.getSetterMethods(StatFilter.class);

    static {
        try {
            METHODS.put("dbType", StatFilter.class.getDeclaredMethod("setDbType", String.class));
        } catch (Exception ignore) {
        }
    }

    /**
     * 根据当前的配置生成druid防火墙配置
     *
     * @param map 配置
     * @return StatFilter
     */
    public static StatFilter toStatFilter(Map<String, Object> map) {
        StatFilter filter = new StatFilter();
        for (Map.Entry<String, Object> item : map.entrySet()) {
            String key = DsConfigUtil.lineToUpper(item.getKey());
            Method method = METHODS.get(key);
            if (method != null) {
                try {
                    method.invoke(filter, DsConfigUtil.convertValue(method, item.getValue()));
                } catch (Exception e) {
                    log.warn("druid stat set param {} error", key, e);
                }
            } else {
                log.warn("druid stat does not have param {}", key);
            }
        }
        return filter;
    }
}
