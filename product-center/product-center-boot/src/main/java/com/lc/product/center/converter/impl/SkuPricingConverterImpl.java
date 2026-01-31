package com.lc.product.center.converter.impl;

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
        entity.setPricingStrategyCode(dto.getPricingStrategyCode());
        entity.setBillingStrategyCode(dto.getBillingStrategyCode());
        entity.setRefundPolicy(dto.getRefundPolicy());
        entity.setUnitPrice(dto.getUnitPrice());
        entity.setOriginalPrice(dto.getOriginalPrice());
        entity.setSalePrice(dto.getSalePrice());
        entity.setCurrency(dto.getCurrency());
        entity.setDiscountRate(dto.getDiscountRate());
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
                .pricingStrategyCode(entity.getPricingStrategyCode())
                .billingStrategyCode(entity.getBillingStrategyCode())
                .refundPolicy(entity.getRefundPolicy())
                .unitPrice(entity.getUnitPrice())
                .originalPrice(entity.getOriginalPrice())
                .salePrice(entity.getSalePrice())
                .currency(entity.getCurrency())
                .discountRate(entity.getDiscountRate())
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
        vo.setMeteringModeDesc(getMeteringModeDesc(entity.getMeteringMode()));
        vo.setPaymentModeDesc(getPaymentModeDesc(entity.getPaymentMode()));
        vo.setBillingCycleDesc(getBillingCycleDesc(entity.getBillingCycle()));
        vo.setBillingUnitDesc(getBillingUnitDesc(entity.getBillingUnit()));
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

    private String getMeteringModeDesc(String meteringMode) {
        if (meteringMode == null) {
            return null;
        }
        return switch (meteringMode) {
            case "BY_USAGE" -> "按用量";
            case "BY_QUOTA" -> "按配额";
            default -> meteringMode;
        };
    }

    private String getPaymentModeDesc(String paymentMode) {
        if (paymentMode == null) {
            return null;
        }
        return switch (paymentMode) {
            case "POSTPAID" -> "后付费";
            case "PREPAID" -> "预付费";
            case "SUBSCRIPTION" -> "订阅制";
            default -> paymentMode;
        };
    }

    private String getBillingCycleDesc(String billingCycle) {
        if (billingCycle == null) {
            return null;
        }
        return switch (billingCycle) {
            case "HOURLY" -> "按小时";
            case "DAILY" -> "按天";
            case "MONTHLY" -> "按月";
            case "QUARTERLY" -> "按季度";
            case "YEARLY" -> "按年";
            case "ONCE" -> "一次性";
            default -> billingCycle;
        };
    }

    private String getBillingUnitDesc(String billingUnit) {
        if (billingUnit == null) {
            return null;
        }
        return switch (billingUnit) {
            case "PERIOD" -> "按周期";
            case "QUANTITY" -> "按数量";
            default -> billingUnit;
        };
    }
}
