package com.lc.product.center.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 定价策略参数细目表(product_center.pricing_strategy_param)实体类
 * 用于存储阶梯定价的区间配置
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Data
@TableName("pricing_strategy_param")
public class PricingStrategyParamDO implements Serializable {

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

    // ==================== 关联策略模板 ====================

    /**
     * 关联pricing_strategy.strategy_code
     */
    @TableField("strategy_code")
    private String strategyCode;

    // ==================== 参数类型 ====================

    /**
     * 参数类型: TIER/CAP/FLOOR/THRESHOLD/RATE/FIXED
     */
    @TableField("param_type")
    private String paramType;

    // ==================== 阶梯区间 ====================

    /**
     * 区间起始
     */
    @TableField("range_start")
    private BigDecimal rangeStart;

    /**
     * 区间结束(NULL为无穷大)
     */
    @TableField("rang_end")
    private BigDecimal rangeEnd;

    // ==================== 参数值 ====================

    /**
     * 通用参数值(阶梯单价/折扣率/固定金额等)
     */
    @TableField("value")
    private BigDecimal value;

    // ==================== 排序 ====================

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

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
}
