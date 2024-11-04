package com.lc.framework.datasource.starter.tool;

import com.lc.framework.datasource.starter.annotation.DataSourceSwitch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *     通用的数据源解析类，从对象及方法获取数据源的key
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/21 9:59
 */
@Slf4j
public class DataSourceClassResolver {

    private static boolean mpEnabled = false;
    private static Field mapperInterfaceField;

    static {
        Class<?> proxyClass = null;
        try {
            proxyClass = Class.forName("com.baomidou.mybatisplus.core.override.MybatisMapperProxy");
        } catch (ClassNotFoundException e1) {
            try {
                proxyClass = Class.forName("com.baomidou.mybatisplus.core.override.PageMapperProxy");
            } catch (ClassNotFoundException e2) {
                try {
                    proxyClass = Class.forName("org.apache.ibatis.binding.MapperProxy");
                } catch (ClassNotFoundException ignored) {
                }
            }
        }
        if (proxyClass != null) {
            try {
                mapperInterfaceField = proxyClass.getDeclaredField("mapperInterface");
                mapperInterfaceField.setAccessible(true);
                mpEnabled = true;
            } catch (NoSuchFieldException e) {
                log.warn("Failed to init mybatis-plus support.");
            }
        }
    }


    /**
     * 缓存方法上的注解
     */
    private final Map<Object, String> dsCache = new ConcurrentHashMap<>();

    /**
     * 只解析public方法
     */
    private final boolean allowedPublicOnly;

    public DataSourceClassResolver(boolean allowedPublicOnly) {
        this.allowedPublicOnly = allowedPublicOnly;
    }

    public String findKey(Method method, Object targetObject, Class<? extends Annotation> annotation) {
        // 不拦截Object原生方法
        if (method.getDeclaringClass() == Object.class) {
            return "";
        }
        Object cacheKey = new MethodClassKey(method, targetObject.getClass());
        String dsKey = this.dsCache.get(cacheKey);
        if (dsKey == null) {
            dsKey = computeDataSource(method, targetObject, annotation);
            dsCache.put(cacheKey, dsKey);
        }
        return dsKey;
    }

    /**
     * 获取方法使用的数据源
     * 查找顺序：当前方法 > 代理方法 > 接口 > 桥接方法(针对泛型) > 当前类及其父类 >  mybatis及mybatis-plus的mapper
     * @param method 目标方法
     * @param targetObject 目标对象
     * @param annotation 目标注解
     * @return 数据源key
     */
    private String computeDataSource(Method method, Object targetObject, Class<? extends Annotation> annotation) {
        if (allowedPublicOnly && !Modifier.isPublic(method.getModifiers())) {
            return "";
        }
        // 1、当前方法
        String dsKey = findDataSource(method, annotation);
        if (dsKey != null) {
            return dsKey;
        }

        // 2、获取代理对象的方法声明
        Class<?> targetClass = targetObject.getClass();
        Class<?> userClass = ClassUtils.getUserClass(targetClass);
        // JDK代理时,  获取实现类的方法声明.  method: 接口的方法, specificMethod: 实现类方法
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        dsKey = findDataSource(specificMethod, annotation);
        if (dsKey != null && ClassUtils.isUserLevelMethod(specificMethod)) {
            return dsKey;
        }

        // 3、 获取所有接口，返回第一个匹配的
        for (Class<?> interfaceClazz : ClassUtils.getAllInterfacesForClassAsSet(userClass)) {
            dsKey = findDataSource(interfaceClazz, annotation);
            if (dsKey != null) {
                return dsKey;
            }
        }

        // 4、父方法存在泛型的情况
        if (specificMethod != method) {
            // 泛型方法上的注解
            dsKey = findDataSource(method, annotation);
            if (dsKey != null) {
                return dsKey;
            }
            // 泛型方法声明类的注解
            dsKey  =findDataSource(method.getDeclaringClass(), annotation);
            if (dsKey != null && ClassUtils.isUserLevelMethod(method)) {
                return dsKey;
            }
        }
        return getDefaultDataSourceKey(targetObject, annotation);
    }

    /**
     * 默认的获取数据源的方法
     * @param target 目标对象
     * @param annotation 目标注解
     * @return 数据源的key
     */
    private String getDefaultDataSourceKey(Object target, Class<? extends Annotation> annotation) {
        Class<?> targetClass = target.getClass();
        // 如果不是代理类, 从当前类开始, 不断的找父类的声明
        if (!Proxy.isProxyClass(targetClass)) {
            Class<?> currentClass = targetClass;
            while (currentClass != Object.class) {
                String dsKey = findDataSource(currentClass, annotation);
                if (dsKey != null) {
                    return dsKey;
                }
                currentClass = currentClass.getSuperclass();
            }
        }
        // mybatis-plus, mybatis-spring 的获取方式
        if (mpEnabled) {
            final Class<?> clazz = getMapperInterfaceClass(target);
            if (clazz != null) {
                String dsKey = findDataSource(clazz, annotation);
                if (dsKey != null) {
                    return dsKey;
                }
                // 尝试从其父接口获取
                return findDataSource(clazz.getSuperclass(), annotation);
            }
        }
        return null;
    }

    private String findDataSource(AnnotatedElement ae, Class<? extends Annotation> annotation) {
        if (annotation.isAssignableFrom(DataSourceSwitch.class)) {
            DataSourceSwitch ds = AnnotatedElementUtils.findMergedAnnotation(ae, DataSourceSwitch.class);
            if (ds != null) {
                return ds.value();
            }
        }
        return null;
    }

    /**
     * 用于处理嵌套代理
     *
     * @param target JDK 代理类对象
     * @return InvocationHandler 的 Class
     */
    private Class<?> getMapperInterfaceClass(Object target) {
        Object current = target;
        while (Proxy.isProxyClass(current.getClass())) {
            Object currentRefObject = AopProxyUtils.getSingletonTarget(current);
            if (currentRefObject == null) {
                break;
            }
            current = currentRefObject;
        }
        try {
            if (Proxy.isProxyClass(current.getClass())) {
                return (Class<?>) mapperInterfaceField.get(Proxy.getInvocationHandler(current));
            }
        } catch (IllegalAccessException ignore) {
        }
        return null;
    }

}
