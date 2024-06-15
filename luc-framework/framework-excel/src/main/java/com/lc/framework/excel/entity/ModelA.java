package com.lc.framework.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.lc.framework.excel.anno.DynamicMerged;
import com.lc.framework.excel.anno.WriteHandlerStrategy;
import com.lc.framework.excel.handler.LoopDynamicMergeStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-09 14:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
@WriteHandlerStrategy({LongestMatchColumnWidthStyleStrategy.class, LoopDynamicMergeStrategy.class})
public class ModelA {
    @ExcelProperty
    @DynamicMerged(index = 0, rowStart = 1)
    private String orderId;

    @ExcelProperty
    @DynamicMerged(index = 1, rowStart = 1)
    private String instanceId;

    @ExcelProperty
    @DynamicMerged(index = 2, rowStart = 1)
    private String flavorId;

    @ExcelProperty
    @DynamicMerged(index = 3, rowStart = 1)
    private String operateProperty;

    @ExcelProperty
    private String source;

    @ExcelProperty
    private String current;
}
