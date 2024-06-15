package com.lc.framework.excel.anno;

import com.alibaba.excel.write.handler.CellWriteHandler;

import java.lang.annotation.*;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-14 09:52
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface WriteHandlerStrategy {
    Class<? extends CellWriteHandler>[] value() default {};
}
