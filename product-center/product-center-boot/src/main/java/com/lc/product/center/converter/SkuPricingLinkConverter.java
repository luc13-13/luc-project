package com.lc.product.center.converter;

import com.lc.product.center.domain.dto.SkuPricingLinkDTO;
import com.lc.product.center.domain.entity.SkuPricingLinkDO;

import java.util.List;

/**
 * SKU与定价关联表(product_center.sku_pricing_link)表对象转换接口
 *
 * @author lucheng
 * @since 2026-02-06
 */
public interface SkuPricingLinkConverter {

    /**
     * 转换DTO为数据库对象
     * @param dto 请求参数
     * @return 数据库对象
     */
    SkuPricingLinkDO convertDTO2DO(SkuPricingLinkDTO dto);

    List<SkuPricingLinkDO> convertDTO2DO(List<SkuPricingLinkDTO> dto);

    List<SkuPricingLinkDO> convertDTO2DOForCreate(List<SkuPricingLinkDTO> dto);

}

