
package com.lc.framework.datasource.starter.config;

import com.lc.framework.datasource.starter.annotation.DataSourceSwitch;
import com.lc.framework.datasource.starter.aop.DynamicDataSourceAnnotationAdvisor;
import com.lc.framework.datasource.starter.aop.DynamicDataSourceExpressionAdvisor;
import com.lc.framework.datasource.starter.aop.advice.DynamicDataSourceAnnotationInterceptor;
import com.lc.framework.datasource.starter.aop.advice.DynamicDataSourceExpressionInterceptor;
import com.lc.framework.datasource.starter.properties.DataSourceProperty;
import com.lc.framework.datasource.starter.properties.DynamicDataSourceProperties;
import com.lc.framework.datasource.starter.tool.DataSourceClassResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

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
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
@Import(DynamicDataSourceAopConfiguration.DynamicDataSourceExpressionAdvisorRegistrar.class)
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
        log.info("DynamicDataSourceAnnotationAdvisor is created successfully, annotation：{}", DataSourceSwitch.class.getName());
        return new DynamicDataSourceAnnotationAdvisor(advice, DataSourceSwitch.class);
    }

    /**
     * 基于表达式的切面
     */
    @Slf4j
    @ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX + ".aop", name = "expression-enabled", havingValue = "true")
    public static class DynamicDataSourceExpressionAdvisorRegistrar implements ImportBeanDefinitionRegistrar , EnvironmentAware  {

        private Environment environment;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            DynamicDataSourceProperties properties = Binder.get(environment).bind(DynamicDataSourceProperties.PREFIX, DynamicDataSourceProperties.class).get();
            boolean registrySuccess = false;
            if (properties != null && properties.getDatasource() != null) {
                String dataSourceKey;
                DataSourceProperty property;
                for (Map.Entry<String, DataSourceProperty> entry : properties.getDatasource().entrySet()) {
                    dataSourceKey = entry.getKey();
                    property = entry.getValue();
                    if (StringUtils.hasText(property.getPointcut())) {
                        // bean definition
                        RootBeanDefinition beanDefinition = new RootBeanDefinition();
                        beanDefinition.setBeanClass(DynamicDataSourceExpressionAdvisor.class);
                        // pointcut
                        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                        pointcut.setExpression(property.getPointcut());
                        // advice
                        DynamicDataSourceExpressionInterceptor advise = new DynamicDataSourceExpressionInterceptor(dataSourceKey);
                        // constructor
                        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
                        constructorArgumentValues.addIndexedArgumentValue(0, advise);
                        constructorArgumentValues.addIndexedArgumentValue(1, pointcut);
                        beanDefinition.setConstructorArgumentValues(constructorArgumentValues);
                        // registry
                        registry.registerBeanDefinition(dataSourceKey + "DynamicDataSourceExpressionAdvisor", beanDefinition);
                        log.info("DynamicDataSourceExpressionAdvisor is created successfully, dataSourceKey：{}，expression：{}", dataSourceKey, property.getPointcut());
                        registrySuccess = true;
                    }
                }
            }
            if (!registrySuccess) {
                throw new NullPointerException(DynamicDataSourceProperties.PREFIX + ".datasource is null, check pointcut");
            }
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }
    }
}
