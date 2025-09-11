package com.lc.framework.data.permission.config;

import com.lc.framework.data.permission.handler.IDataPermissionSqlHandler;

import java.io.Serializable;

/**
 * <pre>
 * 定制sql处理器的属性。由于Lambda表达式生成的实例，在运行时会被泛型擦除，无法通过getGenericInterfaces获取运行时类型， 只能通过方法参数获取到实际类型
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-11-16 15:41
 */
@FunctionalInterface
public interface DataScopeSqlHandlerCustomizer<T extends IDataPermissionSqlHandler> extends Serializable {
    void customize(T handler);
}
