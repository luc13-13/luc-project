package com.lc.product.center.converter;

import com.lc.product.center.domain.dto.SkuItemCombinationDTO;
import com.lc.product.center.domain.entity.SkuItemCombinationDO;
import com.lc.product.center.domain.vo.SkuItemCombinationVO;

import java.util.List;

/**
 * SKU计费项组合转换器接口
 *
 * @author lucheng
 * @since 2025-12-27
 */
public interface SkuItemCombinationConverter {

    /**
     * DTO转DO（用于更新）
     */
    SkuItemCombinationDO convertDTO2DO(SkuItemCombinationDTO dto);

    /**
     * DTO转DO（用于创建，包含默认值设置）
     */
    SkuItemCombinationDO convertDTO2DOForCreate(SkuItemCombinationDTO dto, String tenantId);

    /**
     * DO转VO
     */
    SkuItemCombinationVO convertDO2VO(SkuItemCombinationDO entity);

    /**
     * DO列表转VO列表
     */
    List<SkuItemCombinationVO> convertDO2VO(List<SkuItemCombinationDO> entities);
}
