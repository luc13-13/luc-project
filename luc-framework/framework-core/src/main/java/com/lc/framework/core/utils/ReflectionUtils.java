package com.lc.framework.core.utils;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 * 反射工具类，提供获取成员变量、方法、注解、构造函数、setter和getter的方法
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-03 13:51
 */
public class ReflectionUtils {

    /**
     * 获取成员变量或方法上的指定注解
     *
     * @param member    目标成员变量或方法
     * @param annoClass 需要获取的注解的Class对象
     * @param <T>       需要获取的注解
     * @author Lu Cheng
     * @create 2023/8/3
     */
    public static <T extends Annotation> T getAnnotation(Member member, Class<T> annoClass) {
        return ((AccessibleObject) member).getAnnotation(annoClass);
    }

    public static Object getFieldValue(Object obj, Field field) {
        if (field == null) {
            return null;
        } else {
            try {
                return field.get(obj);
            } catch (IllegalAccessException e) {
                try {
                    getGetterByField(obj, field).invoke(obj);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
                throw new RuntimeException(e);
            }
        }
    }

    public static Method getGetterByField(Object target, Member field) {
        Method getter;
        try {
            getter = target.getClass().getMethod(fieldNameToGetterName(field.getName()));
        } catch (NoSuchMethodException e) {
            try {
                getter = target.getClass().getMethod(fieldNameToIsName(field.getName()));
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
        return getter;
    }


    public static Method getFirstMethod(Class<?> clazz, String name) {
        return findMethod(clazz, name);
    }

    /**
     * 未传参数时，{@link Nullable}将参数编译为长度为0的数组
     *
     * @author Lu Cheng
     * @create 2023/8/3
     */
    public static Method findMethod(Class<?> clazz, String name, @Nullable Class<?>... paramTypes) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(name, "Method name must not be null");
        Class<?> searchType = clazz;
        while (searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() :
                    getDeclaredMethods(searchType, false));
            for (Method method : methods) {
                if (name.equals(method.getName()) && (paramTypes == null || hasSameParams(method, paramTypes))) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    private static boolean hasSameParams(Method method, Class<?>[] paramTypes) {
        return (paramTypes.length == method.getParameterCount() &&
                Arrays.equals(paramTypes, method.getParameterTypes()));
    }

    private static String fieldNameToGetterName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private static String fieldNameToIsName(String fieldName) {
        return "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private static Method[] getDeclaredMethods(Class<?> clazz, boolean defensive) {
        Assert.notNull(clazz, "Class must not be null");
        Method[] result;
        try {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
            if (defaultMethods != null) {
                result = new Method[declaredMethods.length + defaultMethods.size()];
                System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
                int index = declaredMethods.length;
                for (Method defaultMethod : defaultMethods) {
                    result[index] = defaultMethod;
                    index++;
                }
            } else {
                result = declaredMethods;
            }
        } catch (Throwable ex) {
            throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() +
                    "] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
        }

        return (result.length == 0 || !defensive) ? result : result.clone();
    }

    @Nullable
    private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
        List<Method> result = null;
        for (Class<?> ifc : clazz.getInterfaces()) {
            for (Method ifcMethod : ifc.getMethods()) {
                if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(ifcMethod);
                }
            }
        }
        return result;
    }
}
