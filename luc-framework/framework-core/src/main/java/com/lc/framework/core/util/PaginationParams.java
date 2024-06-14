package com.lc.framework.core.util;


import jakarta.validation.constraints.NotNull;

/**
 * @author : Lu Cheng
 * @version : 1.0
 * @desc : 实现了该接口的类，具备分页操作的参数 rowStart pageIndex pageSize
 * @date : 2023/4/15 13:42
 */
public interface PaginationParams {
    Integer getPageIndex();

    void setPageIndex( Integer pageIndex);

    Integer getPageSize();


    void setPageSize(@NotNull Integer pageSize);

    default Integer getRowStart() {
        Integer pageSize = this.getPageSize();
        if(pageSize == null || pageSize == 0) {
            return 0;
        }

        Integer pageIndex = this.getPageIndex();

        if(pageIndex == null || pageIndex == 0) {
            return 0;
        }
        return (pageIndex - 1) * pageSize;

    }

}
