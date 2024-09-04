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

    /**
     * 设置查询页码
     * @param pageIndex 页码，从0开始
     */
    void setPageIndex(Long pageIndex);

    /**
     * 获取当前页码
     * @return 当前页码
     */
    Long getPageIndex();

    /**
     * 获取每页大小
     * @return 每页大小
     */
    Long getPageSize();

    /**
     * 获取总数
     * @return 总数
     */
    Long getTotal();

    /**
     * 验证参数
     * @return true表示验证成功，false表示验证失败
     */
    default boolean validate() {
        return getPageIndex() != null && getPageIndex() >= 0 && getPageSize() != null && getPageSize() > 0 && getTotal() != null && getTotal() >= 0;
    }
}
