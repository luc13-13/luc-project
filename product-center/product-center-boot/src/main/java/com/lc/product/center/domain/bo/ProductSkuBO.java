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
import java.util.List;

/**
 * 产品SKU业务对象
 * 承载业务逻辑中的参数封装
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSkuBO implements Serializable {

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

    // ==================== SKU基本信息 ====================

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * SKU名称
     */
    private String skuName;

    // ==================== 版本控制 ====================

    /**
     * SKU版本号
     */
    private String revision;

    // ==================== SKU类型 ====================

    /**
     * SKU类型
     */
    private String skuType;

    // ==================== 基准定价 ====================

    /**
     * 基准单价
     */
    private BigDecimal baseUnitPrice;

    /**
     * 币种
     */
    private String currency;

    // ==================== 售卖控制 ====================

    /**
     * 是否可售
     */
    private Short saleable;

    /**
     * 是否可见
     */
    private Short visible;

    /**
     * 配额限制
     */
    private Integer quotaLimit;

    // ==================== 版本状态 ====================

    /**
     * 是否当前主版本
     */
    private Short isCurrent;

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
     * 状态
     */
    private String status;

    /**
     * 上架时间
     */
    private Date publishTime;

    // ==================== 关联信息（通过BOM聚合） ====================

    /**
     * 所属产品编码（从BOM查询）
     */
    private String productCode;

    /**
     * 产品名称（关联查询）
     */
    private String productName;

    /**
     * 所属规格族编码（从BOM查询）
     */
    private String subProductCode;

    /**
     * 规格族名称（关联查询）
     */
    private String subProductName;

    // ==================== 业务聚合字段 ====================

    /**
     * 计费项列表（业务聚合）
     */
    private List<ProductInfoBO> billingItems;

    /**
     * 按量小时价（业务计算）
     */
    private BigDecimal hourlyPrice;

    /**
     * 包月价格（业务计算）
     */
    private BigDecimal monthlyPrice;

    /**
     * 包年价格（业务计算）
     */
    private BigDecimal yearlyPrice;

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
    private Short deleted;

    // ==================== 业务方法 ====================

    /**
     * 是否可售
     */
    public boolean isSaleable() {
        return ProductStatusEnum.ACTIVE.getCode().equals(this.status)
                && NumberConstants.STATUS_TRUE.intValue() == this.saleable
                && NumberConstants.STATUS_TRUE.intValue() == this.visible
                && (this.deleted == null || this.deleted == NumberConstants.STATUS_FALSE.intValue());
    }

    /**
     * 是否已上架
     */
    public boolean isPublished() {
        return ProductStatusEnum.ACTIVE.getCode().equals(this.status) && this.publishTime != null;
    }

    /**
     * 是否当前版本
     */
    public boolean isCurrentVersion() {
        return this.isCurrent != null && this.isCurrent == NumberConstants.STATUS_TRUE.shortValue();
    }

    /**
     * 计算按量小时价（基于计费项列表）
     */
    public void calculateHourlyPrice() {
        if (this.billingItems != null && !this.billingItems.isEmpty()) {
            this.hourlyPrice = this.billingItems.stream()
                    .filter(ProductInfoBO::isActive)
                    .map(item -> {
                        item.calculateUnitPrice();
                        return item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO;
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    /**
     * 检查配额是否可用
     */
    public boolean isQuotaAvailable(int currentUsage) {
        if (this.quotaLimit == null) {
            return true; // 无限制
        }
        return currentUsage < this.quotaLimit;
    }
}
