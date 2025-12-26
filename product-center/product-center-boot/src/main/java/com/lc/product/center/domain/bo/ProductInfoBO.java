package com.lc.product.center.domain.bo;

import com.lc.framework.core.constants.NumberConstants;
import com.lc.product.center.constants.ProductStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品信息业务对象
 * 承载业务逻辑中的参数封装
 *
 * @author lucheng
 * @since 2025-12-26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfoBO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 租户ID
     */
    private String tenantId;

    // ==================== 四层产品结构 ====================

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 规格族编码
     */
    private String subProductCode;

    /**
     * 计费项编码
     */
    private String billingItemCode;

    /**
     * 计费规格编码
     */
    private String subBillingItemCode;

    // ==================== 名称 ====================

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 规格族名称
     */
    private String subProductName;

    /**
     * 计费项名称
     */
    private String billingItemName;

    /**
     * 计费规格名称
     */
    private String subBillingItemName;

    // ==================== 规格属性 ====================

    /**
     * 规格值
     */
    private BigDecimal specValue;

    /**
     * 规格单位
     */
    private String specUnit;

    // ==================== 计费属性 ====================

    /**
     * 基准单价
     */
    private BigDecimal basePrice;

    /**
     * 价格系数
     */
    private BigDecimal priceFactor;

    /**
     * 计量单位
     */
    private String meteringUnit;

    // ==================== 业务计算字段 ====================

    /**
     * 计算后的单价 = basePrice * priceFactor
     */
    private BigDecimal unitPrice;

    // ==================== 状态与排序 ====================

    /**
     * 状态
     */
    private String status;

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

    /**
     * 更新者
     */
    private String modifiedBy;

    /**
     * 更新时间
     */
    private Date dtModified;

    /**
     * 逻辑删除
     */
    private Integer deleted;

    // ==================== 业务方法 ====================

    /**
     * 计算单价
     */
    public void calculateUnitPrice() {
        if (this.basePrice != null && this.priceFactor != null) {
            this.unitPrice = this.basePrice.multiply(this.priceFactor);
        } else if (this.basePrice != null) {
            this.unitPrice = this.basePrice;
        }
    }

    /**
     * 是否生效
     */
    public boolean isActive() {
        return ProductStatusEnum.ACTIVE.getCode().equals(this.status) && (this.deleted == null || this.deleted == NumberConstants.STATUS_FALSE.intValue());
    }
}
