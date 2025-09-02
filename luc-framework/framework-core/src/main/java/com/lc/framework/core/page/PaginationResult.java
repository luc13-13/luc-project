package com.lc.framework.core.page;

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *     分页查询返回值封装, 包含实体列表、每页大小、查询的最后一页索引、总数、实体列表大小
 * </pre>
 *
 * @param pageIndex 当前页索引，从0开始
 * @param pageSize         每页大小
 * @param total            总数
 * @param count            返回结果的数量
 * @author Lu Cheng
 * @date 2024/6/24 9:11
 */
public record PaginationResult<T>(Long pageIndex, Long pageSize, Long total, Long count, List<T> results) {


    public static <T> PaginationResult<T> success(List<T> results, PaginationParams params) {
        Assert.isTrue(results != null, "NullPointException: null List is not supported for PaginationResult");
        Assert.isTrue(params.validate(), "please check the pageSize: " + params.getPageSize() + " pageIndex: " + params.getPageIndex() + " total: " + params.getTotal());
        return new PaginationResult<>(params.getPageIndex(), params.getPageSize(), params.getTotal(), (long) results.size(), results);
    }

    public static <T> PaginationResult<T> fail(PaginationParams params) {
        Assert.isTrue(params.validate(), "please check the pageSize: " + params.getPageSize() + " pageIndex: " + params.getPageIndex() + " total: " + params.getTotal());
        return new PaginationResult<>(params.getPageIndex(), params.getPageSize(), params.getTotal(), 0L, Collections.emptyList());
    }

    public static <T> PaginationResult<T> empty() {
        return new PaginationResult<>(0L, 0L, 0L, 0L, Collections.emptyList());
    }

    public boolean isFinished() {
        return total == null || total <= 0 || results == null || results.isEmpty();
    }
}