package com.lc.product.center.domain.bo;

import com.lc.framework.core.constants.NumberConstants;
import com.lc.product.center.constants.ProductStatusEnum;
import com.lc.product.center.domain.entity.PricingStrategyDO;
import com.lc.product.center.domain.entity.ProductInfoDO;
import com.lc.product.center.domain.entity.ProductSkuDO;
import com.lc.product.center.domain.entity.SkuPricingDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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

    private ProductSkuDO productSkuDO;
    // ==================== 业务聚合字段 ====================

    /**
     * 计费项列表（业务聚合）
     */
    private List<ProductInfoDO> billingItems;

    /**
     * 关联的定价模板列表（嵌套查询）
     */
    private List<SkuPricingDO> pricingTemplates;

    /**
     * 关联的定价策略列表（嵌套查询）
     */
    private List<PricingStrategyDO> pricingStrategies;

    // ==================== 业务方法 ====================

    /**
     * 是否可售
     */
    public boolean isSaleable() {
        return productSkuDO != null && ProductStatusEnum.ACTIVE.getCode().equals(this.productSkuDO.getStatus())
                && NumberConstants.STATUS_TRUE.intValue() == this.productSkuDO.getSaleable()
                && NumberConstants.STATUS_TRUE.intValue() == this.productSkuDO.getVisible()
                && (this.productSkuDO.getDeleted() == null || this.productSkuDO.getDeleted() == NumberConstants.STATUS_FALSE.intValue());
    }

    /**
     * 是否已上架
     */
    public boolean isPublished() {
        return productSkuDO != null && ProductStatusEnum.ACTIVE.getCode().equals(this.productSkuDO.getStatus()) && this.productSkuDO.getPublishTime() != null;
    }

    /**
     * 是否当前版本
     */
    public boolean isCurrentVersion() {
        return productSkuDO != null && this.productSkuDO.getIsCurrent() != null && this.productSkuDO.getIsCurrent() == NumberConstants.STATUS_TRUE.shortValue();
    }

    /**
     * 检查配额是否可用
     */
    public boolean isQuotaAvailable(int currentUsage) {
        return productSkuDO != null && (this.productSkuDO.getQuotaLimit() == null || currentUsage < this.productSkuDO.getQuotaLimit());
    }
}
