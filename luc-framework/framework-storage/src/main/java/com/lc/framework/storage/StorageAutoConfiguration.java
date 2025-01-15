package com.lc.framework.storage;

import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.framework.storage.core.oss.OssStorageClientFactory;
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
public class StorageAutoConfiguration {

    /**
     * 七牛云对象存储
     */
    @AllArgsConstructor
    @ConditionalOnProperty(prefix = StorageProperties.PREFIX + ".oss", name = "enabled", havingValue = "true")
    public static class OssStorageAutoConfiguration {
        private StorageProperties storageProperties;

        @Bean
        @ConditionalOnMissingBean
        public OssStorageClientFactory ossStorageClientFactory() {
            return new OssStorageClientFactory();
        }

        @Bean
        @ConditionalOnMissingBean(StorageClientTemplate.class)
        public StorageClientTemplate storageClientTemplate(OssStorageClientFactory factory) {
            return factory.newInstance(storageProperties.getOss());
        }
    }
}
