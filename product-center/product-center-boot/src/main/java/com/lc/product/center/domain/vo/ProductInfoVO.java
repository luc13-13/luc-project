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

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Schema(description = "主键id")
    private Long id;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private String tenantId;

    // ==================== 四层产品结构 ====================

    /**
     * 产品编码
     */
    @Schema(description = "产品编码")
    private String productCode;

    /**
     * 规格族编码
     */
    @Schema(description = "规格族编码")
    private String subProductCode;

    /**
     * 计费项编码
     */
    @Schema(description = "计费项编码")
    private String billingItemCode;

    /**
     * 计费规格编码
     */
    @Schema(description = "计费规格编码")
    private String subBillingItemCode;

    // ==================== 名称 ====================

    /**
     * 产品名称
     */
    @Schema(description = "产品名称")
    private String productName;

    /**
     * 规格族名称
     */
    @Schema(description = "规格族名称")
    private String subProductName;

    /**
     * 计费项名称
     */
    @Schema(description = "计费项名称")
    private String billingItemName;

    /**
     * 计费规格名称
     */
    @Schema(description = "计费规格名称")
    private String subBillingItemName;

    // ==================== 规格属性 ====================

    /**
     * 规格值
     */
    @Schema(description = "规格值")
    private BigDecimal specValue;

    /**
     * 规格单位
     */
    @Schema(description = "规格单位")
    private String specUnit;

    // ==================== 计费属性 ====================

    /**
     * 基准单价
     */
    @Schema(description = "基准单价")
    private BigDecimal basePrice;

    /**
     * 价格系数
     */
    @Schema(description = "价格系数")
    private BigDecimal priceFactor;

    /**
     * 计算后的单价 = basePrice * priceFactor
     */
    @Schema(description = "计算后的单价")
    private BigDecimal unitPrice;

    /**
     * 计量单位（账单展示）
     */
    @Schema(description = "计量单位")
    private String meteringUnit;

    // ==================== 状态与排序 ====================

    /**
     * 状态: DRAFT/ACTIVE/INACTIVE
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 状态描述
     */
    @Schema(description = "状态描述")
    private String statusDesc;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sortOrder;

    // ==================== 审计字段 ====================

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
}
