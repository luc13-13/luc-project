package com.lc.framework.storage;

import com.lc.framework.storage.local.LocalStorageAutoConfiguration;
import com.lc.framework.storage.oss.OssStorageAutoConfiguration;
import com.lc.framework.storage.core.StorageProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * <pre>
 *     存储工具自动装配
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 9:12
 */
@AutoConfiguration
@EnableConfigurationProperties(StorageProperties.class)
@Import({OssStorageAutoConfiguration.class, LocalStorageAutoConfiguration.class})
public class StorageAutoConfiguration {

}
