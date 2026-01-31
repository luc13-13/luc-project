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
 * SKU与定价关联表(product_center.sku_pricing_link)表实体类
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Data
@TableName("sku_pricing_link")
public class SkuPricingLinkDO implements Serializable {

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

    // ==================== 定价关联 ====================

    /**
     * 定价编码
     */
    @TableField("pricing_code")
    private String pricingCode;

    /**
     * 定价版本号
     */
    @TableField("pricing_revision")
    private String pricingRevision;

    // ==================== 覆盖配置 ====================

    /**
     * 覆盖系数(可选)
     */
    @TableField("override_factor")
    private BigDecimal overrideFactor;

    /**
     * 是否默认收费模式: 1是 0否
     */
    @TableField("is_default")
    private Short isDefault;

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
