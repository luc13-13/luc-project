package com.lc.framework.datasource.starter.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/23 17:24
 */
public class DynamicDataSourceExpressionAdvisor implements PointcutAdvisor {

    private final Advice advice;

    private final Pointcut pointcut;

    public DynamicDataSourceExpressionAdvisor(Advice advice, Pointcut pointcut) {
        this.advice = advice;
        this.pointcut = pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }
}
