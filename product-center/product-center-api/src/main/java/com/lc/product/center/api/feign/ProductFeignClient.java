package com.lc.product.center.api.feign;

import com.lc.framework.core.mvc.WebResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025/3/5 11:05
 */
@FeignClient(value = "product-center")
public interface ProductFeignClient {

    @GetMapping("/product/list")
    WebResult<String> getProductList();

    @GetMapping("/product/detail")
    WebResult<String> getProductDetail(@RequestParam("productId") String productId);

}
