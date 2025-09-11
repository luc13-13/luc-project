package com.lc.system.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.lc.framework.data.permission.interceptor.LucDataPermissionInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-11-16 11:29
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    @ConditionalOnBean(LucDataPermissionInterceptor.class)
    public MybatisPlusInterceptor interceptor(LucDataPermissionInterceptor lucDataPermissionInterceptor) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(lucDataPermissionInterceptor);
        return interceptor;
    }
}
