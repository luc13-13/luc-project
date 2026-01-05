package com.lc.framework.data.permission.config;

import com.lc.framework.data.permission.entity.DataPermissionProperties;
import com.lc.framework.data.permission.entity.HandlerDefinition;
import com.lc.framework.data.permission.handler.IDataPermissionSqlHandler;
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
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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
@Slf4j
@ConditionalOnMissingBean(LucDataPermissionInterceptor.class)
@AutoConfigureBefore(name = {"com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration",
        "com.baomidou.mybatisplus.autoconfigure.MybatisPlusInnerInterceptorAutoConfiguration"})
@AutoConfigureAfter(value = {DataSourceAutoConfiguration.class}, name = "com.baomidou.mybatisplus.autoconfigure.MybatisPlusLanguageDriverAutoConfiguration")
@EnableConfigurationProperties(DataPermissionProperties.class)
@Import({DataScopeAutoConfiguration.CustomizerBeanPostProcessorRegistry.class})
public class DataScopeAutoConfiguration {

    /**
     * @param handlers 所有handler的单例bean， beanName必须为类名首字母小写
     * @author Lu Cheng
     * @date 2023/11/20
     */
    @Bean
    @ConditionalOnBean(IDataPermissionSqlHandler.class)
    @ConditionalOnProperty(name = "data-permission.enabled", havingValue = "true", matchIfMissing = true)
    public LucDataPermissionInterceptor dataScopeInterceptor(DataPermissionProperties dataPermissionProperties,
                                                             List<IDataPermissionSqlHandler> handlers) {
        log.info("注册LucDataPermissionInterceptor");
        // spring容器中已有的handler
        // 只有在配置文件中注册了handler的表信息时，才将handler注册到拦截器中
        if (!CollectionUtils.isEmpty(dataPermissionProperties.getHandlerDefinition())) {
            Map<String, ? extends IDataPermissionSqlHandler> handlerMap = handlers.stream().collect(Collectors.toMap(IDataPermissionSqlHandler::getName, it -> it));
            for (HandlerDefinition definition : dataPermissionProperties.getHandlerDefinition()) {
                // 允许只通过配置文件注册实例bean，而不定义SupportTableDefinition
                if (!CollectionUtils.isEmpty(definition.getSupportTables())) {
                    definition.getSupportTables().forEach((database, tables) -> tables.forEach(table -> {
                        if (!dataPermissionProperties.isIgnoreDatabaseName()) {
                            table.setDatabase(database);
                        }
                        handlerMap.computeIfPresent(definition.getId(), (k, handlerBean) -> {
                            handlerBean.bindTable(table);
                            return handlerBean;
                        });
                    }));
                }
            }
        }
        return new LucDataPermissionInterceptor(handlers.stream().collect(Collectors.toMap(IDataPermissionSqlHandler::getClass, v -> v, (k1, k2) -> k1)));
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
