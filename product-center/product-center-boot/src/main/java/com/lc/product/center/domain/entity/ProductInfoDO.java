package com.lc.product.center.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品资源信息主档(四层结构)(product_center.product_info)表实体类
 *
 * @author lucheng
 * @since 2026-02-06
 */
@Data
@TableName(schema = "product_center", value = "product_info")
public class ProductInfoDO implements Serializable {
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
     * 产品编码: CVM/CBS/CLB
     */
    @TableField("product_code")
    private String productCode;

    /**
     * 规格族编码: S5_GENERAL/C6_COMPUTE
     */
    @TableField("sub_product_code")
    private String subProductCode;

    /**
     * 计费项编码: CPU/MEMORY/STORAGE
     */
    @TableField("billing_item_code")
    private String billingItemCode;

    /**
     * 计费规格编码: INTEL_4C/HYGON_4C
     */
    @TableField("sub_billing_item_code")
    private String subBillingItemCode;

    /**
     * 产品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 规格族名称
     */
    @TableField("sub_product_name")
    private String subProductName;

    /**
     * 计费项名称
     */
    @TableField("billing_item_name")
    private String billingItemName;

    /**
     * 计费规格名称
     */
    @TableField("sub_billing_item_name")
    private String subBillingItemName;

    /**
     * 规格值: 4, 8, 100
     */
    @TableField("spec_value")
    private BigDecimal specValue;

    /**
     * 规格单位: 核, GB, Mbps
     */
    @TableField("spec_unit")
    private String specUnit;

    /**
     * 计量展示单位: 核·小时, GB·月
     */
    @TableField("metering_unit")
    private String meteringUnit;

    /**
     * 状态: DRAFT/ACTIVE/INACTIVE
     */
    @TableField("status")
    private String status;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 逻辑删除: 0未删除 1已删除
     */
    @TableLogic
    @TableField("deleted")
    private Boolean deleted;

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
