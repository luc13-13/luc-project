package com.lc.product.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.product.center.domain.entity.SkuPricingStrategyLinkDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * SKU与策略关联表(product_center.sku_pricing_strategy_link)Mapper接口
 *
 * @author lucheng
 * @since 2026-02-05
 */
@Mapper
public interface SkuPricingStrategyLinkMapper extends BaseMapper<SkuPricingStrategyLinkDO> {
    // 继承 BaseMapper 提供的 CRUD 方法，无需自定义 SQL
}
