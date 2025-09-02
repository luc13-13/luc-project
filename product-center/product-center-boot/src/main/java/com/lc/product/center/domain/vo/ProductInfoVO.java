package com.lc.product.center.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品信息表(product_center.product_info)表视图对象
 *
 * @author lucheng
 * @since 2025-08-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ProductInfoVO", description = "产品信息视图对象")
public class ProductInfoVO implements Serializable {
    /**
     * 主键id
     */
    @Schema(description = "主键id")
    private Long id;

    /**
     * 产品code
     */
    @Schema(description = "产品code")
    private String productCode;

    /**
     * 子产品code
     */
    @Schema(description = "子产品code")
    private String subProductCode;

    /**
     * 计费项code
     */
    @Schema(description = "计费项code")
    private String billingItemCode;

    /**
     * 子计费项code
     */
    @Schema(description = "子计费项code")
    private String subBillingItemCode;

    /**
     * 产品名称
     */
    @Schema(description = "产品名称")
    private String productName;

    /**
     * 子产品名称
     */
    @Schema(description = "子产品名称")
    private String subProductName;

    /**
     * 计费项名称
     */
    @Schema(description = "计费项名称")
    private String billingItemName;

    /**
     * 子计费项名称
     */
    @Schema(description = "子计费项名称")
    private String subBillingItemName;

    /**
     * 单位，个、次、GB等
     */
    @Schema(description = "单位，个、次、GB等")
    private String unit;

    /**
     * 价格
     */
    @Schema(description = "价格")
    private BigDecimal price;

    /**
     * 计费规格
     */
    @Schema(description = "计费规格")
    private BigDecimal chargeSize;

    /**
     * 生效状态（1生效 0失效）
     */
    @Schema(description = "生效状态（1生效 0失效）")
    private Short status;

    /**
     * 状态描述
     */
    @Schema(description = "状态描述")
    private String statusDesc;

    /**
     * 创建者
     */
    @Schema(description = "创建者")
    private String createdBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date dtCreated;

    /**
     * 更新者
     */
    @Schema(description = "更新者")
    private String modifiedBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private Date dtModified;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
