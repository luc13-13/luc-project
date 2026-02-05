package com.lc.product.center.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * SKU与策略关联表(product_center.sku_pricing_strategy_link)实体类
 * 支持多策略叠加
 *
 * @author lucheng
 * @since 2026-02-05
 */
@Data
@TableName("sku_pricing_strategy_link")
public class SkuPricingStrategyLinkDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;

    // ==================== SKU关联 ====================

    /**
     * SKU编码
     */
    @TableField("sku_code")
    private String skuCode;

    /**
     * SKU版本号
     */
    @TableField("sku_revision")
    private String skuRevision;

    // ==================== 策略关联 ====================

    /**
     * 策略编码
     */
    @TableField("strategy_code")
    private String strategyCode;

    // ==================== 执行优先级 ====================

    /**
     * 优先级(NULL使用策略默认值)
     */
    @TableField("priority")
    private Integer priority;

    // ==================== 有效期 ====================

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

    // ==================== 状态 ====================

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
