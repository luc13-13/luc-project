
package com.lc.framework.datasource.starter.config;

import com.lc.framework.datasource.starter.annotation.DataSourceSwitch;
import com.lc.framework.datasource.starter.aop.DynamicDataSourceAnnotationAdvisor;
import com.lc.framework.datasource.starter.aop.advice.DynamicDataSourceAnnotationInterceptor;
import com.lc.framework.datasource.starter.properties.DataSourceProperty;
import com.lc.framework.datasource.starter.properties.DynamicDataSourceProperties;
import com.lc.framework.datasource.starter.tool.DataSourceClassResolver;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.beans.BeanMetadataAttribute;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Role;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * <pre>
 *     创建切面Advisor， 所有Advisor将会在AbstractAdvisorAutoProxy中被获取。
 *     每个Bean在创建时都会被判断是否满足Advisor的条件，从而进行代理增强
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 15:43
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class DynamicDataSourceAopConfiguration {

    private final DynamicDataSourceProperties dynamicDataSourceProperties;

    public DynamicDataSourceAopConfiguration(DynamicDataSourceProperties dynamicDataSourceProperties) {
        this.dynamicDataSourceProperties = dynamicDataSourceProperties;
    }

    /**
     * 针对注解DataSourceSwitch，创建切面
     * @return 切面
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public Advisor dynamicDataSourceAnnotationAdvisor() {
        DynamicDataSourceAnnotationInterceptor advice = new DynamicDataSourceAnnotationInterceptor(DataSourceSwitch.class, new DataSourceClassResolver(dynamicDataSourceProperties.isAllowedPublicOnly()));
        DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(advice, DataSourceSwitch.class);
        return advisor;
    }

    /**
     * 动态注册所有基于表达式的数据源切换方式
     */
    public static class DynamicDataSourceExpressionAdvisorRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

        private DynamicDataSourceProperties dynamicDataSourceProperties;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            if (dynamicDataSourceProperties != null && !CollectionUtils.isEmpty(dynamicDataSourceProperties.getDatasource())) {
                for (Map.Entry<String, DataSourceProperty> dataSource : dynamicDataSourceProperties.getDatasource().entrySet()) {
                    if (dataSource.getValue() != null && dataSource.getValue().getPointcut() != null) {
                        RootBeanDefinition beanDefinition = new RootBeanDefinition();
                        beanDefinition.setBeanClass(AspectJExpressionPointcutAdvisor.class);
                        BeanMetadataAttribute pointcutAttribute = beanDefinition.getMetadataAttribute("pointcut");
                        if (pointcutAttribute != null) {
                            AspectJExpressionPointcut pointcut = (AspectJExpressionPointcut) pointcutAttribute.getSource();
                            assert pointcut != null;
                            pointcut.setExpression(dataSource.getValue().getPointcut());
                            registry.registerBeanDefinition(dataSource.getKey() + "dynamicDataSourceExpressionAdvisor", beanDefinition);
                        }
                    }
                }
            }
        }

        @Override
        public void setEnvironment(Environment environment) {
            if (dynamicDataSourceProperties == null) {
                dynamicDataSourceProperties = Binder.get(environment).bind(DynamicDataSourceProperties.PREFIX, DynamicDataSourceProperties.class).get();
            }
        }
    }


}
