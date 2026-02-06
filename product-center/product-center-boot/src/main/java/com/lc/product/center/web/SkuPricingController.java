package com.lc.product.center.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.core.page.PaginationResult;
import com.lc.framework.core.utils.validator.Groups;
import com.lc.product.center.domain.dto.SkuPricingDTO;
import com.lc.product.center.domain.vo.SkuPricingVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定价模板管理控制器
 *
 * @author lucheng
 * @since 2026-01-31
 */
@RestController
@RequestMapping("/sku-pricing")
@Tag(name = "定价模板管理", description = "定价模板的增删改查接口")
public class SkuPricingController {

    @PostMapping("/list")
    @Operation(summary = "查询定价列表")
    public WebResult<List<SkuPricingVO>> list(@RequestBody SkuPricingDTO queryDTO) {
        return WebResult.success();
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询定价列表")
    public WebResult<PaginationResult<SkuPricingVO>> page(
            @RequestBody @Validated(Groups.PageGroup.class) SkuPricingDTO queryDTO) {
        return WebResult.success();
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "查询定价详情")
    public WebResult<SkuPricingVO> detail(@PathVariable Long id) {
        return WebResult.success();
    }

    @PutMapping("/update")
    @Operation(summary = "更新定价")
    public WebResult<SkuPricingVO> update(
            @RequestBody @Validated(Groups.UpdateGroup.class) SkuPricingDTO pricingDTO) {
        return WebResult.success();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除定价")
    public WebResult<Boolean> delete(@PathVariable Long id) {
        return WebResult.success();
    }
}
