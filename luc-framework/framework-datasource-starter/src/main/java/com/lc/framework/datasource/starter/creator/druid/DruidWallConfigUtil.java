package com.lc.framework.datasource.starter.creator.druid;

import com.alibaba.druid.wall.WallConfig;
import com.lc.framework.datasource.starter.tool.DsConfigUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * <pre>
 *     druid防火墙配置工具
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 10:27
 */
@Slf4j
public class DruidWallConfigUtil {
    private static final Map<String, Method> METHODS = DsConfigUtil.getSetterMethods(WallConfig.class);

    /**
     * 根据当前的配置和全局的配置生成druid防火墙配置
     *
     * @param map 当前配置
     * @return 防火墙配置
     */
    public static WallConfig toWallConfig(Map<String, Object> map) {
        WallConfig wallConfig = new WallConfig();
        Object dir = map.get("dir");
        if (dir != null) {
            wallConfig.loadConfig(String.valueOf(dir));
        }
        for (Map.Entry<String, Object> item : map.entrySet()) {
            String key = DsConfigUtil.lineToUpper(item.getKey());
            Method method = METHODS.get(key);
            if (method != null) {
                try {
                    method.invoke(wallConfig, DsConfigUtil.convertValue(method, item.getValue()));
                } catch (Exception e) {
                    log.warn("druid wall set param {} error", key, e);
                }
            } else {
                log.warn("druid wall does not have param {}", key);
            }
        }
        return wallConfig;
    }
}
