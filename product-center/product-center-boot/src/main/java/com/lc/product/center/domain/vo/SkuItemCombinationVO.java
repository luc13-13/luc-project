package com.lc.product.center.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * SKU计费项组合表(product_center.sku_item_combination)表视图类
 *
 * @author lucheng
 * @since 2025-12-27
 */
@Data
@Schema(name = "SkuItemCombinationVO", description = "SKU计费项组合VO")
public class SkuItemCombinationVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "租户ID")
    private String tenantId;

    @Schema(description = "SKU编码")
    private String skuCode;

    @Schema(description = "产品编码")
    private String productCode;

    @Schema(description = "规格族编码")
    private String subProductCode;

    @Schema(description = "计费项编码")
    private String billingItemCode;

    @Schema(description = "计费规格编码")
    private String subBillingItemCode;

    @Schema(description = "数量/份数")
    private BigDecimal quantity;

    @Schema(description = "是否计入SKU定价: 1是 0否")
    private Short pricingIncluded;

    @Schema(description = "是否计入SKU定价描述")
    private String pricingIncludedDesc;

    @Schema(description = "创建时间")
    private Date dtCreated;

    @Schema(description = "更新时间")
    private Date dtModified;
}
