package com.lc.product.center.converter.impl;

import com.lc.product.center.converter.SkuItemCombinationConverter;
import com.lc.product.center.domain.dto.SkuItemCombinationDTO;
import com.lc.product.center.domain.entity.SkuItemCombinationDO;
import com.lc.product.center.domain.vo.SkuItemCombinationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * SKU计费项组合转换器实现
 *
 * @author lucheng
 * @since 2025-12-27
 */
@Component
public class SkuItemCombinationConverterImpl implements SkuItemCombinationConverter {

    @Override
    public SkuItemCombinationDO convertDTO2DO(SkuItemCombinationDTO dto) {
        if (dto == null) {
            return null;
        }
        SkuItemCombinationDO entity = new SkuItemCombinationDO();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    @Override
    public SkuItemCombinationDO convertDTO2DOForCreate(SkuItemCombinationDTO dto, String tenantId) {
        if (dto == null) {
            return null;
        }
        SkuItemCombinationDO entity = new SkuItemCombinationDO();
        BeanUtils.copyProperties(dto, entity);

        // 设置默认值
        entity.setTenantId(tenantId);
        if (entity.getQuantity() == null) {
            entity.setQuantity(BigDecimal.ONE);
        }
        if (entity.getPricingIncluded() == null) {
            entity.setPricingIncluded((short) 1); // 默认计入定价
        }
        return entity;
    }

    @Override
    public SkuItemCombinationVO convertDO2VO(SkuItemCombinationDO entity) {
        if (entity == null) {
            return null;
        }
        SkuItemCombinationVO vo = new SkuItemCombinationVO();
        BeanUtils.copyProperties(entity, vo);

        // 设置描述字段
        vo.setPricingIncludedDesc(entity.getPricingIncluded() != null && entity.getPricingIncluded() == 1 ? "是" : "否");
        return vo;
    }

    @Override
    public List<SkuItemCombinationVO> convertDO2VO(List<SkuItemCombinationDO> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }
        return entities.stream().map(this::convertDO2VO).toList();
    }
}
