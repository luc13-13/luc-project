package com.lc.system.service;

import com.lc.framework.datasource.starter.annotation.DataSourceSwitch;
import com.lc.framework.datasource.starter.tool.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/22 15:38
 */
@Slf4j
@Service
public class TestService {
    @DataSourceSwitch("master")
    public String dataSourceMaster() {
        return DynamicDataSourceContextHolder.peek();
    }

    @DataSourceSwitch("slave")
    public String dataSourceSwitchSlave() {
        return DynamicDataSourceContextHolder.peek();
    }

    @DataSourceSwitch("shading")
    public String dataSourceShading() {
        return DynamicDataSourceContextHolder.peek();
    }

    @DataSourceSwitch("slave")
    public String dataSourceSwitchRecursive() {
        log.info("切换至数据库：{}", this.dataSourceShading());
        log.info("切换至数据库：{}", this.dataSourceMaster());
        log.info("切换至数据库：{}", this.dataSourceSwitchSlave());
        return DynamicDataSourceContextHolder.peek();
    }
}
