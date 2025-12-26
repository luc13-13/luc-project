package com.lc.product.center.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.core.page.PaginationResult;
import com.lc.framework.core.utils.validator.Groups;
import com.lc.product.center.domain.dto.ProductSkuDTO;
import com.lc.product.center.domain.vo.ProductSkuVO;
import com.lc.product.center.service.ProductSkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品SKU管理控制器
 *
 * @author lucheng
 * @since 2025-12-21
 */
@RestController
@RequestMapping("/sku")
@Tag(name = "产品SKU管理", description = "产品SKU的增删改查接口")
public class ProductSkuController {

    @Autowired
    private ProductSkuService productSkuService;

    @PostMapping("/list")
    @Operation(summary = "查询SKU列表", description = "根据查询条件查询SKU列表")
    public WebResult<List<ProductSkuVO>> list(@RequestBody ProductSkuDTO queryDTO) {
        List<ProductSkuVO> result = productSkuService.querySkuList(queryDTO);
        return WebResult.success(result);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询SKU列表", description = "根据查询条件分页查询SKU列表")
    public WebResult<PaginationResult<ProductSkuVO>> page(
            @RequestBody @Validated(Groups.PageGroup.class) ProductSkuDTO queryDTO) {
        PaginationResult<ProductSkuVO> result = productSkuService.querySkuPage(queryDTO);
        return WebResult.success(result);
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "查询SKU详情", description = "根据SKU ID查询详情")
    public WebResult<ProductSkuVO> detail(
            @Parameter(description = "SKU ID") @PathVariable Long id) {
        ProductSkuVO result = productSkuService.getSkuById(id);
        if (result == null) {
            return WebResult.error("SKU不存在");
        }
        return WebResult.success(result);
    }

    @GetMapping("/by-code")
    @Operation(summary = "根据SKU编码查询", description = "根据SKU编码查询详情")
    public WebResult<ProductSkuVO> getByCode(
            @Parameter(description = "租户ID") @RequestParam(defaultValue = "DEFAULT") String tenantId,
            @Parameter(description = "SKU编码") @RequestParam String skuCode) {
        ProductSkuVO result = productSkuService.getSkuByCode(tenantId, skuCode);
        if (result == null) {
            return WebResult.error("SKU不存在");
        }
        return WebResult.success(result);
    }

    @GetMapping("/by-product")
    @Operation(summary = "根据产品编码查询SKU列表", description = "根据产品编码查询SKU列表")
    public WebResult<List<ProductSkuVO>> getByProductCode(
            @Parameter(description = "租户ID") @RequestParam(defaultValue = "DEFAULT") String tenantId,
            @Parameter(description = "产品编码") @RequestParam String productCode) {
        List<ProductSkuVO> result = productSkuService.getSkusByProductCode(tenantId, productCode);
        return WebResult.success(result);
    }

    @GetMapping("/saleable")
    @Operation(summary = "查询可售SKU列表", description = "查询所有可售的SKU")
    public WebResult<List<ProductSkuVO>> getSaleableSkus(
            @Parameter(description = "租户ID") @RequestParam(defaultValue = "DEFAULT") String tenantId) {
        List<ProductSkuVO> result = productSkuService.getSaleableSkus(tenantId);
        return WebResult.success(result);
    }

    @PostMapping("/create")
    @Operation(summary = "创建SKU", description = "创建新的SKU")
    public WebResult<ProductSkuVO> create(
            @RequestBody @Validated(Groups.AddGroup.class) ProductSkuDTO skuDTO) {
        ProductSkuVO result = productSkuService.createSku(skuDTO);
        return WebResult.success(result);
    }

    @PutMapping("/update")
    @Operation(summary = "更新SKU", description = "根据SKU ID更新SKU信息")
    public WebResult<ProductSkuVO> update(
            @RequestBody @Validated(Groups.UpdateGroup.class) ProductSkuDTO skuDTO) {
        ProductSkuVO result = productSkuService.updateSku(skuDTO);
        return WebResult.success(result);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除SKU", description = "根据SKU ID删除SKU（逻辑删除）")
    public WebResult<Boolean> delete(
            @Parameter(description = "SKU ID") @PathVariable Long id) {
        Boolean result = productSkuService.deleteSku(id);
        return WebResult.success(result);
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除SKU", description = "批量删除SKU（逻辑删除）")
    public WebResult<Boolean> batchDelete(@RequestBody List<Long> ids) {
        Boolean result = productSkuService.batchDeleteSku(ids);
        return WebResult.success(result);
    }

    @PutMapping("/publish/{id}")
    @Operation(summary = "上架SKU", description = "上架SKU")
    public WebResult<Boolean> publish(
            @Parameter(description = "SKU ID") @PathVariable Long id) {
        Boolean result = productSkuService.publishSku(id);
        return WebResult.success(result);
    }

    @PutMapping("/unpublish/{id}")
    @Operation(summary = "下架SKU", description = "下架SKU")
    public WebResult<Boolean> unpublish(
            @Parameter(description = "SKU ID") @PathVariable Long id) {
        Boolean result = productSkuService.unpublishSku(id);
        return WebResult.success(result);
    }
}
