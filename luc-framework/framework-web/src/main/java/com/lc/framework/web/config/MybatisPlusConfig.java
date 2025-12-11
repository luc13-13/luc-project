package com.lc.framework.web.config;

import com.lc.framework.web.mybatis.CreateAndUpdateObjectHandler;
import com.lc.framework.web.mybatis.MetaObjectHandlerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 8/12/25 11:18
 * @version : 1.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MetaObjectHandlerProperties.class)
public class MybatisPlusConfig {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "mybatis-plus.auto-fill-handler", value = "enabled", havingValue = "true")
     public CreateAndUpdateObjectHandler createAndUpdateObjectHandler(MetaObjectHandlerProperties  metaObjectHandlerProperties){
        log.info("createAndUpdateObjectHandler with properties:{}", metaObjectHandlerProperties);
        return new CreateAndUpdateObjectHandler(metaObjectHandlerProperties);
     }
}
