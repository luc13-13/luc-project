package com.lc.product.center.converter.impl;

import com.lc.product.center.constants.ProductStatusEnum;
import com.lc.product.center.converter.SkuPricingConverter;
import com.lc.product.center.domain.dto.SkuPricingDTO;
import com.lc.product.center.domain.entity.SkuPricingDO;
import com.lc.product.center.domain.vo.SkuPricingVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * SKU定价转换器实现
 *
 * @author lucheng
 * @since 2025-12-27
 */
@Component
public class SkuPricingConverterImpl implements SkuPricingConverter {

    @Override
    public SkuPricingDO convertDTO2DO(SkuPricingDTO dto) {
        if (dto == null) {
            return null;
        }
        SkuPricingDO entity = new SkuPricingDO();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    @Override
    public SkuPricingDO convertDTO2DOForCreate(SkuPricingDTO dto, String tenantId) {
        if (dto == null) {
            return null;
        }
        SkuPricingDO entity = new SkuPricingDO();
        BeanUtils.copyProperties(dto, entity);

        // 设置默认值
        entity.setTenantId(tenantId);
        if (entity.getStatus() == null) {
            entity.setStatus(ProductStatusEnum.ACTIVE.getCode());
        }
        if (entity.getCurrency() == null) {
            entity.setCurrency("CNY");
        }
        if (entity.getPeriodCount() == null) {
            entity.setPeriodCount(1);
        }
        if (entity.getPriority() == null) {
            entity.setPriority(0);
        }
        if (entity.getDiscountRate() == null) {
            entity.setDiscountRate(BigDecimal.ONE);
        }
        return entity;
    }

    @Override
    public SkuPricingVO convertDO2VO(SkuPricingDO entity) {
        if (entity == null) {
            return null;
        }
        SkuPricingVO vo = new SkuPricingVO();
        BeanUtils.copyProperties(entity, vo);

        // 设置描述字段
        vo.setPricingModelDesc(getPricingModelDesc(entity.getPricingModel()));
        vo.setBillingPeriodDesc(getBillingPeriodDesc(entity.getBillingPeriod()));
        vo.setStatusDesc(ProductStatusEnum.getDescByCode(entity.getStatus()));
        return vo;
    }

    @Override
    public List<SkuPricingVO> convertDO2VO(List<SkuPricingDO> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }
        return entities.stream().map(this::convertDO2VO).toList();
    }

    private String getPricingModelDesc(String pricingModel) {
        if (pricingModel == null) {
            return null;
        }
        return switch (pricingModel) {
            case "PAY_AS_GO" -> "按量付费";
            case "PREPAID" -> "预付费";
            case "SUBSCRIPTION" -> "订阅制";
            default -> pricingModel;
        };
    }

    private String getBillingPeriodDesc(String billingPeriod) {
        if (billingPeriod == null) {
            return null;
        }
        return switch (billingPeriod) {
            case "HOURLY" -> "按小时";
            case "DAILY" -> "按天";
            case "MONTHLY" -> "按月";
            case "QUARTERLY" -> "按季度";
            case "YEARLY" -> "按年";
            default -> billingPeriod;
        };
    }
}
