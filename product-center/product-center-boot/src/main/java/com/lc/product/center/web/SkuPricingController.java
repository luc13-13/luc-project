package com.lc.product.center.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.core.page.PaginationResult;
import com.lc.framework.core.utils.validator.Groups;
import com.lc.product.center.domain.dto.SkuPricingDTO;
import com.lc.product.center.domain.vo.SkuPricingVO;
import com.lc.product.center.service.SkuPricingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定价模板管理控制器
 *
 * @author lucheng
 * @since 2026-01-31
 */
@AllArgsConstructor
@RestController
@RequestMapping("/sku-pricing")
@Tag(name = "定价模板管理", description = "定价模板的增删改查接口")
public class SkuPricingController {

    private final SkuPricingService skuPricingService;

    @PostMapping("/list")
    @Operation(summary = "查询定价模板列表")
    public WebResult<List<SkuPricingVO>> list(@RequestBody SkuPricingDTO queryDTO) {
        List<SkuPricingVO> list = skuPricingService.listByCondition(queryDTO);
        return WebResult.success(list);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询定价模板列表")
    public WebResult<PaginationResult<SkuPricingVO>> page(
            @RequestBody @Validated(Groups.PageGroup.class) SkuPricingDTO queryDTO) {
        PaginationResult<SkuPricingVO> result = skuPricingService.pageByCondition(queryDTO);
        return WebResult.success(result);
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "查询定价模板详情")
    public WebResult<SkuPricingVO> detail(@PathVariable Long id) {
        SkuPricingVO vo = skuPricingService.getDetailById(id);
        return WebResult.success(vo);
    }

    @PostMapping("/create")
    @Operation(summary = "创建定价模板")
    public WebResult<SkuPricingVO> create(
            @RequestBody @Validated(Groups.AddGroup.class) SkuPricingDTO pricingDTO) {
        SkuPricingVO vo = skuPricingService.createPricing(pricingDTO);
        return WebResult.success(vo);
    }

    @PutMapping("/update")
    @Operation(summary = "更新定价模板")
    public WebResult<SkuPricingVO> update(
            @RequestBody @Validated(Groups.UpdateGroup.class) SkuPricingDTO pricingDTO) {
        SkuPricingVO vo = skuPricingService.updatePricing(pricingDTO);
        return WebResult.success(vo);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除定价模板")
    public WebResult<Boolean> delete(@PathVariable Long id) {
        Boolean result = skuPricingService.deletePricing(id);
        return WebResult.success(result);
    }
}
