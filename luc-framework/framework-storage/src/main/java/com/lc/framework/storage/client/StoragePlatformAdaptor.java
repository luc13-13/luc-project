package com.lc.framework.storage.client;

import com.lc.framework.storage.core.oss.properties.BucketInfo;

/**
 * <pre>
 *     TODO 兼容不容存储厂商的操作，如获取cdn外链
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/18 20:40
 * @version : 1.0
 */
public interface StoragePlatformAdaptor {
    String getAccessUrl(BucketInfo bucketInfo, String key);
}
