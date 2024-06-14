package com.lc.framework.datascope.config;

import com.lc.framework.datascope.handler.IDataScopeSqlHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.util.LambdaSafe;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *     处理所有IDataScopeSqlHandler实例的注册过程，为其执行匹配的DataScopeSqlHandlerCustomizer
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/6/5 10:21
 */
public class DataScopeSqlHandlerCustomizerBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private ListableBeanFactory beanFactory;

    private List<DataScopeSqlHandlerCustomizer<?>> customizers;

    @Override
    @SuppressWarnings("NullableProblems")
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        Assert.isInstanceOf(ListableBeanFactory.class, beanFactory,
                "WebServerCustomizerBeanPostProcessor can only be used with a ListableBeanFactory");
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof IDataScopeSqlHandler sqlHandler) {
            postProcessSqlHandlerBean(sqlHandler);
        }
        return bean;
    }

    @SuppressWarnings("unchecked")
    private void postProcessSqlHandlerBean(IDataScopeSqlHandler sqlHandler) {
        LambdaSafe.callbacks(DataScopeSqlHandlerCustomizer.class, getCustomizers(), sqlHandler)
                .withLogger(DataScopeSqlHandlerCustomizerBeanPostProcessor.class)
                .invoke((customizer) -> customizer.customize(sqlHandler));
    }

    private List<DataScopeSqlHandlerCustomizer<?>> getCustomizers() {
        if (this.customizers == null) {
            // Look up does not include the parent context
            this.customizers = new ArrayList<>(getSqlHandlerCustomizerBeans());
            this.customizers.sort(AnnotationAwareOrderComparator.INSTANCE);
            this.customizers = Collections.unmodifiableList(this.customizers);
        }
        return this.customizers;
    }

    /**
     * 从BeanFactory中获取已注册的DataScopeSqlHandlerCustomizer实现类
     *
     * @return DataScopeSqlHandlerCustomizer集合
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Collection<DataScopeSqlHandlerCustomizer<?>> getSqlHandlerCustomizerBeans() {
        return (Collection) this.beanFactory.getBeansOfType(DataScopeSqlHandlerCustomizer.class, false, false).values();
    }
}
