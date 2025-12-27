package com.lc.product.center.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.lc.framework.core.constants.StringConstants.STATUS_FALSE;

/**
 * 产品信息表(product_center.product_info)表实体类
 * 四层产品结构 + 计费属性
 *
 * @author lucheng
 * @since 2025-08-31
 */
@Data
@TableName("product_info")
public class ProductInfoDO implements Serializable {

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

    // ==================== 四层产品结构 ====================

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

    // ==================== 名称 ====================

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

    // ==================== 规格属性 ====================

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

    // ==================== 计费属性 ====================

    /**
     * 基准单价
     */
    @TableField("base_price")
    private BigDecimal basePrice;

    /**
     * 价格系数
     */
    @TableField("price_factor")
    private BigDecimal priceFactor;

    /**
     * 计量单位（账单展示）: 核·小时, GB·月
     */
    @TableField("metering_unit")
    private String meteringUnit;

    // ==================== 状态与排序 ====================

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
     * 逻辑删除(0:未删除 1:已删除)
     */
    @TableField("deleted")
    @TableLogic(value = STATUS_FALSE)
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
