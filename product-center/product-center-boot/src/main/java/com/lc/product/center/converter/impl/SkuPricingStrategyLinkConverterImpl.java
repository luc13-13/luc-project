package com.lc.product.center.converter.impl;

import com.lc.product.center.converter.SkuPricingStrategyLinkConverter;
import com.lc.product.center.domain.dto.SkuPricingStrategyLinkDTO;
import com.lc.product.center.domain.entity.SkuPricingStrategyLinkDO;
import org.springframework.stereotype.Service;

/**
 * SKU与策略关联表(支持多策略叠加)(product_center.sku_pricing_strategy_link)表对象转换接口
 *
 * @author lucheng
 * @since 2026-02-06
 */
@Service("skuPricingStrategyLinkConverter")
public class SkuPricingStrategyLinkConverterImpl implements SkuPricingStrategyLinkConverter {

    @Override
    public SkuPricingStrategyLinkDO convertDTO2DO(SkuPricingStrategyLinkDTO dto) {
        SkuPricingStrategyLinkDO entity = new SkuPricingStrategyLinkDO();
        entity.setTenantId(dto.getTenantId());
        entity.setSkuCode(dto.getSkuCode());
        entity.setSkuRevision(dto.getSkuRevision());
        entity.setStrategyCode(dto.getStrategyCode());
        entity.setPriority(dto.getPriority());
        entity.setEffectiveTime(dto.getEffectiveTime());
        entity.setExpiryTime(dto.getExpiryTime());
        entity.setStatus(dto.getStatus());
        return entity;
    }
}

