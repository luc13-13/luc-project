package com.lc.framework.data.permission.config;

import com.lc.framework.data.permission.handler.IDataPermissionSqlHandler;
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
 *     由于DataScopeSqlHandlerCustomizer是FunctionalInterface，通过Lambda方式创建实例时，会被泛型擦除，导致无法获取内部类，无法判断要为IDataScopeSqlHandler执行哪一个Customizer
 *     因此创建BeanPostProcessor和LambdaSafe.callBack()方法，干预所有IDataScopeSqlHandler实例的注册过程，为每个实例执行能够匹配的DataScopeSqlHandlerCustomizer
 *     LambdaSafe.callBack方法内部捕捉了类型转换异常，对无法匹配的Handler和Customizer无法匹配的情况能够跳过执行customize方法
 * Examples:
 *  {@code
 *  @Configuration
 *  public class DataScopeAutoConfig {
 *      // 这里每个Customizer实例的具体类型为lambda，无法判断应该用到哪个具体的Handler上
 *      @Autowired
 *      List<DataScopeSqlHandlerCustomizer> customizers;
 *
 *      // 这里每个handler实例可以获取到类型，但是无法与customizers进行匹配
 *      @Bean
 *      public DataScopeInterceptor dataScopeInterceptor(List<IDataScopeHandler) {
 *          return new SysDataScopeSqlHandler()
 *      }
 *
 *  }
 *
 *  }
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
        if (bean instanceof IDataPermissionSqlHandler sqlHandler) {
            postProcessSqlHandlerBean(sqlHandler);
        }
        return bean;
    }

    @SuppressWarnings("unchecked")
    private void postProcessSqlHandlerBean(IDataPermissionSqlHandler sqlHandler) {
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
