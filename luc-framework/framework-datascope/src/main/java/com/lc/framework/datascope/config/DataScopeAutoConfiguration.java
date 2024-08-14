package com.lc.framework.datascope.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusLanguageDriverAutoConfiguration;
import com.lc.framework.datascope.entity.DataScopeProperties;
import com.lc.framework.datascope.entity.HandlerDefinition;
import com.lc.framework.datascope.entity.SupportTableDefinition;
import com.lc.framework.datascope.handler.IDataScopeSqlHandler;
import com.lc.framework.datascope.handler.SysRoleDataScopeSqlHandler;
import com.lc.framework.datascope.handler.SysUserDataScopeSqlHandler;
import com.lc.framework.datascope.handler.TenantDataScopeSqlHandler;
import com.lc.framework.datascope.interceptor.DataScopeInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import java.util.*;
import java.util.stream.Collectors;
import static com.lc.framework.core.constants.StringConstants.DOT;


/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-04 10:15
 */
@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class, MybatisPlusLanguageDriverAutoConfiguration.class})
@EnableConfigurationProperties(DataScopeProperties.class)
@Import(DataScopeAutoConfiguration.CustomizerBeanPostProcessorRegistry.class)
@Slf4j
public class DataScopeAutoConfiguration {

    @Autowired
    private List<DataScopeSqlHandlerCustomizer> customizers;

    /**
     * @param handlers 所有handler的单例bean， beanName必须为类名首字母小写
     * @author Lu Cheng
     * @date 2023/11/20
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "data-scope.enabled", havingValue = "true")
    public DataScopeInterceptor dataScopeInterceptor(DataScopeProperties dataScopeProperties,
                                                     List<IDataScopeSqlHandler> handlers) {
        // 所有处理器支持的表名， 采用全限定名 database.tableName
        Set<String> tableNames = new HashSet<>();
        // spring容器中已有的handler
        Map<Class<? extends IDataScopeSqlHandler>, IDataScopeSqlHandler> registeredHandler = new HashMap<>();
        // 只有在配置文件中注册了handler的表信息时，才将handler注册到拦截器中
        if (!CollectionUtils.isEmpty(dataScopeProperties.getHandlerDefinition())) {
            Map<String, ? extends IDataScopeSqlHandler> handlerMap = handlers.stream().collect(Collectors.toMap(IDataScopeSqlHandler::getName, it -> it));
            IDataScopeSqlHandler handler;
            for (HandlerDefinition definition : dataScopeProperties.getHandlerDefinition()) {
                // 允许只通过配置文件注册实例bean，而不定义SupportTableDefinition
                handler = handlerMap.get(definition.getId());
                if (!CollectionUtils.isEmpty(definition.getSupportTables())) {
                    for (SupportTableDefinition table : definition.getSupportTables()) {
                        handler.bindTable(table);
                        tableNames.add(table.getDatabase() + DOT + table.getTableName());
                    }
                }
                registeredHandler.put(handler.getClass(), handler);
            }
        }
        return new DataScopeInterceptor(tableNames, registeredHandler);
    }

    @Bean("sysRoleDataScopeSqlHandler")
    @ConditionalOnMissingBean
    public SysRoleDataScopeSqlHandler sysRoleDataScopeSqlHandler() {
        return new SysRoleDataScopeSqlHandler();
    }

    @Bean("sysUserDataScopeSqlHandler")
    @ConditionalOnMissingBean
    public SysUserDataScopeSqlHandler sysUserDataScopeSqlHandler() {
        return new SysUserDataScopeSqlHandler();
    }

    @Bean("tenantDataScopeSqlHandler")
    @ConditionalOnMissingBean
    public TenantDataScopeSqlHandler tenantDataScopeSqlHandler() {
        return new TenantDataScopeSqlHandler();
    }

    /**
     * 注册DataScopeSqlHandlerCustomizerBeanPostProcessor
     */
    public static class CustomizerBeanPostProcessorRegistry implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

        private ConfigurableListableBeanFactory beanFactory;

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            if (beanFactory instanceof ConfigurableListableBeanFactory listableBeanFactory) {
                this.beanFactory = listableBeanFactory;
            }
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
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
