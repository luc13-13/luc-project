package com.lc.framework.datasource.starter.aop.advice;

import com.lc.framework.datasource.starter.tool.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * <pre>
 *     基于表达式的环绕通知, 封装配置文件中的数据源key和pointcut表达式
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/18 16:28
 */
@Slf4j
public class DynamicDataSourceExpressionInterceptor implements MethodInterceptor {

    private final String datasourceKey;

    public DynamicDataSourceExpressionInterceptor(String datasourceKey) {
        this.datasourceKey = datasourceKey;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        DynamicDataSourceContextHolder.push(datasourceKey);
        log.info("基于表达式的通知：执行方法{}, 切换至{}", invocation.getMethod().getName(), datasourceKey);
        try {
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.poll();
            log.info("基于表达式的通知: 方法{}执行完毕, 剔除{}", invocation.getMethod().getName(), datasourceKey);
        }
    }
}
