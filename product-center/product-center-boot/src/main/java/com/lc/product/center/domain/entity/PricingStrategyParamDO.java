package com.lc.product.center.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 定价策略参数表(product_center.pricing_strategy_param)表实体类
 *
 * @author lucheng
 * @since 2026-02-06
 */
@Data
@TableName(schema = "product_center", value = "pricing_strategy_param")
public class PricingStrategyParamDO implements Serializable {
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
     * 关联pricing_strategy.strategy_code
     */
    @TableField("strategy_code")
    private String strategyCode;

    /**
     * 参数类型: TIER/CAP/FLOOR/THRESHOLD/RATE/FIXED/QUANTITY_LIMIT
     */
    @TableField("param_type")
    private String paramType;

    /**
     * 区间起始(阶梯/满减门槛)
     */
    @TableField("range_start")
    private BigDecimal rangeStart;

    /**
     * 区间结束(NULL为无穷大)
     */
    @TableField("range_end")
    private BigDecimal rangeEnd;

    /**
     * 参数值(折扣率/金额/单价等)
     */
    @TableField("value")
    private BigDecimal value;

    /**
     * 排序(阶梯顺序)
     */
    @TableField("sort_order")
    private Integer sortOrder;

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
