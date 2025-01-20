package com.lc.framework.storage.client;

/**
 * <pre>
 *     属性转换器，将StorageProperties转换为目标属性
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 14:23
 */
public interface StorageClientFactory<T> {
    /**
     * 将全局属性转换为{@link StorageClientTemplate}实现类需要的属性
     * @param properties 全局属性
     * @return 实现类需要的属性
     */
    StorageClientTemplate newInstance(T properties);
}
