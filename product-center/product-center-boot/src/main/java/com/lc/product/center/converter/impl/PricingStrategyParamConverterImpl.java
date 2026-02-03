package com.lc.product.center.converter.impl;

import com.lc.product.center.converter.PricingStrategyParamConverter;
import com.lc.product.center.domain.dto.PricingStrategyParamDTO;
import com.lc.product.center.domain.entity.PricingStrategyParamDO;
import com.lc.product.center.domain.vo.PricingStrategyParamVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2/2/26 09:55
 * @version : 1.0
 */
@Service
public class PricingStrategyParamConverterImpl implements PricingStrategyParamConverter {
    @Override
    public List<PricingStrategyParamVO> convertDO2VOList(List<PricingStrategyParamDO> params) {
        return List.of();
    }

    @Override
    public List<PricingStrategyParamDO> convertDTO2DOList(List<PricingStrategyParamDTO> dtoList) {
        return List.of();
    }

    @Override
    public PricingStrategyParamDO convertDTO2DO(PricingStrategyParamDTO dto) {
        PricingStrategyParamDO entity = new PricingStrategyParamDO();
        entity.setTenantId(dto.getTenantId());
        entity.setStrategyCode(dto.getStrategyCode());
        entity.setRangeStart(dto.getRangeStart());
        entity.setRangeEnd(dto.getRangeEnd());
        entity.setUnitPrice(dto.getUnitPrice());
        entity.setFixedAmount(dto.getFixedAmount() != null ? dto.getFixedAmount() : BigDecimal.ZERO);
        entity.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        return entity;
    }
}
