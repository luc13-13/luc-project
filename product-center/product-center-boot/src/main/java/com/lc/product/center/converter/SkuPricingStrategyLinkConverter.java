package com.lc.product.center.converter;

import com.lc.product.center.domain.dto.SkuPricingStrategyLinkDTO;
import com.lc.product.center.domain.entity.SkuPricingStrategyLinkDO;

/**
 * SKU与策略关联表(支持多策略叠加)(product_center.sku_pricing_strategy_link)表对象转换接口
 *
 * @author lucheng
 * @since 2026-02-06
 */
public interface SkuPricingStrategyLinkConverter {

    /**
     * 转换DTO为数据库对象
     * @param dto 请求参数
     * @return 数据库对象
     */
    SkuPricingStrategyLinkDO convertDTO2DO(SkuPricingStrategyLinkDTO dto);

}

