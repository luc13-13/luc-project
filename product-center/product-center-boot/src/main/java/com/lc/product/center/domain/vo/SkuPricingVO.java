package com.lc.product.center.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 定价模板表(product_center.sku_pricing)表视图类
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SkuPricingVO", description = "定价模板VO")
public class SkuPricingVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "租户ID")
    private String tenantId;

    // ==================== 业务标识 ====================

    @Schema(description = "定价编码")
    private String pricingCode;

    @Schema(description = "定价版本号")
    private String revision;

    // ==================== 四维度收费模式 ====================

    @Schema(description = "计量方式: BY_USAGE/BY_QUOTA")
    private String meteringMode;

    @Schema(description = "计量方式描述")
    private String meteringModeDesc;

    @Schema(description = "付费方式: POSTPAID/PREPAID/SUBSCRIPTION")
    private String paymentMode;

    @Schema(description = "付费方式描述")
    private String paymentModeDesc;

    @Schema(description = "计费周期: HOURLY/DAILY/MONTHLY/QUARTERLY/YEARLY/ONCE")
    private String billingCycle;

    @Schema(description = "计费周期描述")
    private String billingCycleDesc;

    @Schema(description = "周期数量")
    private Integer cycleCount;

    @Schema(description = "计费单位: PERIOD/QUANTITY")
    private String billingUnit;

    @Schema(description = "计费单位描述")
    private String billingUnitDesc;

    @Schema(description = "退款政策")
    private String refundPolicy;

    // ==================== 价格信息 ====================

    @Schema(description = "折扣率")
    private BigDecimal discountRate;

    @Schema(description = "币种")
    private String currency;

    // ==================== 计量配置 ====================

    @Schema(description = "计量单位")
    private String meteringUnit;

    @Schema(description = "计量精度")
    private Integer meteringPrecision;

    // ==================== 时间与优先级 ====================

    @Schema(description = "生效时间")
    private Date effectiveTime;

    @Schema(description = "失效时间")
    private Date expiryTime;

    @Schema(description = "是否当前版本")
    private Short isCurrent;

    @Schema(description = "优先级")
    private Integer priority;

    // ==================== 状态 ====================

    @Schema(description = "状态")
    private String status;

    @Schema(description = "状态描述")
    private String statusDesc;

    @Schema(description = "备注")
    private String remark;

    // ==================== 审计字段 ====================

    @Schema(description = "创建时间")
    private Date dtCreated;

    @Schema(description = "更新时间")
    private Date dtModified;
}
