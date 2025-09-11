package com.lc.framework.data.permission.anno;

import com.lc.framework.data.permission.handler.IDataPermissionSqlHandler;

import java.lang.annotation.*;

/**
 * <pre>
 *     权限控制注解，用于定义sql中相关权限表和权限字段的别名
 * </pre>
 * @author Lu Cheng
 * @create 2023-07-31 16:27
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataScope {

     DataColumn[] value() default {};

     /**
      * 数据权限的处理器，同一个方法上支持多个处理器，每个处理器返回的Expression进行拼接
      *
      * @author Lu Cheng
      * @date 2023/11/17
      */
     Class<? extends IDataPermissionSqlHandler>[] rules() default {};

     /**
      * 各个权限处理器的Expression拼接关系，
      * true表示将Expression按照And拼接，
      * false表示将Expression按照Or拼接
      *
      * @author Lu Cheng
      * @date 2023/11/17
      */
     boolean isMutex() default false;
}