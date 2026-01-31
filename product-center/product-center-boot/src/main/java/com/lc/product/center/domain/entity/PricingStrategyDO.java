package com.lc.product.center.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "pricing_strategy", autoResultMap = true)
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
    private String tenantId;

    // ==================== 策略标识 ====================

    /**
     * 策略编码: TIERED_CPU_001
     */
    private String strategyCode;

    /**
     * 策略名称
     */
    private String strategyName;

    // ==================== 策略类型 ====================

    /**
     * 策略类型: LINEAR/TIERED/VOLUME_DISCOUNT/REGION/PROMOTION
     */
    private String strategyType;

    // ==================== 应用范围 ====================

    /**
     * 应用范围: ALL/SKU/PRODUCT_LINE
     */
    private String applyScope;

    /**
     * 范围值: SKU编码或产品线
     */
    private String applyScopeValue;

    // ==================== 策略配置 ====================

    /**
     * 策略配置(阶梯区间/区域系数等)
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> strategyConfig;

    // ==================== 优先级 ====================

    /**
     * 优先级
     */
    private Integer priority;

    // ==================== 时间有效性 ====================

    /**
     * 生效时间
     */
    private Date effectiveTime;

    /**
     * 失效时间
     */
    private Date expiryTime;

    // ==================== 状态 ====================

    /**
     * 状态: ACTIVE/INACTIVE
     */
    private String status;

    /**
     * 备注说明
     */
    private String remark;

    // ==================== 审计字段 ====================

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date dtCreated;

    /**
     * 更新者
     */
    private String modifiedBy;

    /**
     * 更新时间
     */
    private Date dtModified;
}
