package com.lc.framework.storage.core;

import com.lc.framework.storage.core.oss.OssStorageProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import static com.lc.framework.storage.core.StorageProperties.PREFIX;
import com.lc.framework.storage.core.StorageConstants.StorageTypeEnum;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * <pre>
 *     存储工具属性封装，支持的存储工具类型：<br/>{@link StorageTypeEnum#OSS_STORAGE}<br/>{@link StorageTypeEnum#OSS_LOCAL}
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
