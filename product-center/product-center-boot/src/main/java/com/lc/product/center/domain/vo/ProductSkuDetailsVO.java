package com.lc.product.center.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 *     SKU详情
 * <pre/>
 * @author : Lu Cheng
 * @date : 6/2/26 10:48
 * @version : 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ProductSkuDetailsVO", description = "产品SKU详情对象")
public class ProductSkuDetailsVO implements Serializable {
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
    private Boolean saleable;

    /**
     * 是否可见
     */
    @Schema(description = "是否可见")
    private Boolean visible;

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
    private Boolean isCurrent;

    @Schema(description = "历史版本")
    private List<ProductSkuDetailsVO> historicalSkuDetails;

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
