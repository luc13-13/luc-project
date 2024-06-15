package com.lc.framework.excel.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.lc.framework.excel.template.DynamicHorizontalCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * <pre>
 * 横向嵌套表格：
 * --------------------|----------|------------|---------------------|
 *         |           |          |            | cA[0].child[0].name |
 *         |           | cA[0].id | cA[0].name |---------------------|
 *         |           |          |            | cA[0].child[1].name |
 * p[0].id | p[0].name |----------|----------- |---------------------|
 *         |           |          |            | cB[0].child[0].name |
 *         |           | cB[0].id | cB[0].name |---------------------|
 *         |           |          |            | cB[0].child[1].name |
 * --------------------|----------|------------|---------------------|
 *         |           | cA[0].id | cA[0].name | cA[0].child[0].name |
 * p[1].id | p[1].name | ---------|------------|---------------------|
 *         |           | cA[1].id | cA[1].name | cB[0].child[0].name |
 * --------|-----------|----------|------------|---------------------|
 * 调用方需要将嵌套表以全量数据的形式传给工具类，例如导出以上表格需要传递的数据为：
 * {@code
 * {
 *     [ p[0], cA[0], cA[0].child[0] ],
 *     [ p[0], cA[0], cA[0].child[1] ],
 *     [ p[0], cB[0], cB[0].child[0] ],
 *     [ p[0], cB[0], cB[0].child[1] ],
 *     [ p[1], cA[0], cA[0].child[0] ],
 *     [ p[1], cA[1], cA[1].child[0] ]
 * }
 * }
 * 该处理器不会对数据进行重排序，要求数据根据合并顺序先排好序(排序的过程相当于已经根据分组字段进行了一次分组， 因此可以考虑将{@link List}转为规定的类型， {@link  DynamicHorizontalCell})
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-09 10:45
 */
public class LoopDynamicMergeStrategy implements CellWriteHandler {

    /**
     * 将数据按列分组， key: 需要进行合并的列， value: 该列每次合并的行数（大于1才表示需要向下合并）, 每次合并完成后poll头节点
     */
    Map<Integer, Queue<Integer>> columnGroup;

    /**
     * 记录执行器当前处理到的列的行位置， key：列索引， value：当前处理的行位置, 初始为0， 有表头时需要加上表头的数量
     */
    Map<Integer, Integer> rowGroup;

    public LoopDynamicMergeStrategy(Map<Integer, Queue<Integer>> columnGroup, Map<Integer, Integer> rowGroup) {
        this.columnGroup = columnGroup;
        this.rowGroup = rowGroup;
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        Sheet sheet = writeSheetHolder.getSheet();
        // 被解析的类没有被注解DynamicMerged标注时则不做处理
        if (CollectionUtils.isEmpty(columnGroup)) {
            return;
        }
        if (columnGroup.containsKey(cell.getColumnIndex()) && cell.getRowIndex() == rowGroup.get(cell.getColumnIndex())) {
            // 只有当前单元格行号与当前执行器的行号一致，且当前单元格的列号在目标列中，才进行合并
            mergeRow(sheet, cell.getColumnIndex(), cell.getRowIndex());
        }
    }

    private void mergeRow(Sheet sheet, int columnIdx, int rowStart) {
        // 获取当前需要合并的行数, 并剔除头节点
        Queue<Integer> rowNumbers = columnGroup.get(columnIdx);
        if (!rowNumbers.isEmpty() && rowNumbers.peek() != null) {
            Integer numbers = rowNumbers.poll();
            if (numbers > 1) {
                // 更新合并列的信息
                columnGroup.put(columnIdx, rowNumbers);
                CellRangeAddress cellAddresses = new CellRangeAddress(rowStart, rowStart + numbers - 1, columnIdx, columnIdx);
                sheet.addMergedRegionUnsafe(cellAddresses);
                // 更新当前列处理到的下一行
                rowGroup.put(columnIdx, rowStart + numbers);
            }
        }

    }

}
