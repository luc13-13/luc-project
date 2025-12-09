package com.lc.framework.web.config;

import com.lc.framework.web.mybatis.CreateAndUpdateObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
public class MybatisPlusConfig {

    @Value("${mybatis-plus.auto-fill-dt.insert-field:dtCreated}")
    private String createTimeField;

    @Value("${mybatis-plus.auto-fill-dt.update-field:dtModified}")
    private String updateTimeField;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "mybatis-plus.auto-fill-dt", value = "enabled", havingValue = "true", matchIfMissing = true)
     public CreateAndUpdateObjectHandler createAndUpdateObjectHandler(){
        log.info("createAndUpdateObjectHandler with createTimeField:{}, updateTimeField:{}", createTimeField, updateTimeField);
        return new CreateAndUpdateObjectHandler(createTimeField,  updateTimeField);
     }
}
