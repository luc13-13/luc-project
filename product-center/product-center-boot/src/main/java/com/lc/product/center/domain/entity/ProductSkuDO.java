package com.lc.product.center.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品SKU表(product_center.product_sku)表实体类
 * 售卖单元(版本化+基准定价)
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Data
@TableName("product_sku")
public class ProductSkuDO implements Serializable {

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

    // ==================== SKU基本信息 ====================

    /**
     * SKU编码: CVM-S5-4C8G
     */
    @TableField("sku_code")
    private String skuCode;

    /**
     * SKU名称: 通用型S5 4核8G
     */
    @TableField("sku_name")
    private String skuName;

    // ==================== 版本控制 ====================

    /**
     * SKU版本号: yyyyMMddHHmmss
     */
    @TableField("revision")
    private String revision;

    // ==================== SKU类型 ====================

    /**
     * SKU类型: INSTANCE/ADDON/BUNDLE/SUBSCRIPTION
     */
    @TableField("sku_type")
    private String skuType;

    // ==================== 基准定价 ====================

    /**
     * 基准单价
     */
    @TableField("base_unit_price")
    private BigDecimal baseUnitPrice;

    /**
     * 币种
     */
    @TableField("currency")
    private String currency;

    // ==================== 售卖控制 ====================

    /**
     * 是否可售: 1是 0否
     */
    @TableField("saleable")
    private Short saleable;

    /**
     * 是否可见: 1是 0否
     */
    @TableField("visible")
    private Short visible;

    /**
     * 配额限制，NULL表示无限制
     */
    @TableField("quota_limit")
    private Integer quotaLimit;

    // ==================== 版本状态 ====================

    /**
     * 是否当前主版本: 1是 0否
     */
    @TableField("is_current")
    private Short isCurrent;

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
     * 状态: DRAFT/ACTIVE/INACTIVE
     */
    @TableField("status")
    private String status;

    /**
     * 上架时间
     */
    @TableField("publish_time")
    private Date publishTime;

    /**
     * 逻辑删除(0:未删除 1:已删除)
     */
    @TableField("deleted")
    @TableLogic
    private Short deleted;

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
