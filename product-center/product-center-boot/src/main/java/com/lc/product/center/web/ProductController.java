package com.lc.product.center.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.core.page.PaginationResult;
import com.lc.framework.core.utils.validator.Groups;
import com.lc.product.center.domain.bo.QueryFilter;
import com.lc.product.center.domain.dto.ProductInfoDTO;
import com.lc.product.center.domain.vo.ProductInfoVO;
import com.lc.product.center.service.ProductInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品信息管理控制器
 *
 * @author lucheng
 * @since 2025-08-31
 */
@AllArgsConstructor
@RestController
@RequestMapping("/product")
@Tag(name = "产品信息管理", description = "产品信息的增删改查接口")
public class ProductController {

    private final ProductInfoService productInfoService;

    @PostMapping("/list")
    @Operation(summary = "查询产品列表", description = "根据查询条件查询产品列表")
    public WebResult<List<ProductInfoVO>> list(@RequestBody ProductInfoDTO queryDTO) {
        List<ProductInfoVO> list = productInfoService.listByCondition(queryDTO);
        return WebResult.success(list);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询产品列表", description = "根据查询条件分页查询产品列表")
    public WebResult<PaginationResult<ProductInfoVO>> page(
            @RequestBody @Validated(Groups.PageGroup.class) ProductInfoDTO queryDTO) {
        PaginationResult<ProductInfoVO> result = productInfoService.pageByCondition(queryDTO);
        return WebResult.success(result);
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "查询产品详情", description = "根据产品ID查询产品详情")
    public WebResult<ProductInfoVO> detail(
            @Parameter(description = "产品ID") @PathVariable Long id) {
        ProductInfoVO vo = productInfoService.getDetailById(id);
        return WebResult.success(vo);
    }

    @PostMapping("/create")
    @Operation(summary = "创建产品", description = "创建新的产品信息")
    public WebResult<ProductInfoVO> create(
            @RequestBody @Validated(Groups.AddGroup.class) ProductInfoDTO productDTO) {
        ProductInfoVO vo = productInfoService.createProduct(productDTO);
        return WebResult.success(vo);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "更新产品", description = "根据产品ID更新产品信息")
    public WebResult<ProductInfoVO> update(
            @Parameter(description = "产品ID") @PathVariable Long id,
            @RequestBody @Validated(Groups.UpdateGroup.class) ProductInfoDTO productDTO) {
        productDTO.setId(id);
        ProductInfoVO vo = productInfoService.updateProduct(productDTO);
        return WebResult.success(vo);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除产品", description = "根据产品ID删除产品（逻辑删除）")
    public WebResult<Boolean> delete(
            @Parameter(description = "产品ID") @PathVariable @NotNull(message = "id不能为空") Long id) {
        Boolean result = productInfoService.deleteProduct(id);
        return WebResult.success(result);
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除产品", description = "批量删除产品（逻辑删除）")
    public WebResult<Boolean> batchDelete(@RequestBody List<Long> ids) {
        Boolean result = productInfoService.batchDeleteProduct(ids);
        return WebResult.success(result);
    }

    // ==================== 级联查询接口, 为前端提供筛选条件 ====================

    @GetMapping("/codes")
    @Operation(summary = "获取产品编码列表", description = "获取所有有效的产品编码")
    public WebResult<List<QueryFilter>> getProductCodes(
            @Parameter(description = "租户ID") @RequestParam(defaultValue = "DEFAULT") String tenantId) {
        List<QueryFilter> filters = productInfoService.getProductCodes(tenantId);
        return WebResult.success(filters);
    }

    @GetMapping("/sub-codes")
    @Operation(summary = "获取规格族编码列表", description = "根据产品编码获取规格族编码列表")
    public WebResult<List<QueryFilter>> getSubProductCodes(
            @Parameter(description = "租户ID") @RequestParam(defaultValue = "DEFAULT") String tenantId,
            @Parameter(description = "产品编码") @RequestParam String productCode) {
        List<QueryFilter> filters = productInfoService.getSubProductCodes(tenantId, productCode);
        return WebResult.success(filters);
    }

    @GetMapping("/billing-codes")
    @Operation(summary = "获取计费项编码列表", description = "根据产品编码和规格族编码获取计费项编码列表")
    public WebResult<List<QueryFilter>> getBillingItemCodes(
            @Parameter(description = "租户ID") @RequestParam(defaultValue = "DEFAULT") String tenantId,
            @Parameter(description = "产品编码") @RequestParam String productCode,
            @Parameter(description = "规格族编码") @RequestParam String subProductCode) {
        List<QueryFilter> filters = productInfoService.getBillingItemCodes(tenantId, productCode, subProductCode);
        return WebResult.success(filters);
    }
}
