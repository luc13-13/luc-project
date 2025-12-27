package com.lc.product.center.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * SKU定价表(product_center.sku_pricing)表实体类
 *
 * @author lucheng
 * @since 2025-12-27
 */
@Data
@TableName("sku_pricing")
public class SkuPricingDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;

    /**
     * SKU编码
     */
    @TableField("sku_code")
    private String skuCode;

    /**
     * 定价模式: PAY_AS_GO/PREPAID/SUBSCRIPTION
     */
    @TableField("pricing_model")
    private String pricingModel;

    /**
     * 计费周期: HOURLY/DAILY/MONTHLY/QUARTERLY/YEARLY
     */
    @TableField("billing_period")
    private String billingPeriod;

    /**
     * 周期数量
     */
    @TableField("period_count")
    private Integer periodCount;

    /**
     * 原价
     */
    @TableField("original_price")
    private BigDecimal originalPrice;

    /**
     * 售价
     */
    @TableField("sale_price")
    private BigDecimal salePrice;

    /**
     * 币种
     */
    @TableField("currency")
    private String currency;

    /**
     * 折扣率: 0.85表示85折
     */
    @TableField("discount_rate")
    private BigDecimal discountRate;

    /**
     * 生效时间
     */
    @TableField("effective_time")
    private Date effectiveTime;

    /**
     * 失效时间
     */
    @TableField("expiry_time")
    private Date expiryTime;

    /**
     * 优先级，数值越大优先级越高
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 状态: ACTIVE/INACTIVE
     */
    @TableField("status")
    private String status;

    // ==================== 审计字段 ====================

    /**
     * 创建者
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "dt_created", fill = FieldFill.INSERT)
    private Date dtCreated;

    /**
     * 更新者
     */
    @TableField(value = "modified_by", fill = FieldFill.UPDATE)
    private String modifiedBy;

    /**
     * 更新时间
     */
    @TableField(value = "dt_modified", fill = FieldFill.UPDATE)
    private Date dtModified;
}
