package com.lc.product.center.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * SKU与策略关联表(支持多策略叠加)(product_center.sku_pricing_strategy_link)表实体类
 *
 * @author lucheng
 * @since 2026-02-06
 */
@Data
@TableName(schema = "product_center", value = "sku_pricing_strategy_link")
public class SkuPricingStrategyLinkDO implements Serializable {
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
     * SKU编码
     */    
    @TableField("sku_code")
    private String skuCode;
    
/**
     * SKU版本号
     */    
    @TableField("sku_revision")
    private String skuRevision;
    
/**
     * 策略编码
     */    
    @TableField("strategy_code")
    private String strategyCode;
    
/**
     * 优先级(NULL使用策略默认值)
     */    
    @TableField("priority")
    private Integer priority;
    
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
     * 状态: ACTIVE/INACTIVE
     */    
    @TableField("status")
    private String status;
    
/**
     * 创建者
w     */
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

