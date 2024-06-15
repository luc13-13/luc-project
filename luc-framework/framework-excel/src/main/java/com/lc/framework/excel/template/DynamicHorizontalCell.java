package com.lc.framework.excel.template;

import lombok.Data;

import java.util.List;

/**
 * <pre>
 * 定义嵌套数据的数据格式， 所有需要
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-09 09:48
 */
@Data
public class DynamicHorizontalCell {
    Object data;

    int rowStart;

    int rowEnd;

    int parentRowStart;

    int parentRowEnd;
    List<DynamicHorizontalCell> childrenColumn;
}
