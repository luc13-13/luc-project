package com.lc.framework.storage.core.qiniu;

import lombok.Data;

import java.util.Map;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 14:53
 */
@Data
public class BucketInfo {
    /**
     * bucket属性封装<br/> key为bucketName，value为domainOfBucket
     */
    private String name;

    /**
     * bucket绑定的cdn加速域名
     */
    private String domain;

    /**
     * bucket是否使用https
     */
    private Boolean useHttps = false;
}
