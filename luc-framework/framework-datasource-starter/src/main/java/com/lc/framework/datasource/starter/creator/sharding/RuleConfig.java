package com.lc.framework.datasource.starter.creator.sharding;

import lombok.Data;
import org.apache.shardingsphere.broadcast.config.BroadcastRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;

/**
 * <pre>
 *     规则配置
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/13 16:06
 */
@Data
public class RuleConfig {

    /**
     * 分片规则
     */
    private ShardingRuleConfiguration sharding;

    private BroadcastRuleConfiguration broadcast;
}
