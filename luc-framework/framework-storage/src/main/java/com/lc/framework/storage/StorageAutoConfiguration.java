package com.lc.framework.storage;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.framework.storage.core.oss.OssClientTemplate;
import com.lc.framework.storage.core.oss.QiniuStoragePropertiesConverter;
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
@ConditionalOnProperty(prefix = StorageProperties.PREFIX, name = "enabled", havingValue = "true")
public class StorageAutoConfiguration {

    /**
     * 七牛云对象存储
     */
    @AllArgsConstructor
    @ConditionalOnProperty(prefix = StorageProperties.PREFIX, name = "type", havingValue = "qiniu")
    public static class OssStorageAutoConfiguration {
        private StorageProperties storageProperties;

        @Bean
        @ConditionalOnMissingBean
        public QiniuStoragePropertiesConverter qiniuStoragePropertiesConverter() {
            return new QiniuStoragePropertiesConverter();
        }

        @Bean
        @ConditionalOnMissingBean(StorageClientTemplate.class)
        public OssClientTemplate qiniuStorageClientTemplate(QiniuStoragePropertiesConverter propertiesConverter) {

            return new OssClientTemplate(propertiesConverter.convert(storageProperties));
        }

        @Bean
        @ConditionalOnMissingBean(AmazonS3.class)
        public AmazonS3 amazonS3() {
            ClientConfiguration clientConfiguration = new ClientConfiguration();
            clientConfiguration.setProtocol(Protocol.HTTPS);

            return AmazonS3Client.builder().build();
        }
    }
}
