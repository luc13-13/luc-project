package com.lc.product.center.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 定价策略参数细目表(product_center.pricing_strategy_param)视图对象
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "PricingStrategyParamVO", description = "定价策略参数VO")
public class PricingStrategyParamVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "租户ID")
    private String tenantId;

    // ==================== 关联策略模板 ====================

    @Schema(description = "关联策略编码")
    private String strategyCode;

    // ==================== 参数类型 ====================

    @Schema(description = "参数类型: TIER/CAP/FLOOR/THRESHOLD/RATE/FIXED")
    private String paramType;

    // ==================== 阶梯区间 ====================

    @Schema(description = "区间起始")
    private BigDecimal rangeStart;

    @Schema(description = "区间结束")
    private BigDecimal rangeEnd;

    @Schema(description = "区间描述(如: 0-100GB)")
    private String rangeDesc;

    // ==================== 参数值 ====================

    @Schema(description = "通用参数值")
    private BigDecimal value;

    // ==================== 排序 ====================

    @Schema(description = "排序")
    private Integer sortOrder;
}
