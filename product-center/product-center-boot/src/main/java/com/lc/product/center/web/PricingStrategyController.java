package com.lc.product.center.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.core.page.PaginationResult;
import com.lc.framework.core.utils.validator.Groups;
import com.lc.product.center.domain.dto.PricingStrategyDTO;
import com.lc.product.center.domain.dto.PricingStrategyParamDTO;
import com.lc.product.center.domain.vo.PricingStrategyVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定价策略管理控制器
 *
 * @author lucheng
 * @since 2026-01-31
 */
@RestController
@RequestMapping("/pricing-strategy")
@Tag(name = "定价策略管理", description = "定价策略的增删改查接口")
public class PricingStrategyController {

    @PostMapping("/list")
    @Operation(summary = "查询策略列表")
    public WebResult<List<PricingStrategyVO>> list(@RequestBody PricingStrategyDTO queryDTO) {
        return WebResult.success();
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询策略列表")
    public WebResult<PaginationResult<PricingStrategyVO>> page(
            @RequestBody @Validated(Groups.PageGroup.class) PricingStrategyDTO queryDTO) {
        return WebResult.success();
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "查询策略详情", description = "包含阶梯参数")
    public WebResult<PricingStrategyVO> detail(@PathVariable Long id) {
        return WebResult.success();
    }

    @GetMapping("/by-code")
    @Operation(summary = "根据策略编码查询")
    public WebResult<PricingStrategyVO> getByCode(
            @Parameter(description = "租户ID") @RequestParam(defaultValue = "DEFAULT") String tenantId,
            @Parameter(description = "策略编码") @RequestParam String strategyCode) {
        return WebResult.success();
    }

    @GetMapping("/effective")
    @Operation(summary = "查询有效策略列表")
    public WebResult<List<PricingStrategyVO>> getEffective(
            @Parameter(description = "租户ID") @RequestParam(defaultValue = "DEFAULT") String tenantId,
            @Parameter(description = "策略类型") @RequestParam(required = false) String strategyType) {
        return WebResult.success();
    }

    @PostMapping("/create")
    @Operation(summary = "创建定价策略")
    public WebResult<PricingStrategyVO> create(
            @RequestBody @Validated(Groups.AddGroup.class) PricingStrategyDTO strategyDTO) {
        return WebResult.success();
    }

    @PutMapping("/update")
    @Operation(summary = "更新定价策略")
    public WebResult<PricingStrategyVO> update(
            @RequestBody @Validated(Groups.UpdateGroup.class) PricingStrategyDTO strategyDTO) {
        return WebResult.success();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除定价策略", description = "同时删除关联的阶梯参数")
    public WebResult<Boolean> delete(@PathVariable Long id) {
        return WebResult.success();
    }

    @PostMapping("/{id}/params")
    @Operation(summary = "保存策略阶梯参数", description = "覆盖原有参数")
    public WebResult<Boolean> saveParams(
            @PathVariable Long id,
            @RequestBody List<PricingStrategyParamDTO> params) {
        return WebResult.success();
    }
}
