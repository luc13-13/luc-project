package com.lc.product.center.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * SKU与策略关联表(product_center.sku_pricing_strategy_link)视图对象
 *
 * @author lucheng
 * @since 2026-02-05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SkuPricingStrategyLinkVO", description = "SKU与策略关联VO")
public class SkuPricingStrategyLinkVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "租户ID")
    private String tenantId;

    // ==================== SKU关联 ====================

    @Schema(description = "SKU编码")
    private String skuCode;

    @Schema(description = "SKU版本号")
    private String skuRevision;

    // ==================== 策略关联 ====================

    @Schema(description = "策略编码")
    private String strategyCode;

    @Schema(description = "策略名称")
    private String strategyName;

    // ==================== 执行优先级 ====================

    @Schema(description = "优先级")
    private Integer priority;

    // ==================== 有效期 ====================

    @Schema(description = "生效时间")
    private Date effectiveTime;

    @Schema(description = "失效时间")
    private Date expiryTime;

    // ==================== 状态 ====================

    @Schema(description = "状态")
    private String status;

    @Schema(description = "状态描述")
    private String statusDesc;

    // ==================== 审计字段 ====================

    @Schema(description = "创建时间")
    private Date dtCreated;

    @Schema(description = "更新时间")
    private Date dtModified;
}
