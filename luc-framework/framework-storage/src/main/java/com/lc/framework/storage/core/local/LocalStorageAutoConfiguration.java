package com.lc.framework.storage.core.local;

import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.framework.storage.core.StorageProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/18 13:44
 * @version : 1.0
 */
@AllArgsConstructor
@ConditionalOnProperty(prefix = StorageProperties.PREFIX + ".oss", name = "enabled", havingValue = "false")
public class LocalStorageAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(StorageClientTemplate.class)
    public StorageClientTemplate localStorageClientTemplate() {
        return new LocalStorageClientTemplate();
    }
}
