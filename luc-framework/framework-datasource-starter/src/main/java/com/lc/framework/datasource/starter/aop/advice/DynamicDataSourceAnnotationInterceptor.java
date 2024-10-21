package com.lc.framework.datasource.starter.aop.advice;

import com.lc.framework.datasource.starter.tool.DataSourceClassResolver;
import com.lc.framework.datasource.starter.tool.DynamicDataSourceContextHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;

/**
 * <pre>
 *  基于注解的环绕通知Advice
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 17:11
 */
public class DynamicDataSourceAnnotationInterceptor implements MethodInterceptor {

    private final Class<? extends Annotation> targetAnnotation;

    private final DataSourceClassResolver dataSourceClassResolver;

    public DynamicDataSourceAnnotationInterceptor(Class<? extends Annotation> targetAnnotation, DataSourceClassResolver dataSourceClassResolver) {
        this.targetAnnotation = targetAnnotation;
        this.dataSourceClassResolver = dataSourceClassResolver;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String key = this.determineDataSourceKey(invocation);
        DynamicDataSourceContextHolder.push(key);
        try {
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }

    private String determineDataSourceKey(MethodInvocation invocation) {
        return dataSourceClassResolver.findKey(invocation.getMethod(), invocation.getThis(), targetAnnotation);
    }
}
