package com.lc.framework.datasource.starter.creator.sharding;

import lombok.Data;
import org.apache.shardingsphere.infra.config.mode.PersistRepositoryConfiguration;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/13 14:53
 */
@Data
public class ModeConfig {

    /**
     * 分库分表模式：Standalone、Cluster
     */
    private String type;

    /**
     * 配置文件的持久化仓库<br/>
     * Standalone模式下支持JDBC；Cluster模式下仅支持Zookeeper
     */
    private PersistRepositoryConfiguration repository;
}
