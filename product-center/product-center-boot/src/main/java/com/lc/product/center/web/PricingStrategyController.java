package com.lc.product.center.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.core.page.PaginationResult;
import com.lc.framework.core.utils.validator.Groups;
import com.lc.product.center.domain.dto.PricingStrategyDTO;
import com.lc.product.center.domain.vo.PricingStrategyVO;
import com.lc.product.center.service.PricingStrategyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定价策略管理控制器
 *
 * @author lucheng
 * @since 2026-01-31
 */
@AllArgsConstructor
@RestController
@RequestMapping("/pricing-strategy")
@Tag(name = "定价策略管理", description = "定价策略的增删改查接口")
public class PricingStrategyController {

    private final PricingStrategyService pricingStrategyService;

    @PostMapping("/list")
    @Operation(summary = "查询策略列表")
    public WebResult<List<PricingStrategyVO>> list(@RequestBody PricingStrategyDTO queryDTO) {
        List<PricingStrategyVO> list = pricingStrategyService.listByCondition(queryDTO);
        return WebResult.success(list);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询策略列表")
    public WebResult<PaginationResult<PricingStrategyVO>> page(
            @RequestBody @Validated(Groups.PageGroup.class) PricingStrategyDTO queryDTO) {
        PaginationResult<PricingStrategyVO> result = pricingStrategyService.pageByCondition(queryDTO);
        return WebResult.success(result);
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "查询策略详情")
    public WebResult<PricingStrategyVO> detail(@PathVariable Long id) {
        PricingStrategyVO vo = pricingStrategyService.getDetailById(id);
        return WebResult.success(vo);
    }

    @PostMapping("/create")
    @Operation(summary = "创建定价策略")
    public WebResult<PricingStrategyVO> create(
            @RequestBody @Validated(Groups.AddGroup.class) PricingStrategyDTO strategyDTO) {
        PricingStrategyVO vo = pricingStrategyService.createStrategy(strategyDTO);
        return WebResult.success(vo);
    }

    @PutMapping("/update")
    @Operation(summary = "更新定价策略")
    public WebResult<PricingStrategyVO> update(
            @RequestBody @Validated(Groups.UpdateGroup.class) PricingStrategyDTO strategyDTO) {
        PricingStrategyVO vo = pricingStrategyService.updateStrategy(strategyDTO);
        return WebResult.success(vo);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除定价策略")
    public WebResult<Boolean> delete(@PathVariable Long id) {
        Boolean result = pricingStrategyService.deleteStrategy(id);
        return WebResult.success(result);
    }
}
