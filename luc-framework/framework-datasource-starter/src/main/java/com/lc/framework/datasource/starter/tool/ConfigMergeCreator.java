package com.lc.framework.datasource.starter.tool;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <pre>
 *     将源配置类C对象转为目标配置类T对象的通用方法
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/17 11:03
 */
@Slf4j
@AllArgsConstructor
public class ConfigMergeCreator<C, T> {

    /**
     * 配置类名
     */
    private final String configName;

    /**
     * 源配置类
     */
    private final Class<C> configClazz;

    /**
     * 目标配置类
     */
    private final Class<T> targetClazz;

    /**
     * 合并源配置类的当前值与全局值，创建为目标配置类。对于相同属性名，当前配置会覆盖全局配置
     * @param global 源配置类全局值
     * @param item 源配置类当前值
     * @return 目标配置
     */
    @SneakyThrows({NoSuchMethodException.class, InvocationTargetException.class, InstantiationException.class, IllegalAccessException.class, IntrospectionException.class})
    @SuppressWarnings("unchecked")
    public T create(C global, C item) {
        if (configClazz.equals(targetClazz) && global == null) {
            return (T) item;
        }
        // 1、获取所有级别的无参构造方法，通过无参构造方法创建实例
        T result = targetClazz.getDeclaredConstructor().newInstance();
        // 2、获取当前配置的信息
        BeanInfo beanInfo = Introspector.getBeanInfo(configClazz, Object.class);
        // 3、获取所有属性的包装（包括属性名、get方法、set方法、属性类型Class）
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd: propertyDescriptors) {
            Class<?> type = pd.getPropertyType();
            if (Properties.class == type) {
                mergeProperties(global, item, result, pd);
            } else if (List.class == type) {
                mergeList(global, item, result, pd);
            } else if (Map.class == type) {
                mergeMap(global, item, result, pd);
            } else {
                mergeBasic(global, item, result, pd);
            }
        }
        return result;
    }

    /**
     * 针对属性pd类型是Properties的创建方法
     * @param global 源配置类全局值
     * @param item 源配置类当前值
     * @param result 目标配置
     * @param pd 配置类的属性
     */
    private void mergeProperties(C global, C item, T result, PropertyDescriptor pd) throws InvocationTargetException, IllegalAccessException {
        String propertyName = pd.getName();
        Method readMethod = pd.getReadMethod();
        // 获取全局属性值
        Properties globalValue = (Properties) readMethod.invoke(global);
        // 获取当前属性值
        Properties itemValue = (Properties) readMethod.invoke(item);

        // 放入全局配置
        Properties properties = new Properties();
        if (globalValue != null) {
            properties.putAll(globalValue);
        }

        // 放入当前配置
        if (itemValue != null) {
            properties.putAll(itemValue);
        }

        if (properties.size() > 0) {
            setField(result, propertyName, properties);
        }
    }

    /**
     * 当属性为List时的赋值方法
     * @param global 源配置类全局值
     * @param item 源配置类当前值
     * @param result 目标配置
     * @param propertyDescriptor 需要修改的属性
     */
    @SneakyThrows({IllegalAccessException.class, ReflectiveOperationException.class})
    @SuppressWarnings("unchecked")
    private void mergeList(C global, C item, T result, PropertyDescriptor propertyDescriptor) {
        Method readMethod = propertyDescriptor.getReadMethod();
        List<Object> resultValue = new ArrayList<>();
        List<Object> value = (List<Object>) readMethod.invoke(item);
        if (value == null) {
            value = (List<Object>) readMethod.invoke(global);
        }
        if (value != null) {
            resultValue.addAll(value);
        }
        setField(result, propertyDescriptor.getName(), resultValue);
    }

    /**
     * 当属性为Map时的赋值方法
     * @param global 源配置类全局值
     * @param item 源配置类当前值
     * @param result 目标配置
     * @param propertyDescriptor 需要修改的属性
     */
    @SneakyThrows({IllegalAccessException.class, ReflectiveOperationException.class})
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void mergeMap(C global, C item, T result, PropertyDescriptor propertyDescriptor) {
        Method readMethod = propertyDescriptor.getReadMethod();
        Map resultValue = new HashMap(8);
        Map value = (Map) readMethod.invoke(item);
        if (value == null) {
            value = (Map) readMethod.invoke(global);
        }
        if (value != null) {
            resultValue.putAll(value);
        }
        setField(result, propertyDescriptor.getName(), resultValue);
    }

    /**
     * 当属性为List时的赋值方法
     * @param global 源配置类全局值
     * @param item 源配置类当前值
     * @param result 目标配置
     * @param propertyDescriptor 需要修改的属性
     */
    @SneakyThrows({IllegalAccessException.class, ReflectiveOperationException.class})
    private void mergeBasic(C global, C item, T result, PropertyDescriptor propertyDescriptor) {
        // 属性名称
        String pdName = propertyDescriptor.getName();
        // 获取get方法
        Method readMethod = propertyDescriptor.getReadMethod();
        // 优先从当前属性取值
        Object itemValue = readMethod.invoke(item);
        if (itemValue == null) {
            // 当前属性为空再从全局属性取值
            itemValue = readMethod.invoke(global);
        }
        // 将属性值设置给结果对象
        setField(result, pdName, itemValue);
    }

    /**
     * 为目标配置属性值
     * @param result 目标配置
     * @param propertyName 属性名
     * @param value 属性值
     */
    private void setField(T result, String propertyName, Object value) {
        try {
            // 创建目标对象属性包装
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyName, targetClazz);
            Method writeMethod = propertyDescriptor.getWriteMethod();
            writeMethod.invoke(result, value);
        } catch (IntrospectionException | ReflectiveOperationException e) {
            // 无法通过set方法赋值时，尝试通过获取属性直接赋值
            Field field = null;
            try {
                field = targetClazz.getDeclaredField(propertyName);
                field.setAccessible(true);
                field.set(result, value);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                log.warn("dynamic-datasource set {} [{}] failed, check your config {} or update it to the latest version", configName, propertyName, targetClazz.getName());
            } finally {
                // 恢复属性的私有性
                if (field != null) {
                    field.setAccessible(false);
                }
            }
        } catch (Exception e) {
            log.warn("dynamic-datasource set {} [{}] failed, check your config {} or update it to the latest version", configName, propertyName, targetClazz.getName(), e);
        }
    }
}
