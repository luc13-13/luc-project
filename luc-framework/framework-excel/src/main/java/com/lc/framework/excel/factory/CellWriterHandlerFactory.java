package com.lc.framework.excel.factory;

import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.WriteHandler;
import com.lc.framework.core.utils.ReflectionUtils;
import com.lc.framework.excel.anno.WriteHandlerStrategy;
import com.lc.framework.excel.anno.WriteHandlerStrategyType;
import com.lc.framework.excel.anno.DynamicMerged;
import com.lc.framework.excel.handler.LoopDynamicMergeStrategy;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiFunction;

/**
 * <pre>
 * CellWriteHandler创建工厂， 根据实体类上注解{@link WriteHandlerStrategy}规定的内容， 向ExcelWriterBuilder中注册处理器
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-10 17:06
 */
@Component
public class CellWriterHandlerFactory implements InitializingBean {
    public Map<Class<? extends CellWriteHandler>, BiFunction<Class, List, ? extends CellWriteHandler>> handlerMap = new HashMap<>();

    public <T> void registerHandlers(ExcelWriterBuilder excelWriterBuilder, Class<T> clazz, @Nullable List<T> dataList) {
        for (WriteHandler handler : getHandlers(clazz, dataList)) {
            excelWriterBuilder = excelWriterBuilder.registerWriteHandler(handler);
        }
    }

    public <T> List<WriteHandler> getHandlers(Class<T> clazz, @Nullable List<T> dataList) {
        WriteHandlerStrategy strategyAnno = clazz.getDeclaredAnnotation(WriteHandlerStrategy.class);
        if (strategyAnno == null) {
            return Collections.emptyList();
        }
        List<WriteHandler> handlers = new ArrayList<>();
        Class<? extends CellWriteHandler>[] handlerClass = strategyAnno.value();
        for (Class<? extends CellWriteHandler> handler : handlerClass) {
            if (handlerMap.containsKey(handler)) {
                handlers.add(handlerMap.get(handler).apply(clazz, dataList));
            }
        }
        return handlers;
    }

    // 注册动态合并单元格的处理器
    public <T> LoopDynamicMergeStrategy buildLoopDynamicMergeStrategy(Class<T> clazz, List<T> dataList) {
        Map<Integer, Integer> rowGroup = new HashMap<>();
        // 获取所有被注解 DynamicMerged标注的属性, 并按照注解标记的索引号排序
        LinkedList<Field> targetFieldList = new LinkedList<>();
        DynamicMerged dynamicMergedAnno;
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(DynamicMerged.class)) {
                dynamicMergedAnno = ReflectionUtils.getAnnotation(field, DynamicMerged.class);
                targetFieldList.add(field);
                // 注解中需要设置带合并的列以及数据的起始行
                rowGroup.put(dynamicMergedAnno.index(), dynamicMergedAnno.rowStart());
            }
        }
        targetFieldList.sort(Comparator.comparingInt(a -> a.getAnnotation(DynamicMerged.class).index()));
        Map<Integer, Queue<Integer>> columnGroup = convertData2Dynamic(targetFieldList, dataList);
        return new LoopDynamicMergeStrategy(columnGroup, rowGroup);
    }

    public <T> Map<Integer, Queue<Integer>> convertData2Dynamic(LinkedList<Field> targetFieldList, List<T> targetList) {
        long startTime = System.currentTimeMillis();
        Map<Integer, Queue<Integer>> columnGroup = new HashMap<>();
        // 存储所有未处理的子表
        Queue<List<T>> unhandledList = new LinkedList<>();
        List<T> tempList;
        unhandledList.add(targetList);
        int rowStart, rowEnd;
        T rowStartData, rowEndData;
        Object rowStartObject;
        for (Field field : targetFieldList) {
            Queue<Integer> mergedRowNumber = new LinkedList<>();
            Queue<List<T>> unhandledChildren = new LinkedList<>();
            while (!unhandledList.isEmpty()) {
                tempList = unhandledList.poll();
                rowStart = 0;
                if (tempList.size() > 1) {
                    while (rowStart < tempList.size()) {
                        List<T> childList = new ArrayList<>();
                        rowEnd = rowStart + 1;
                        if (rowEnd < tempList.size()) {
                            rowStartData = tempList.get(rowStart);
                            rowStartObject = ReflectionUtils.getFieldValue(tempList.get(rowStart), field);
                            childList.add(rowStartData);
                            while (rowEnd < tempList.size() && rowStartObject.equals(ReflectionUtils.getFieldValue(tempList.get(rowEnd), field))) {
                                rowEndData = tempList.get(rowEnd);
                                childList.add(rowEndData);
                                // 向后移动指针
                                rowEnd++;
                            }
                            // 跳出循环是说明当前单元格连续性被打断
                        }
                        // 连续行数超过1时，才需要进行后续的构建
                        unhandledChildren.add(childList);
                        mergedRowNumber.add(rowEnd - rowStart);
                        // 根据rowEnd跳表
                        rowStart = rowEnd;
                    }
                }
            }
            if (!CollectionUtils.isEmpty(unhandledChildren)) {
                unhandledList.addAll(unhandledChildren);
            }
            columnGroup.put(ReflectionUtils.getAnnotation(field, DynamicMerged.class).index(), mergedRowNumber);
        }
        System.out.println("处理数据结构耗时：" + (System.currentTimeMillis() - startTime) + " ms");
        return columnGroup;
    }

    @Override
    public void afterPropertiesSet() {
        handlerMap.put(WriteHandlerStrategyType.MERGE_INDEPENDENT.getStrategyClazz(), this::buildLoopDynamicMergeStrategy);
    }
}
