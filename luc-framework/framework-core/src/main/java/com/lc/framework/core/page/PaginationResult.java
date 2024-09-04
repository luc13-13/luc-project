package com.lc.framework.core.page;

import lombok.Data;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *     分页查询返回值封装, 包含实体列表、每页大小、查询的最后一页索引、总数、实体列表大小
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/6/24 9:11
 */
@Data
public class PaginationResult<T> {


    private final List<T> results;

    /**
     * 每页大小
     */
    private final Long pageSize;

    /**
     * 当前页索引，从0开始
     */
    private final Long currentPageIndex;

    /**
     * 总数
     */
    private final Long total;

    /**
     * 返回结果的数量
     */
    private final Long count;

    public PaginationResult(List<T> results, Long pageSize, Long currentPageIndex, Long total, Long count) {
        this.results = results;
        this.pageSize = pageSize;
        this.currentPageIndex = currentPageIndex;
        this.total = total;
        this.count = count;
    }

    public static <T> PaginationResult<T> success(List<T> results, PaginationParams params) {
        Assert.isTrue(results != null, "NullPointException: null List is not supported for PaginationResult");
        Assert.isTrue(params.validate(), "please check the pageSize: " + params.getPageSize() + " pageIndex: " + params.getPageIndex() + " total: " + params.getTotal());
        return new PaginationResult<>(results, params.getPageSize(), params.getPageIndex(), params.getTotal(), (long) results.size());
    }

    public static <T> PaginationResult<T> fail(PaginationParams params) {
        Assert.isTrue(params.validate(), "please check the pageSize: " + params.getPageSize() + " pageIndex: " + params.getPageIndex() + " total: " + params.getTotal());
        return new PaginationResult<>(Collections.emptyList(), params.getPageSize(), params.getPageIndex(), params.getTotal(), 0L);
    }

    public static <T> PaginationResult<T> empty() {
        return new PaginationResult<>(Collections.emptyList(), 0L, 0L, 0L, 0L);
    }

    public boolean isFinished() {
        return total == null || total <= 0 || results == null || results.size() == 0;
    }
}