package com.lc.product.center.converter.impl;

import com.lc.product.center.constants.PricingConstants;
import com.lc.product.center.constants.ProductStatusEnum;
import com.lc.product.center.converter.SkuPricingConverter;
import com.lc.product.center.domain.dto.SkuPricingDTO;
import com.lc.product.center.domain.entity.SkuPricingDO;
import com.lc.product.center.domain.vo.SkuPricingVO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 定价模板转换器实现
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Component
public class SkuPricingConverterImpl implements SkuPricingConverter {

    @Override
    public SkuPricingDO convertDTO2DO(SkuPricingDTO dto) {
        if (dto == null) {
            return null;
        }
        SkuPricingDO entity = new SkuPricingDO();
        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setPricingCode(dto.getPricingCode());
        entity.setRevision(dto.getRevision());
        entity.setMeteringMode(dto.getMeteringMode());
        entity.setPaymentMode(dto.getPaymentMode());
        entity.setBillingCycle(dto.getBillingCycle());
        entity.setCycleCount(dto.getCycleCount());
        entity.setBillingUnit(dto.getBillingUnit());
        entity.setRefundPolicy(dto.getRefundPolicy());
        entity.setDiscountRate(dto.getDiscountRate());
        entity.setCurrency(dto.getCurrency());
        entity.setMeteringUnit(dto.getMeteringUnit());
        entity.setMeteringPrecision(dto.getMeteringPrecision());
        entity.setEffectiveTime(dto.getEffectiveTime());
        entity.setExpiryTime(dto.getExpiryTime());
        entity.setIsCurrent(dto.getIsCurrent());
        entity.setPriority(dto.getPriority());
        entity.setStatus(dto.getStatus());
        entity.setRemark(dto.getRemark());
        return entity;
    }

    @Override
    public SkuPricingDO convertDTO2DOForCreate(SkuPricingDTO dto, String tenantId) {
        if (dto == null) {
            return null;
        }
        SkuPricingDO entity = convertDTO2DO(dto);

        // 设置默认值
        entity.setTenantId(tenantId);
        if (entity.getStatus() == null) {
            entity.setStatus(ProductStatusEnum.ACTIVE.getCode());
        }
        if (entity.getCurrency() == null) {
            entity.setCurrency("CNY");
        }
        if (entity.getCycleCount() == null) {
            entity.setCycleCount(1);
        }
        if (entity.getPriority() == null) {
            entity.setPriority(0);
        }
        if (entity.getDiscountRate() == null) {
            entity.setDiscountRate(BigDecimal.ONE);
        }
        if (entity.getRefundPolicy() == null) {
            entity.setRefundPolicy("PRO_RATA");
        }
        if (entity.getMeteringPrecision() == null) {
            entity.setMeteringPrecision(2);
        }
        return entity;
    }

    @Override
    public SkuPricingVO convertDO2VO(SkuPricingDO entity) {
        if (entity == null) {
            return null;
        }
        SkuPricingVO vo = SkuPricingVO.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .pricingCode(entity.getPricingCode())
                .revision(entity.getRevision())
                .meteringMode(entity.getMeteringMode())
                .paymentMode(entity.getPaymentMode())
                .billingCycle(entity.getBillingCycle())
                .cycleCount(entity.getCycleCount())
                .billingUnit(entity.getBillingUnit())
                .refundPolicy(entity.getRefundPolicy())
                .discountRate(entity.getDiscountRate())
                .currency(entity.getCurrency())
                .meteringUnit(entity.getMeteringUnit())
                .meteringPrecision(entity.getMeteringPrecision())
                .effectiveTime(entity.getEffectiveTime())
                .expiryTime(entity.getExpiryTime())
                .isCurrent(entity.getIsCurrent())
                .priority(entity.getPriority())
                .status(entity.getStatus())
                .remark(entity.getRemark())
                .dtCreated(entity.getDtCreated())
                .dtModified(entity.getDtModified())
                .build();

        // 设置描述字段
        vo.setMeteringModeDesc(PricingConstants.MeteringModeEnum.getDescByScope(entity.getMeteringMode()));
        vo.setPaymentModeDesc(PricingConstants.PaymentModeEnum.getDescByScope(entity.getPaymentMode()));
        vo.setBillingCycleDesc(PricingConstants.BillingCycleEnum.getDescByScope(entity.getBillingCycle()));
        vo.setBillingUnitDesc(PricingConstants.BillingUnitEnum.getDescByScope(entity.getBillingUnit()));
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
}
