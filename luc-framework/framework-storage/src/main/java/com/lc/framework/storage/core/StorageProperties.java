package com.lc.framework.storage.core;

import com.lc.framework.storage.core.StorageConstants.StorageTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import static com.lc.framework.storage.core.StorageProperties.PREFIX;

/**
 * <pre>
 *     存储工具属性封装，支持的存储工具类型：<br/>{@link StorageTypeEnum#OSS_STORAGE}<br/>{@link StorageTypeEnum#OSS_LOCAL}
 *     Examples:
 *     # 存储工具配置示例
 * file:
 *   storage:
 *     enabled: true
 *     oss:
 *       enabled: true
 *       # 存储平台
 *       platform: qiniu
 *       # ak
 *       access-key: lUIjHxV60do6tsmas9ZW2FRDGh_Vs0o9YINgYbed
 *       # sk
 *       secret-key: X9jo5EcdPYgZ0os8Z-h2DFf0gMihI773_6hP3whz
 *       # 默认bucket名
 *       default-bucket-name: mini-program-cookbook
 *       # 默认domain
 *       default-domain-of-bucket: qiniucs.com
 *       use-async: true
 *       cdn: storage.cloud.ffluc.online
 *       buckets:
 *         - name: mini-program-cookbook
 *           region: cn-south-1
 *           use-https: true
 *           path-style-enabled: false
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 9:11
 */
@Data
@ConfigurationProperties(prefix = PREFIX)
public class StorageProperties {

    public static final String PREFIX = "file.storage";

    private boolean enabled = true;

    /**
     * oss属性
     */
    @NestedConfigurationProperty
    private OssStorageProperties oss = new OssStorageProperties();
}
