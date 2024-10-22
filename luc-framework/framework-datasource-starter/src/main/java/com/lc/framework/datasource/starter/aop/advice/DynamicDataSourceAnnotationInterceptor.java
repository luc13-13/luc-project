package com.lc.framework.datasource.starter.aop.advice;

import com.lc.framework.datasource.starter.annotation.DataSourceSwitch;
import com.lc.framework.datasource.starter.tool.DataSourceClassResolver;
import com.lc.framework.datasource.starter.tool.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;

/**
 * <pre>
 *  基于注解的环绕通知Advice，根据{@link DataSourceSwitch#value()}取值，存入本地线程
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 17:11
 */
@Slf4j
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
        log.info("基于注解的通知：执行方法{}, 切换至{}", invocation.getMethod().getName(), key);
        try {
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.poll();
            log.info("基于注解的通知: 方法{}执行完毕, 剔除{}", invocation.getMethod().getName(), key);
        }
    }

    private String determineDataSourceKey(MethodInvocation invocation) {
        return dataSourceClassResolver.findKey(invocation.getMethod(), invocation.getThis(), targetAnnotation);
    }
}
