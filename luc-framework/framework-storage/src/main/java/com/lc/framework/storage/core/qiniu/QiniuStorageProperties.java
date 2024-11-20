package com.lc.framework.storage.core.qiniu;

import lombok.Data;

import java.util.Map;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 14:28
 */
@Data
public class QiniuStorageProperties {

    private String accessKey;

    private String secretKey;

    /**
     * 默认bucket
     */
    private String defaultBucketName;

    /**
     * 默认domain
     */
    private String defaultDomainOfBucket;

    /**
     * 是否开启https<br/>默认不开启，具体看bucket属性的设置
     */
    private Boolean enableHttps = false;

    private Map<String, BucketInfo> bucketMap;
}
