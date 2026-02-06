package com.lc.product.center.converter.impl;

import com.lc.product.center.constants.PricingConstants;
import com.lc.product.center.constants.ProductStatusEnum;
import com.lc.product.center.converter.PricingStrategyConverter;
import com.lc.product.center.domain.dto.PricingStrategyDTO;
import com.lc.product.center.domain.entity.PricingStrategyDO;
import com.lc.product.center.domain.vo.PricingStrategyVO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <pre>
 * 
 * <pre/>
 * 
 * @author : Lu Cheng
 * @date : 3/2/26 09:50
 * @version : 1.0
 */
@Service
public class PricingStrategyConverterImpl implements PricingStrategyConverter {
    @Override
    public List<PricingStrategyVO> convertDO2VOList(List<PricingStrategyDO> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return List.of();
        }
        return entityList.stream().map(this::convertDO2VO).collect(Collectors.toList());
    }

    @Override
    public PricingStrategyDO convertDTO2DO(PricingStrategyDTO dto) {
        PricingStrategyDO entity = new PricingStrategyDO();
        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setStrategyCode(dto.getStrategyCode());
        entity.setStrategyName(dto.getStrategyName());
        entity.setStrategyType(dto.getStrategyType());
        entity.setApplyScope(dto.getApplyScope());
        entity.setApplyScopeValue(dto.getApplyScopeValue());
        entity.setCalcMethod(dto.getCalcMethod());
        entity.setPriority(dto.getPriority());
        entity.setEffectiveTime(dto.getEffectiveTime());
        entity.setExpiryTime(dto.getExpiryTime());
        entity.setStatus(dto.getStatus());
        entity.setRemark(dto.getRemark());
        return entity;
    }

    @Override
    public PricingStrategyVO convertDO2VO(PricingStrategyDO entity) {
        if (entity == null) {
            return null;
        }
        return PricingStrategyVO.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .strategyCode(entity.getStrategyCode())
                .strategyName(entity.getStrategyName())
                .strategyType(entity.getStrategyType())
                .strategyTypeDesc(PricingConstants.PricingStrategyTypeEnum.getDescByType(entity.getStrategyType()))
                .applyScope(entity.getApplyScope())
                .applyScopeDesc(PricingConstants.StrategyApplyScopeEnum.getDescByScope(entity.getApplyScope()))
                .applyScopeValue(entity.getApplyScopeValue())
                .calcMethod(entity.getCalcMethod())
                .priority(entity.getPriority())
                .effectiveTime(entity.getEffectiveTime())
                .expiryTime(entity.getExpiryTime())
                .status(entity.getStatus())
                .statusDesc(ProductStatusEnum.getDescByCode(entity.getStatus()))
                .remark(entity.getRemark())
                .dtCreated(entity.getDtCreated())
                .dtModified(entity.getDtModified())
                .build();
    }
}
