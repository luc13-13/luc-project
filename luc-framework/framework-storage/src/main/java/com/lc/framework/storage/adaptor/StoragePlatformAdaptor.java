package com.lc.framework.storage.adaptor;

import com.lc.framework.storage.core.BucketInfo;

/**
 * <pre>
 *     TODO 兼容不容存储厂商的操作，如获取cdn外链
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/18 20:40
 * @version : 1.0
 */
public interface StoragePlatformAdaptor {

    /**
     * 获取外链
     * @param bucketInfo bucket信息
     * @param key 文件key, 文件系统中全路径名，不包含第一个目录前的斜杠/
     * @return 外链
     */
    String getAccessUrl(BucketInfo bucketInfo, String key);
}
