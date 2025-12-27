package com.lc.product.center.converter;

import com.lc.product.center.domain.dto.SkuPricingDTO;
import com.lc.product.center.domain.entity.SkuPricingDO;
import com.lc.product.center.domain.vo.SkuPricingVO;

import java.util.List;

/**
 * SKU定价转换器接口
 *
 * @author lucheng
 * @since 2025-12-27
 */
public interface SkuPricingConverter {

    /**
     * DTO转DO（用于更新）
     */
    SkuPricingDO convertDTO2DO(SkuPricingDTO dto);

    /**
     * DTO转DO（用于创建，包含默认值设置）
     */
    SkuPricingDO convertDTO2DOForCreate(SkuPricingDTO dto, String tenantId);

    /**
     * DO转VO
     */
    SkuPricingVO convertDO2VO(SkuPricingDO entity);

    /**
     * DO列表转VO列表
     */
    List<SkuPricingVO> convertDO2VO(List<SkuPricingDO> entities);
}
