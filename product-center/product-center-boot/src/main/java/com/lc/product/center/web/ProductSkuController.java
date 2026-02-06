package com.lc.product.center.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.core.page.PaginationResult;
import com.lc.framework.core.utils.validator.Groups;
import com.lc.product.center.domain.dto.ProductSkuDTO;
import com.lc.product.center.domain.vo.ProductSkuDetailsVO;
import com.lc.product.center.domain.vo.ProductSkuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品SKU管理控制器
 *
 * @author lucheng
 * @since 2025-12-21
 */
@Tag(name = "产品SKU管理", description = "产品SKU的增删改查接口")
@AllArgsConstructor
@RestController
@RequestMapping("/sku")
public class ProductSkuController {


    @PostMapping("/list")
    @Operation(summary = "查询SKU列表", description = "根据查询条件查询SKU列表")
    public WebResult<List<ProductSkuVO>> list(@RequestBody ProductSkuDTO queryDTO) {
        return WebResult.success();
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询SKU列表", description = "根据查询条件分页查询SKU列表")
    public WebResult<PaginationResult<ProductSkuVO>> page(
            @RequestBody @Validated(Groups.PageGroup.class) ProductSkuDTO queryDTO) {
        return WebResult.success();
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "查询SKU详情", description = "根据SKU ID查询详情")
    public WebResult<ProductSkuDetailsVO> detail(
            @Parameter(description = "SKU ID") @PathVariable Long id) {
        return WebResult.success();
    }

    @PostMapping("/create")
    @Operation(summary = "创建SKU", description = "创建新的SKU")
    public WebResult<ProductSkuVO> create(
            @RequestBody @Validated(Groups.AddGroup.class) ProductSkuDTO skuDTO) {
        return WebResult.success();
    }

    @PutMapping("/update")
    @Operation(summary = "更新SKU", description = "根据SKU ID更新SKU信息")
    public WebResult<ProductSkuVO> update(
            @RequestBody @Validated(Groups.UpdateGroup.class) ProductSkuDTO skuDTO) {
        return WebResult.success();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除SKU", description = "根据SKU ID删除SKU（逻辑删除）")
    public WebResult<Boolean> delete(
            @Parameter(description = "SKU ID") @PathVariable Long id) {
        return WebResult.success();
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除SKU", description = "批量删除SKU（逻辑删除）")
    public WebResult<Boolean> batchDelete(@RequestBody List<Long> ids) {
        return WebResult.success();
    }

    @PutMapping("/publish/{id}")
    @Operation(summary = "上架SKU", description = "上架SKU")
    public WebResult<Boolean> publish(
            @Parameter(description = "SKU ID") @PathVariable Long id) {
        return WebResult.success();
    }

    @PutMapping("/unpublish/{id}")
    @Operation(summary = "下架SKU", description = "下架SKU")
    public WebResult<Boolean> unpublish(
            @Parameter(description = "SKU ID") @PathVariable Long id) {
        return WebResult.success();
    }
}
