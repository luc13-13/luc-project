package com.lc.framework.excel.template;

import java.util.LinkedList;

/**
 * <pre>
 * 将横向嵌套表读取到
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-09 10:45
 */
public abstract class AbstractHorizontalMergeExcel<R, C> {
    R rootColumn;


    LinkedList<C> childColumn;
}
