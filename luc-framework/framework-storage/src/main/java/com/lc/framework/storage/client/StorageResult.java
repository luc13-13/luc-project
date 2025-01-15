package com.lc.framework.storage.client;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 9:21
 */
public interface StorageResult {
    /**
     * 获取bucket名称
     * @return bucket名称
     */
    String bucketName();

    /**
     * 获取文件名称
     * @return 文件名称
     */
    String filename();

    /**
     * 获取文件地址
     * @return url
     */
    String accessUrl();
}
