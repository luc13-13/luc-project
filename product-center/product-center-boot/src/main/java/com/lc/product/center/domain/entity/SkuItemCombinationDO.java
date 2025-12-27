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
 * SKU计费项组合表(product_center.sku_item_combination)表实体类
 *
 * @author lucheng
 * @since 2025-12-27
 */
@Data
@TableName("sku_item_combination")
public class SkuItemCombinationDO implements Serializable {

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

    /**
     * SKU编码
     */
    @TableField("sku_code")
    private String skuCode;

    /**
     * 产品编码
     */
    @TableField("product_code")
    private String productCode;

    /**
     * 规格族编码
     */
    @TableField("sub_product_code")
    private String subProductCode;

    /**
     * 计费项编码
     */
    @TableField("billing_item_code")
    private String billingItemCode;

    /**
     * 计费规格编码
     */
    @TableField("sub_billing_item_code")
    private String subBillingItemCode;

    /**
     * 数量/份数
     */
    @TableField("quantity")
    private BigDecimal quantity;

    /**
     * 是否计入SKU定价: 1是 0否
     */
    @TableField("pricing_included")
    private Short pricingIncluded;

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
