package com.lc.product.center.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 产品SKU表(product_center.product_sku)表实体类
 * 可售卖单元
 *
 * @author lucheng
 * @since 2025-12-21
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

    // ==================== 关联产品 ====================

    /**
     * 所属产品编码
     */
    @TableField("product_code")
    private String productCode;

    /**
     * 所属规格族编码
     */
    @TableField("sub_product_code")
    private String subProductCode;

    // ==================== SKU类型 ====================

    /**
     * SKU类型: INSTANCE/ADDON/BUNDLE/SUBSCRIPTION
     */
    @TableField("sku_type")
    private String skuType;

    // ==================== 售卖控制 ====================

    /**
     * 是否可售: 1是 0否
     */
    @TableField("saleable")
    private Integer saleable;

    /**
     * 是否可见: 1是 0否
     */
    @TableField("visible")
    private Integer visible;

    /**
     * 配额限制，NULL表示无限制
     */
    @TableField("quota_limit")
    private Integer quotaLimit;

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
    private Integer deleted;

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
