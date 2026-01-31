package com.lc.product.center.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 定价策略表(product_center.pricing_strategy)视图对象
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "PricingStrategyVO", description = "定价策略VO")
public class PricingStrategyVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "租户ID")
    private String tenantId;

    // ==================== 策略标识 ====================

    @Schema(description = "策略编码")
    private String strategyCode;

    @Schema(description = "策略名称")
    private String strategyName;

    // ==================== 策略类型 ====================

    @Schema(description = "策略类型")
    private String strategyType;

    @Schema(description = "策略类型描述")
    private String strategyTypeDesc;

    // ==================== 应用范围 ====================

    @Schema(description = "应用范围")
    private String applyScope;

    @Schema(description = "应用范围描述")
    private String applyScopeDesc;

    @Schema(description = "范围值")
    private String applyScopeValue;

    // ==================== 策略配置 ====================

    @Schema(description = "策略配置(阶梯区间/区域系数等)")
    private Map<String, Object> strategyConfig;

    @Schema(description = "阶梯参数列表")
    private List<PricingStrategyParamVO> strategyParams;

    // ==================== 优先级 ====================

    @Schema(description = "优先级")
    private Integer priority;

    // ==================== 时间有效性 ====================

    @Schema(description = "生效时间")
    private Date effectiveTime;

    @Schema(description = "失效时间")
    private Date expiryTime;

    // ==================== 状态 ====================

    @Schema(description = "状态")
    private String status;

    @Schema(description = "状态描述")
    private String statusDesc;

    @Schema(description = "备注说明")
    private String remark;

    // ==================== 审计字段 ====================

    @Schema(description = "创建时间")
    private Date dtCreated;

    @Schema(description = "更新时间")
    private Date dtModified;
}
