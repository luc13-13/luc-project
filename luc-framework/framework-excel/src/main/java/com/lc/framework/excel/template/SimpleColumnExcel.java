package com.lc.framework.excel.template;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-09 09:29
 */
@Data
public class SimpleColumnExcel {

    private DynamicHorizontalCell cell = new DynamicHorizontalCell();
    @ExcelProperty(value = "姓名", index = 1)
    private String name;

    @ExcelProperty(value = "年龄", index = 2)
    private String age;

    @ExcelProperty(value = "性别", index = 3)
    private String sex;

    public void test() {
        Object data = cell.data;
    }
}
