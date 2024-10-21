package com.lc.framework.datasource.starter.aop.pointcut;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.IntroductionAwareMethodMatcher;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/21 15:48
 */
public class DelegatedExpressionPointcut implements Pointcut, BeanFactoryAware, InitializingBean, ClassFilter, IntroductionAwareMethodMatcher {

    private final List<Pointcut> delegatedPointcuts;

    public DelegatedExpressionPointcut(List<Pointcut> delegatedPointcuts) {
        this.delegatedPointcuts = delegatedPointcuts;
    }

    @Override
    public ClassFilter getClassFilter() {
        return null;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (delegatedPointcuts != null) {
            for (Pointcut pointcut : delegatedPointcuts) {
                if (pointcut instanceof BeanFactoryAware) {
                    ((BeanFactoryAware) pointcut).setBeanFactory(beanFactory);
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 根据Pointcut的order重新排序
    }

    @Override
    public boolean matches(Class<?> clazz) {
        return false;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass, boolean hasIntroductions) {
        return false;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return false;
    }

    @Override
    public boolean isRuntime() {
        return false;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        return false;
    }
}
