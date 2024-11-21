package com.lc.framework.datasource.starter.tool;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     动态数据源配置相关的工具类
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 10:09
 */
public class DsConfigUtil {
    private static final Pattern LINE_PATTERN = Pattern.compile("-(\\w)");

    /**
     * 横划线转驼峰
     *
     * @param str 原字符串
     * @return 转换后的字符串
     */
    public static String lineToUpper(String str) {
        Matcher matcher = LINE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static Map<String, PropertyDescriptor> getPropertyDescriptorMap(Class<?> clazz) {
        Map<String, PropertyDescriptor> methodMap = new HashMap<>(16);
        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
                if (!"class".equals(pd.getName())) {
                    methodMap.put(pd.getName(), pd);
                }
            }
        } catch (Exception ignore) {
        }
        return methodMap;
    }

    /**
     * 通过clazz获取对应的setter方法
     *
     * @param clazz 类
     * @return setter方法
     */
    public static Map<String, Method> getSetterMethods(Class<?> clazz) {
        Map<String, Method> methodMap = new HashMap<>(16);
        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
                Method method = pd.getWriteMethod();
                if (method != null) {
                    methodMap.put(pd.getName(), method);
                }
            }
        } catch (Exception ignore) {
        }
        return methodMap;
    }

    /**
     * 将需要传入invoke方法的值转换成方法对应的类型
     *
     * @param method 方法
     * @param value  值
     * @return 对应值
     */
    public static Object convertValue(Method method, Object value) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1) {
            Class<?> parameterType = parameterTypes[0];
            String propertyValue = String.valueOf(value);
            if (parameterType == String.class) {
                return propertyValue;
            }
            if (parameterType == Integer.class || parameterType == int.class) {
                return Integer.valueOf(propertyValue);
            }
            if (parameterType == Long.class || parameterType == long.class) {
                return Long.valueOf(propertyValue);
            }
            if (parameterType == Boolean.class || parameterType == boolean.class) {
                return Boolean.valueOf(propertyValue);
            }
            if (parameterType == Double.class || parameterType == double.class) {
                return Double.valueOf(propertyValue);
            }
            if (parameterType == Float.class || parameterType == float.class) {
                return Float.valueOf(propertyValue);
            }
        }
        return value;
    }
}
