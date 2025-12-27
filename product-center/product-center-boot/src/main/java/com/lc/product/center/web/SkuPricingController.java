package com.lc.product.center.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.core.page.PaginationResult;
import com.lc.framework.core.utils.validator.Groups;
import com.lc.product.center.domain.dto.SkuPricingDTO;
import com.lc.product.center.domain.vo.SkuPricingVO;
import com.lc.product.center.service.SkuPricingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SKU定价管理控制器
 *
 * @author lucheng
 * @since 2025-12-27
 */
@RestController
@RequestMapping("/sku-pricing")
@Tag(name = "SKU定价管理", description = "SKU定价的增删改查接口")
public class SkuPricingController {

    @Autowired
    private SkuPricingService skuPricingService;

    @PostMapping("/list")
    @Operation(summary = "查询定价列表")
    public WebResult<List<SkuPricingVO>> list(@RequestBody SkuPricingDTO queryDTO) {
        return WebResult.success(skuPricingService.queryPricingList(queryDTO));
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询定价列表")
    public WebResult<PaginationResult<SkuPricingVO>> page(
            @RequestBody @Validated(Groups.PageGroup.class) SkuPricingDTO queryDTO) {
        return WebResult.success(skuPricingService.queryPricingPage(queryDTO));
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "查询定价详情")
    public WebResult<SkuPricingVO> detail(@PathVariable Long id) {
        SkuPricingVO result = skuPricingService.getPricingById(id);
        return result == null ? WebResult.error("定价不存在") : WebResult.success(result);
    }

    @GetMapping("/by-sku")
    @Operation(summary = "根据SKU编码查询定价")
    public WebResult<List<SkuPricingVO>> getBySkuCode(
            @Parameter(description = "租户ID") @RequestParam(defaultValue = "DEFAULT") String tenantId,
            @Parameter(description = "SKU编码") @RequestParam String skuCode) {
        return WebResult.success(skuPricingService.getPricingsBySkuCode(tenantId, skuCode));
    }

    @GetMapping("/effective")
    @Operation(summary = "查询有效定价")
    public WebResult<SkuPricingVO> getEffectivePricing(
            @Parameter(description = "租户ID") @RequestParam(defaultValue = "DEFAULT") String tenantId,
            @Parameter(description = "SKU编码") @RequestParam String skuCode,
            @Parameter(description = "计费周期") @RequestParam String billingPeriod) {
        SkuPricingVO result = skuPricingService.getEffectivePricing(tenantId, skuCode, billingPeriod);
        return result == null ? WebResult.error("无有效定价") : WebResult.success(result);
    }

    @PostMapping("/create")
    @Operation(summary = "创建定价")
    public WebResult<SkuPricingVO> create(
            @RequestBody @Validated(Groups.AddGroup.class) SkuPricingDTO pricingDTO) {
        return WebResult.success(skuPricingService.createPricing(pricingDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新定价")
    public WebResult<SkuPricingVO> update(
            @RequestBody @Validated(Groups.UpdateGroup.class) SkuPricingDTO pricingDTO) {
        return WebResult.success(skuPricingService.updatePricing(pricingDTO));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除定价")
    public WebResult<Boolean> delete(@PathVariable Long id) {
        return WebResult.success(skuPricingService.deletePricing(id));
    }
}
