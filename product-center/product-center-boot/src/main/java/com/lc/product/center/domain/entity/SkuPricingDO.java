package com.lc.product.center.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 定价模板表(收费模式模板,可复用)(product_center.sku_pricing)表实体类
 *
 * @author lucheng
 * @since 2026-02-06
 */
@Data
@TableName(schema = "product_center", value = "sku_pricing")
public class SkuPricingDO implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;

    /**
     * 定价编码: PAY_AS_GO_HOURLY/PREPAID_MONTHLY
     */
    @TableField("pricing_code")
    private String pricingCode;

    /**
     * 定价版本号: yyyyMMddHHmmss
     */
    @TableField("revision")
    private String revision;

    /**
     * 计量方式: BY_USAGE/BY_QUOTA
     */
    @TableField("metering_mode")
    private String meteringMode;

    /**
     * 付费方式: POSTPAID/PREPAID/SUBSCRIPTION
     */
    @TableField("payment_mode")
    private String paymentMode;

    /**
     * 计费周期: HOURLY/DAILY/MONTHLY/QUARTERLY/YEARLY/ONCE
     */
    @TableField("billing_cycle")
    private String billingCycle;

    /**
     * 周期数量: 1月/3月/12月
     */
    @TableField("cycle_count")
    private Integer cycleCount;

    /**
     * 计费单位类型: PERIOD/QUANTITY
     */
    @TableField("billing_unit")
    private String billingUnit;

    /**
     * 退款政策: PRO_RATA/NON_REFUNDABLE
     */
    @TableField("refund_policy")
    private String refundPolicy;

    /**
     * 折扣率: 0.85表示85折
     */
    @TableField("discount_rate")
    private BigDecimal discountRate;

    /**
     * 币种
     */
    @TableField("currency")
    private String currency;

    /**
     * 计量单位: 核·小时/GB·月/次
     */
    @TableField("metering_unit")
    private String meteringUnit;

    /**
     * 计量精度: 小数位数
     */
    @TableField("metering_precision")
    private Integer meteringPrecision;

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
     * 是否当前主版本: 1是 0否
     */
    @TableField("is_current")
    private Boolean isCurrent;

    /**
     * 优先级(数值越大优先级越高)
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 状态: ACTIVE/INACTIVE
     */
    @TableField("status")
    private String status;

    /**
     * 备注说明
     */
    @TableField("remark")
    private String remark;

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