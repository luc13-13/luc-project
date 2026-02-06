package com.lc.product.center.converter.impl;

import com.lc.product.center.constants.ProductStatusEnum;
import com.lc.product.center.converter.SkuPricingLinkConverter;
import com.lc.product.center.domain.dto.SkuPricingLinkDTO;
import com.lc.product.center.domain.entity.SkuPricingLinkDO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * SKU与定价关联表(product_center.sku_pricing_link)表对象转换接口
 *
 * @author lucheng
 * @since 2026-02-06
 */
@Service("skuPricingLinkConverter")
public class SkuPricingLinkConverterImpl implements SkuPricingLinkConverter {

    @Override
    public SkuPricingLinkDO convertDTO2DO(SkuPricingLinkDTO dto) {
        SkuPricingLinkDO entity = new SkuPricingLinkDO();
        entity.setTenantId(dto.getTenantId());
        entity.setSkuCode(dto.getSkuCode());
        entity.setSkuRevision(dto.getSkuRevision());
        entity.setPricingCode(dto.getPricingCode());
        entity.setPricingRevision(dto.getPricingRevision());
        entity.setOverrideFactor(dto.getOverrideFactor());
        entity.setIsDefault(dto.getIsDefault());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    @Override
    public List<SkuPricingLinkDO> convertDTO2DO(List<SkuPricingLinkDTO> dto) {
        if(CollectionUtils.isEmpty(dto)) {
            return List.of();
        }
        return dto.stream().map(this::convertDTO2DO).toList();
    }

    @Override
    public List<SkuPricingLinkDO> convertDTO2DOForCreate(List<SkuPricingLinkDTO> dto) {
        if(CollectionUtils.isEmpty(dto)) {
            return List.of();
        }
        return dto.stream().map(it -> {
            SkuPricingLinkDO linkDO = this.convertDTO2DO(it);
            if (!StringUtils.hasText(linkDO.getStatus())) {
                linkDO.setStatus(ProductStatusEnum.ACTIVE.getCode());
            }
            return linkDO;
        }).toList();
    }
}

