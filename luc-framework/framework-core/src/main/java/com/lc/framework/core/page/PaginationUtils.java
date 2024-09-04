package com.lc.framework.core.page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/6/21 15:43
 */
@Slf4j
public class PaginationUtils {
    /**
     *
     * @param singlePageQueryFunction 查询方法, 由调用方确定
     * @param pageParams 分页查询参数, {@link PaginationParams}
     * @param threshold 查询阈值，当查询数量大于threshold后，停止分页查询并返回当前结果
     * @return 分页查询结果, {@link PaginationResult}
     * @param <T> 分页查询参数实现类
     * @param <R> 分页查询结果实体类
     */
    public static <T extends PaginationParams, R> PaginationResult<R> pageSearch(Function<T, List<R>> singlePageQueryFunction, T pageParams, Long threshold) {
        Assert.isTrue(pageParams.validate(), "please check the pageSize: " + pageParams.getPageSize() + " pageIndex: " + pageParams.getPageIndex() + " total: " + pageParams.getTotal());
        PaginationResult<R> paginationResult = PaginationResult.empty();
        // 每页大小
        long pageSize = pageParams.getPageSize();
        // 起始页码
        long pageIndex = pageParams.getPageIndex();
        // 总数量
        long total = pageParams.getTotal();
        log.info("分页查询工具——start，起始页{}，页大小{}，总数{}", pageIndex, pageSize, total);
        long count = 0;
        List<R> response = singlePageQueryFunction.apply(pageParams);
        if (skipPage(response)) {
            log.info("分页查询工具——返回空页，当前页{}，页大小{}，总数{}", pageParams.getPageIndex(), pageSize, count);
            return paginationResult;
        }
        List<R> result = new ArrayList<>(response);
        count = count + response.size();
        while (total > pageSize * (pageIndex + 1) && count < threshold) {
            pageParams.setPageIndex( ++pageIndex);
            response = singlePageQueryFunction.apply(pageParams);
            count = count + response.size();
            if (skipPage(response)) {
                log.info("分页查询工具——start，当前页{}，页大小{}，总数{}", pageParams.getPageIndex(), pageSize, count);
                break;
            }
            result.addAll(response);
        }
        return PaginationResult.success(result, pageParams);
    }

    private static <T> boolean skipPage(List<T> pageResults) {
        return pageResults == null || pageResults.size() == 0;
    }
}
