package com.lc.framework.storage;

import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.framework.storage.core.qiniu.QiniuStorageClientTemplate;
import com.lc.framework.storage.core.qiniu.QiniuStoragePropertiesConverter;
import com.lc.framework.storage.core.StorageProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

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
@ConditionalOnProperty(prefix = StorageProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = false)
@AllArgsConstructor
public class StorageAutoConfiguration {

    private StorageProperties storageProperties;

    /**
     * 七牛云对象存储
     */
    @ConditionalOnProperty(prefix = StorageProperties.PREFIX, name = "type", havingValue = "qiniu")
    public static class OssStorageAutoConfiguration {


        @Bean
        @ConditionalOnProperty(prefix = StorageProperties.PREFIX, name = "")
        public QiniuStoragePropertiesConverter qiniuStoragePropertiesConverter() {
            return new QiniuStoragePropertiesConverter();
        }

        @Bean
        @ConditionalOnMissingBean(StorageClientTemplate.class)
        @ConditionalOnProperty(prefix = StorageProperties.PREFIX, name = "")
        public QiniuStorageClientTemplate qiniuStorageClientTemplate(QiniuStoragePropertiesConverter propertiesConverter) {

            return new QiniuStorageClientTemplate();
        }
    }
}
