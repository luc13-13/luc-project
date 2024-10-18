
package com.lc.framework.datasource.starter.config;

import com.lc.framework.datasource.starter.annotation.DataSourceSwitch;
import com.lc.framework.datasource.starter.aop.DynamicDataSourceAnnotationAdvisor;
import com.lc.framework.datasource.starter.aop.DynamicDataSourceAnnotationInterceptor;
import com.lc.framework.datasource.starter.properties.DynamicDataSourceProperties;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 15:43
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class DynamicDataSourceAopConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public DynamicDataSourceProperties dynamicDataSourceProperties() {
        return new DynamicDataSourceProperties();
    }

    /**
     * 针对注解创建切面
     * @return 切面
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public Advisor dynamicDataSourceAnnotationAdvisor() {
        DynamicDataSourceAnnotationInterceptor advice = new DynamicDataSourceAnnotationInterceptor();
        DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(advice, DataSourceSwitch.class);
        return advisor;
    }
}
