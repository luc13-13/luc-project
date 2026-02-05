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
import java.util.List;

/**
 * 产品SKU表(product_center.product_sku)表视图对象
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ProductSkuVO", description = "产品SKU视图对象")
public class ProductSkuVO implements Serializable {

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

    // ==================== SKU基本信息 ====================

    /**
     * SKU编码
     */
    @Schema(description = "SKU编码")
    private String skuCode;

    /**
     * SKU名称
     */
    @Schema(description = "SKU名称")
    private String skuName;

    // ==================== 版本控制 ====================

    /**
     * SKU版本号
     */
    @Schema(description = "SKU版本号")
    private String revision;

    // ==================== SKU类型 ====================

    /**
     * SKU类型
     */
    @Schema(description = "SKU类型")
    private String skuType;

    /**
     * SKU类型描述
     */
    @Schema(description = "SKU类型描述")
    private String skuTypeDesc;

    // ==================== 基准定价 ====================

    /**
     * 基准单价
     */
    @Schema(description = "基准单价")
    private BigDecimal baseUnitPrice;

    /**
     * 币种
     */
    @Schema(description = "币种")
    private String currency;

    // ==================== 售卖控制 ====================

    /**
     * 是否可售
     */
    @Schema(description = "是否可售")
    private Short saleable;

    /**
     * 是否可见
     */
    @Schema(description = "是否可见")
    private Short visible;

    /**
     * 配额限制
     */
    @Schema(description = "配额限制")
    private Integer quotaLimit;

    // ==================== 版本状态 ====================

    /**
     * 是否当前主版本
     */
    @Schema(description = "是否当前主版本")
    private Short isCurrent;

    /**
     * 生效时间
     */
    @Schema(description = "生效时间")
    private Date effectiveTime;

    /**
     * 失效时间
     */
    @Schema(description = "失效时间")
    private Date expiryTime;

    // ==================== 状态 ====================

    /**
     * 状态
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 状态描述
     */
    @Schema(description = "状态描述")
    private String statusDesc;

    /**
     * 上架时间
     */
    @Schema(description = "上架时间")
    private Date publishTime;

    // ==================== 关联信息（通过BOM查询） ====================

    /**
     * 所属产品编码
     */
    @Schema(description = "所属产品编码(通过BOM查询)")
    private String productCode;

    /**
     * 产品名称
     */
    @Schema(description = "产品名称")
    private String productName;

    /**
     * 所属规格族编码
     */
    @Schema(description = "所属规格族编码(通过BOM查询)")
    private String subProductCode;

    /**
     * 规格族名称
     */
    @Schema(description = "规格族名称")
    private String subProductName;

    // ==================== 价格信息（关联查询） ====================

    /**
     * 按量小时价
     */
    @Schema(description = "按量小时价")
    private BigDecimal hourlyPrice;

    /**
     * 包月价格
     */
    @Schema(description = "包月价格")
    private BigDecimal monthlyPrice;

    /**
     * 包年价格
     */
    @Schema(description = "包年价格")
    private BigDecimal yearlyPrice;

    /**
     * 计费项列表
     */
    @Schema(description = "计费项列表")
    private List<ProductInfoVO> billingItems;

    /**
     * 关联的定价模板列表
     */
    @Schema(description = "关联的定价模板列表")
    private List<SkuPricingVO> pricingTemplates;

    /**
     * 关联的定价策略列表
     */
    @Schema(description = "关联的定价策略列表")
    private List<PricingStrategyVO> pricingStrategies;

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
