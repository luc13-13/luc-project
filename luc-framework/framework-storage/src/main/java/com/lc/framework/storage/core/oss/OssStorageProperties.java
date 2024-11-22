package com.lc.framework.storage.core.oss;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 14:28
 */
@Data
@NoArgsConstructor
public class OssStorageProperties {
    /**
     * 是否开启oss存储，true是，false否。默认为false
     */
    private boolean enabled = false;

    private String accessKey;

    private String secretKey;

    /**
     * 默认bucket，需要与bucketMap中的key匹配
     */
    @NotBlank(message = "defaultBucketName must not be null")
    private String defaultBucketName;

    /**
     * 默认domain, 如果没有设置bucketMap或者bucketMap中没有设置domain，则默认取defaultDomainOfBucket
     */
    private String defaultDomainOfBucket;

    /**
     * 配置bucket信息，key为bucketName，value为具体bucket属性封装
     */
    private List<BucketInfo> buckets;
}
