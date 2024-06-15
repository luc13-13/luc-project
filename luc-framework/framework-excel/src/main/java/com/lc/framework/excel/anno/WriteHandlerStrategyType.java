package com.lc.framework.excel.anno;

import com.alibaba.excel.write.handler.CellWriteHandler;
import com.lc.framework.excel.handler.LoopDynamicMergeStrategy;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-09 16:53
 */
public enum WriteHandlerStrategyType {
    MERGE_INDEPENDENT("merge_independent", LoopDynamicMergeStrategy.class);

    final String strategy;
    final Class<? extends CellWriteHandler> strategyClazz;

    WriteHandlerStrategyType(String strategy, Class<? extends CellWriteHandler> strategyClazz) {
        this.strategy = strategy;
        this.strategyClazz = strategyClazz;
    }

    public String getStrategy() {
        return this.strategy;
    }

    public Class<? extends CellWriteHandler> getStrategyClazz() {
        return strategyClazz;
    }

}
