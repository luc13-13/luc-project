package com.lc.framework.datasource.starter.aop.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Map;

/**
 * <pre>
 *     基于表达式的环绕通知, 封装配置文件中的数据源key和pointcut表达式
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/18 16:28
 */
public class DynamicDataSourceExpressionInterceptor implements MethodInterceptor {

    private Map<String, String> dataSourceExpressionMap;
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return null;
    }
}
