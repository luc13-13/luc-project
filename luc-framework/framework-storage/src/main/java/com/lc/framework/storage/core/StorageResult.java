package com.lc.framework.storage.core;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lc.framework.storage.serializer.StorageResultSerializer;

/**
 * <pre>
 *     存储响应，统一的序列化为json串，子类可重写序列化方法
 *     Examples:
 *     默认的序列化结果：{
 *         "accessUrl": "https://accesUrl",
 *         "filename": "2025-01-20.89828afb-9a77-407c-9254-fe1963ce1e7d.jpg",
 *         "bucket": "mini-program-cookbook"
 *     }
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 9:21
 */
@JsonSerialize(using = StorageResultSerializer.class)
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
