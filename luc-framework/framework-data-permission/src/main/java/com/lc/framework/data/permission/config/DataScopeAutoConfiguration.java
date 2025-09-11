package com.lc.framework.data.permission.config;

import com.lc.framework.data.permission.entity.DataScopeProperties;
import com.lc.framework.data.permission.entity.HandlerDefinition;
import com.lc.framework.data.permission.entity.SupportTableDefinition;
import com.lc.framework.data.permission.handler.IDataPermissionSqlHandler;
import com.lc.framework.data.permission.handler.SysRoleDataPermissionSqlHandler;
import com.lc.framework.data.permission.handler.TenantDataPermissionSqlHandler;
import com.lc.framework.data.permission.interceptor.LucDataPermissionInterceptor;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-04 10:15
 */
@AutoConfigureBefore(name = "com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration")
@AutoConfigureAfter(value = {DataSourceAutoConfiguration.class}, name = "com.baomidou.mybatisplus.autoconfigure.MybatisPlusLanguageDriverAutoConfiguration")
@EnableConfigurationProperties(DataScopeProperties.class)
@Import(DataScopeAutoConfiguration.CustomizerBeanPostProcessorRegistry.class)
@Slf4j
public class DataScopeAutoConfiguration {

    /**
     * @param handlers 所有handler的单例bean， beanName必须为类名首字母小写
     * @author Lu Cheng
     * @date 2023/11/20
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(IDataPermissionSqlHandler.class)
    @ConditionalOnProperty(name = "data-permission.enabled", havingValue = "true", matchIfMissing = true)
    public LucDataPermissionInterceptor dataScopeInterceptor(DataScopeProperties dataScopeProperties,
                                                             List<IDataPermissionSqlHandler> handlers) {
        // spring容器中已有的handler
        Map<Class<? extends IDataPermissionSqlHandler>, IDataPermissionSqlHandler> registeredHandler = new HashMap<>();
        // 只有在配置文件中注册了handler的表信息时，才将handler注册到拦截器中
        if (!CollectionUtils.isEmpty(dataScopeProperties.getHandlerDefinition())) {
            Map<String, ? extends IDataPermissionSqlHandler> handlerMap = handlers.stream().collect(Collectors.toMap(IDataPermissionSqlHandler::getName, it -> it));
            IDataPermissionSqlHandler handler;
            for (HandlerDefinition definition : dataScopeProperties.getHandlerDefinition()) {
                // 允许只通过配置文件注册实例bean，而不定义SupportTableDefinition
                handler = handlerMap.get(definition.getId());
                if (!CollectionUtils.isEmpty(definition.getSupportTables())) {
                    for (SupportTableDefinition table : definition.getSupportTables()) {
                        handler.bindTable(table);
                    }
                }
                registeredHandler.put(handler.getClass(), handler);
            }
        }
        return new LucDataPermissionInterceptor(registeredHandler);
    }

    @Bean("sysRoleDataPermissionSqlHandler")
    @ConditionalOnMissingBean
    public SysRoleDataPermissionSqlHandler sysRoleDataScopeSqlHandler() {
        return new SysRoleDataPermissionSqlHandler();
    }

     @Bean("tenantDataPermissionSqlHandler")
    @ConditionalOnMissingBean
    public TenantDataPermissionSqlHandler tenantDataScopeSqlHandler() {
        return new TenantDataPermissionSqlHandler();
    }

    /**
     * 注册DataScopeSqlHandlerCustomizerBeanPostProcessor
     */
    public static class CustomizerBeanPostProcessorRegistry implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

        private ConfigurableListableBeanFactory beanFactory;

        @Override
        public void setBeanFactory(@Nonnull BeanFactory beanFactory) throws BeansException {
            if (beanFactory instanceof ConfigurableListableBeanFactory listableBeanFactory) {
                this.beanFactory = listableBeanFactory;
            }
        }

        @Override
        public void registerBeanDefinitions(@Nonnull AnnotationMetadata importingClassMetadata,
                                            @Nonnull BeanDefinitionRegistry registry,
                                            @Nonnull BeanNameGenerator importBeanNameGenerator) {
            registerSyntheticBeanIfMissing(registry, "dataScopeSqlHandlerCustomizerBeanPostProcessor",
                    DataScopeSqlHandlerCustomizerBeanPostProcessor.class);
        }

        @SuppressWarnings("SameParameterValue")
        private <T> void registerSyntheticBeanIfMissing(BeanDefinitionRegistry registry, String name,
                                                        Class<T> beanClass) {
            if (ObjectUtils.isEmpty(this.beanFactory.getBeanNamesForType(beanClass, true, false))) {
                log.info("向容器注册bean：{}, class:{}", name, beanClass.getName());
                RootBeanDefinition beanDefinition = new RootBeanDefinition(beanClass);
                beanDefinition.setSynthetic(true);
                registry.registerBeanDefinition(name, beanDefinition);
            }
        }
    }
}
