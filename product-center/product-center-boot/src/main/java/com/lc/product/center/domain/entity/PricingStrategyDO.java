package com.lc.product.center.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 定价策略表(product_center.pricing_strategy)实体类
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Data
@TableName("pricing_strategy")
public class PricingStrategyDO implements Serializable {

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

    // ==================== 策略标识 ====================

    /**
     * 策略编码: TIERED_CPU_001
     */
    @TableField("strategy_code")
    private String strategyCode;

    /**
     * 策略名称
     */
    @TableField("strategy_name")
    private String strategyName;

    // ==================== 策略类型 ====================

    /**
     * 策略类型: LINEAR/TIERED/VOLUME_DISCOUNT/REGION/PROMOTION
     */
    @TableField("strategy_type")
    private String strategyType;

    // ==================== 应用范围 ====================

    /**
     * 应用范围: ALL/SKU/PRODUCT_LINE
     */
    @TableField("apply_scope")
    private String applyScope;

    /**
     * 范围值: SKU编码或产品线
     */
    @TableField("apply_scope_value")
    private String applyScopeValue;

    // ==================== 策略配置 ====================

    /**
     * 策略配置(阶梯区间/区域系数等)
     */
    @TableField(value = "strategy_config", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> strategyConfig;

    // ==================== 优先级 ====================

    /**
     * 优先级
     */
    @TableField("priority")
    private Integer priority;

    // ==================== 时间有效性 ====================

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

    /**
     * 备注说明
     */
    @TableField("remark")
    private String remark;

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
