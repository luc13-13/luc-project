package com.lc.product.center.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private String tenantId;

    // ==================== 关联定价模板 ====================

    /**
     * 关联sku_pricing.pricing_code
     */
    private String pricingCode;

    /**
     * 关联sku_pricing.revision
     */
    private String pricingRevision;

    // ==================== 关联策略模板 ====================

    /**
     * 关联pricing_strategy.strategy_code
     */
    private String strategyCode;

    // ==================== 阶梯区间 ====================

    /**
     * 区间起始
     */
    private BigDecimal rangeStart;

    /**
     * 区间结束(NULL为无穷大)
     */
    private BigDecimal rangeEnd;

    // ==================== 价格 ====================

    /**
     * 阶梯单价
     */
    private BigDecimal unitPrice;

    /**
     * 固定附加费/起步价
     */
    private BigDecimal fixedAmount;

    // ==================== 排序 ====================

    /**
     * 排序
     */
    private Integer sortOrder;

    // ==================== 审计字段 ====================

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date dtCreated;
}
