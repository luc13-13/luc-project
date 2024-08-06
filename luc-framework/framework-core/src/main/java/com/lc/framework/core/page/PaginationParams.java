package com.lc.framework.core.page;

/**
 * <pre>
 *     分页参数接口, 提供页面大小、起始页码、总数
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/6/21 16:02
 */
public interface PaginationParams {

    void setPageIndex(Long pageIndex);

    Long getPageIndex();

    Long getPageSize();

    Long getTotal();

    default boolean validate() {
        return getPageIndex() != null && getPageIndex() >= 0 && getPageSize() != null && getPageSize() > 0 && getTotal() != null && getTotal() >= 0;
    }
}
