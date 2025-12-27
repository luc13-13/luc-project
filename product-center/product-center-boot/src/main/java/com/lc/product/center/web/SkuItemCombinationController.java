package com.lc.product.center.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.core.page.PaginationResult;
import com.lc.framework.core.utils.validator.Groups;
import com.lc.product.center.domain.dto.SkuItemCombinationDTO;
import com.lc.product.center.domain.vo.SkuItemCombinationVO;
import com.lc.product.center.service.SkuItemCombinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SKU计费项组合管理控制器
 *
 * @author lucheng
 * @since 2025-12-27
 */
@RestController
@RequestMapping("/sku-item-combination")
@Tag(name = "SKU计费项组合管理", description = "SKU计费项组合的增删改查接口")
public class SkuItemCombinationController {

    @Autowired
    private SkuItemCombinationService skuItemCombinationService;

    @PostMapping("/list")
    @Operation(summary = "查询组合列表")
    public WebResult<List<SkuItemCombinationVO>> list(@RequestBody SkuItemCombinationDTO queryDTO) {
        return WebResult.success(skuItemCombinationService.queryCombinationList(queryDTO));
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询组合列表")
    public WebResult<PaginationResult<SkuItemCombinationVO>> page(
            @RequestBody @Validated(Groups.PageGroup.class) SkuItemCombinationDTO queryDTO) {
        return WebResult.success(skuItemCombinationService.queryCombinationPage(queryDTO));
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "查询组合详情")
    public WebResult<SkuItemCombinationVO> detail(@PathVariable Long id) {
        SkuItemCombinationVO result = skuItemCombinationService.getCombinationById(id);
        return result == null ? WebResult.error("组合不存在") : WebResult.success(result);
    }

    @GetMapping("/by-sku")
    @Operation(summary = "根据SKU编码查询组合")
    public WebResult<List<SkuItemCombinationVO>> getBySkuCode(
            @Parameter(description = "租户ID") @RequestParam(defaultValue = "DEFAULT") String tenantId,
            @Parameter(description = "SKU编码") @RequestParam String skuCode) {
        return WebResult.success(skuItemCombinationService.getCombinationsBySkuCode(tenantId, skuCode));
    }

    @PostMapping("/create")
    @Operation(summary = "创建组合")
    public WebResult<SkuItemCombinationVO> create(
            @RequestBody @Validated(Groups.AddGroup.class) SkuItemCombinationDTO combinationDTO) {
        return WebResult.success(skuItemCombinationService.createCombination(combinationDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新组合")
    public WebResult<SkuItemCombinationVO> update(
            @RequestBody @Validated(Groups.UpdateGroup.class) SkuItemCombinationDTO combinationDTO) {
        return WebResult.success(skuItemCombinationService.updateCombination(combinationDTO));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除组合")
    public WebResult<Boolean> delete(@PathVariable Long id) {
        return WebResult.success(skuItemCombinationService.deleteCombination(id));
    }
}
