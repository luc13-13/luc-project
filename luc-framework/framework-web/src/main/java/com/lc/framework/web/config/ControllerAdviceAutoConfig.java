package com.lc.framework.web.config;

import com.lc.framework.web.excp.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/21 19:38
 * @version : 1.0
 */
public class ControllerAdviceAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
