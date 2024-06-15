package com.lc.framework.excel.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.lc.framework.excel.factory.CellWriterHandlerFactory;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-09 14:23
 */
@Component
public class EasyExcelUtils {
    // todo: 项目完成后自动注入属性即可
    private static CellWriterHandlerFactory cellWriterHandlerFactory;

    public static <T> void export(Class<T> clazz, List<T> dataList, String filename) {
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(filename, clazz);
        doWrite(clazz, dataList, excelWriterBuilder);
    }

    public static <T> void export(Class<T> clazz, List<T> dataList, String filename, HttpServletResponse response) {
        try {
            // 创建builder
            ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(response.getOutputStream(), clazz);
            // 将处理器注册的操作统一封装
            doWrite(clazz, dataList, excelWriterBuilder);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    private static <T> void doWrite(Class<T> clazz, List<T> dataList, ExcelWriterBuilder excelWriterBuilder) {
        // 为builder封装处理器
        cellWriterHandlerFactory.registerHandlers(excelWriterBuilder, clazz, dataList);
        // 将流的自动关闭设为false， 防止写丢失
        excelWriterBuilder = excelWriterBuilder.autoCloseStream(Boolean.FALSE);
        // 开始写入到流中
        excelWriterBuilder.writeExcelOnException(true).sheet().doWrite(dataList);
    }

    public CellWriterHandlerFactory getCellWriterHandlerFactory() {
        return cellWriterHandlerFactory;
    }

    @Autowired
    public void setCellWriterHandlerFactory(CellWriterHandlerFactory factory) {
        EasyExcelUtils.cellWriterHandlerFactory = factory;
    }

}
