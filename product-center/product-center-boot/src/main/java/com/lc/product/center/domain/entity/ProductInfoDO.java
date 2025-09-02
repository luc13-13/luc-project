package com.lc.product.center.domain.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 产品信息表(product_center.product_info)表实体类
 *
 * @author lucheng
 * @since 2025-08-31
 */
@Data
@TableName("product_center.product_info")
public class ProductInfoDO implements Serializable {
/**
     * 主键id
     */     
    @TableId("id")
    private Long id;

/**
     * 产品code
     */    
    @TableField("product_code")
    private String productCode;
    
/**
     * 子产品code
     */    
    @TableField("sub_product_code")
    private String subProductCode;
    
/**
     * 计费项code
     */    
    @TableField("billing_item_code")
    private String billingItemCode;
    
/**
     * 子计费项code
     */    
    @TableField("sub_billing_item_code")
    private String subBillingItemCode;
    
/**
     * 产品名称
     */    
    @TableField("product_name")
    private String productName;
    
/**
     * 子产品名称
     */    
    @TableField("sub_product_name")
    private String subProductName;
    
/**
     * 计费项名称
     */    
    @TableField("billing_item_name")
    private String billingItemName;
    
/**
     * 子计费项名称
     */    
    @TableField("sub_billing_item_name")
    private String subBillingItemName;
    
/**
     * 单位，个、次、GB等
     */    
    @TableField("unit")
    private String unit;
    
/**
     * 用户邮箱, 用于登陆
     */    
    @TableField("price")
    private BigDecimal price;
    
/**
     * 手机号码, 用于登陆
     */    
    @TableField("charge_size")
    private BigDecimal chargeSize;
    
/**
     * 生效状态（1生效 0实效）
     */    
    @TableField("status")
    private Short status;
    
/**
     * 创建者
     */    
    @TableField("created_by")
    private String createdBy;
    
/**
     * 创建时间
     */    
    @TableField("dt_created")
    private Date dtCreated;
    
/**
     * 更新者
     */    
    @TableField("modified_by")
    private String modifiedBy;
    
/**
     * 更新时间
     */    
    @TableField("dt_modified")
    private Date dtModified;
    
/**
     * 备注
     */    
    @TableField("remark")
    private String remark;
    
/**
     * 逻辑删除(0:未删除 1:已删除)
     */    
    @TableField("deleted")
    private Short deleted;
    
}

