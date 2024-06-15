package com.lc.framework.excel.anno;

import java.lang.annotation.*;

/**
 * <pre>
 *  用于标注动态单元格的注解，按照index为最小的字段为所有行排序
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-09 14:16
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DynamicMerged {

    int index() default -1;

    int rowStart() default 1;

    WriteHandlerStrategyType strategy() default WriteHandlerStrategyType.MERGE_INDEPENDENT;
}
