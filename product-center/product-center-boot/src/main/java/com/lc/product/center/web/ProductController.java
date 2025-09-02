package com.lc.product.center.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.domain.dto.ProductInfoDTO;
import com.lc.product.center.domain.vo.ProductInfoVO;
import com.lc.product.center.service.ProductInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品管理控制器
 *
 * @author Lu Cheng
 * @date 2025/8/31 15:22
 * @version 1.0
 */
@RestController
@RequestMapping("/product")
@Tag(name = "产品管理", description = "产品信息的增删改查接口")
public class ProductController {

    @Autowired
    private ProductInfoService productInfoService;

    @PostMapping("/list")
    @Operation(summary = "分页查询产品列表", description = "根据查询条件分页查询产品列表")
    public WebResult<PaginationResult<ProductInfoVO>> list(@RequestBody ProductInfoDTO queryDTO) {
        PaginationResult<ProductInfoVO> result = productInfoService.queryProductPage(queryDTO);
        return WebResult.success(result);
    }

    @PostMapping("/all")
    @Operation(summary = "查询所有产品", description = "查询所有产品列表，不分页")
    public WebResult<List<ProductInfoVO>> listAll(@RequestBody ProductInfoDTO queryDTO) {
        List<ProductInfoVO> result = productInfoService.queryProductList(queryDTO);
        return WebResult.success(result);
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "查询产品详情", description = "根据产品ID查询产品详情")
    public WebResult<ProductInfoVO> detail(
            @Parameter(description = "产品ID") @PathVariable Long id) {
        ProductInfoVO result = productInfoService.getProductById(id);
        if (result == null) {
            return WebResult.error("产品不存在");
        }
        return WebResult.success(result);
    }

    @PostMapping("/create")
    @Operation(summary = "创建产品", description = "创建新的产品信息")
    public WebResult<ProductInfoVO> create(@RequestBody ProductInfoDTO productDTO) {
        ProductInfoVO result = productInfoService.createProduct(productDTO);
        return WebResult.success(result);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "更新产品", description = "根据产品ID更新产品信息")
    public WebResult<ProductInfoVO> update(
            @Parameter(description = "产品ID") @PathVariable Long id,
            @RequestBody ProductInfoDTO productDTO) {
        try {
            ProductInfoVO result = productInfoService.updateProduct(id, productDTO);
            return WebResult.success(result);
        } catch (RuntimeException e) {
            return WebResult.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除产品", description = "根据产品ID删除产品（逻辑删除）")
    public WebResult<Boolean> delete(
            @Parameter(description = "产品ID") @PathVariable Long id) {
        Boolean result = productInfoService.deleteProduct(id);
        return WebResult.success(result);
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除产品", description = "批量删除产品（逻辑删除）")
    public WebResult<Boolean> batchDelete(@RequestBody List<Long> ids) {
        Boolean result = productInfoService.batchDeleteProduct(ids);
        return WebResult.success(result);
    }
}
