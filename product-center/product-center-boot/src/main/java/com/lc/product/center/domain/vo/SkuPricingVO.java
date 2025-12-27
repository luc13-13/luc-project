package com.lc.product.center.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * SKU定价表(product_center.sku_pricing)表视图类
 *
 * @author lucheng
 * @since 2025-12-27
 */
@Data
@Schema(name = "SkuPricingVO", description = "SKU定价VO")
public class SkuPricingVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "租户ID")
    private String tenantId;

    @Schema(description = "SKU编码")
    private String skuCode;

    @Schema(description = "定价模式: PAY_AS_GO/PREPAID/SUBSCRIPTION")
    private String pricingModel;

    @Schema(description = "定价模式描述")
    private String pricingModelDesc;

    @Schema(description = "计费周期: HOURLY/DAILY/MONTHLY/QUARTERLY/YEARLY")
    private String billingPeriod;

    @Schema(description = "计费周期描述")
    private String billingPeriodDesc;

    @Schema(description = "周期数量")
    private Integer periodCount;

    @Schema(description = "原价")
    private BigDecimal originalPrice;

    @Schema(description = "售价")
    private BigDecimal salePrice;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "折扣率")
    private BigDecimal discountRate;

    @Schema(description = "生效时间")
    private Date effectiveTime;

    @Schema(description = "失效时间")
    private Date expiryTime;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "状态描述")
    private String statusDesc;

    @Schema(description = "创建时间")
    private Date dtCreated;

    @Schema(description = "更新时间")
    private Date dtModified;
}
