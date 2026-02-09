package com.lc.product.center.converter.impl;

import com.lc.product.center.constants.ProductStatusEnum;
import com.lc.product.center.constants.SkuConstants;
import com.lc.product.center.converter.PricingStrategyConverter;
import com.lc.product.center.converter.ProductInfoConverter;
import com.lc.product.center.converter.ProductSkuConverter;
import com.lc.product.center.converter.SkuPricingConverter;
import com.lc.product.center.domain.bo.ProductSkuBO;
import com.lc.product.center.domain.dto.ProductSkuDTO;
import com.lc.product.center.domain.entity.ProductSkuDO;
import com.lc.product.center.domain.vo.ProductSkuDetailsVO;
import com.lc.product.center.domain.vo.ProductSkuVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品SKU表对象转换实现类
 *
 * @author lucheng
 * @since 2026-01-31
 */
@AllArgsConstructor
@Service("productSkuConverter")
public class ProductSkuConverterImpl implements ProductSkuConverter {

    private final ProductInfoConverter productInfoConverter;

    private final SkuPricingConverter skuPricingConverter;

    private final PricingStrategyConverter pricingStrategyConverter;

    // ==================== 简单转换实现 ====================

    @Override
    public ProductSkuDO convertDTO2DO(ProductSkuDTO dto) {
        if (dto == null) {
            return null;
        }
        ProductSkuDO entity = new ProductSkuDO();
        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setSkuCode(dto.getSkuCode());
        entity.setSkuName(dto.getSkuName());
        entity.setRevision(dto.getRevision());
        entity.setSkuType(dto.getSkuType());
        entity.setBaseUnitPrice(dto.getBaseUnitPrice());
        entity.setCurrency(dto.getCurrency());
        entity.setSaleable(dto.getSaleable());
        entity.setVisible(dto.getVisible());
        entity.setQuotaLimit(dto.getQuotaLimit());
        entity.setIsCurrent(dto.getIsCurrent());
        entity.setEffectiveTime(dto.getEffectiveTime());
        entity.setExpiryTime(dto.getExpiryTime());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    @Override
    public ProductSkuVO convertDO2VO(ProductSkuDO entity) {
        if (entity == null) {
            return null;
        }

        return ProductSkuVO.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .skuCode(entity.getSkuCode())
                .skuName(entity.getSkuName())
                .revision(entity.getRevision())
                .skuType(entity.getSkuType())
                .skuTypeDesc(SkuConstants.SkuTypeEnum.getDescByCode(entity.getSkuType()))
                .baseUnitPrice(entity.getBaseUnitPrice())
                .currency(entity.getCurrency())
                .saleable(entity.getSaleable())
                .visible(entity.getVisible())
                .quotaLimit(entity.getQuotaLimit())
                .isCurrent(entity.getIsCurrent())
                .effectiveTime(entity.getEffectiveTime())
                .expiryTime(entity.getExpiryTime())
                .status(entity.getStatus())
                .statusDesc(ProductStatusEnum.getDescByCode(entity.getStatus()))
                .publishTime(entity.getPublishTime())
                .createdBy(entity.getCreatedBy())
                .dtCreated(entity.getDtCreated())
                .modifiedBy(entity.getModifiedBy())
                .dtModified(entity.getDtModified())
                .build();
    }

    @Override
    public ProductSkuDetailsVO convertDO2DetailsVO(ProductSkuDO entity) {
        if (entity == null) {
            return null;
        }
        return ProductSkuDetailsVO.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .skuCode(entity.getSkuCode())
                .skuName(entity.getSkuName())
                .revision(entity.getRevision())
                .skuType(entity.getSkuType())
                .skuTypeDesc(SkuConstants.SkuTypeEnum.getDescByCode(entity.getSkuType()))
                .baseUnitPrice(entity.getBaseUnitPrice())
                .currency(entity.getCurrency())
                .saleable(entity.getSaleable())
                .visible(entity.getVisible())
                .quotaLimit(entity.getQuotaLimit())
                .isCurrent(entity.getIsCurrent())
                .effectiveTime(entity.getEffectiveTime())
                .expiryTime(entity.getExpiryTime())
                .status(entity.getStatus())
                .statusDesc(ProductStatusEnum.getDescByCode(entity.getStatus()))
                .publishTime(entity.getPublishTime())
                .createdBy(entity.getCreatedBy())
                .dtCreated(entity.getDtCreated())
                .modifiedBy(entity.getModifiedBy())
                .dtModified(entity.getDtModified())
                .build();
    }

    @Override
    public ProductSkuDO convertDTO2DOForCreate(ProductSkuDTO dto, String tenantId) {
        if (dto == null) {
            return null;
        }

        // 基础转换
        ProductSkuDO entity = convertDTO2DO(dto);

        // 设置默认值
        entity.setTenantId(tenantId);

        return entity;
    }

    @Override
    public List<ProductSkuVO> convertDO2VO(List<ProductSkuDO> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::convertDO2VO)
                .collect(Collectors.toList());
    }

    // ==================== 复杂转换实现（经过BO） ====================

    @Override
    public ProductSkuDetailsVO convertBO2DetailsVO(ProductSkuBO bo) {
        ProductSkuDetailsVO vo = this.convertDO2DetailsVO(bo.getProductSkuDO());

        // 转换关联的计费项列表
        if (!CollectionUtils.isEmpty(bo.getBillingItems())) {
            vo.setBillingItems(productInfoConverter.convertDO2VO(bo.getBillingItems()));
        }

        // 转换关联的定价模板列表
        if (!CollectionUtils.isEmpty(bo.getPricingTemplates())) {
            vo.setPricingTemplates(skuPricingConverter.convertDO2VO(bo.getPricingTemplates()));
        }

        // 转换关联的定价策略列表
        if (!CollectionUtils.isEmpty(bo.getPricingStrategies())) {
            vo.setPricingStrategies(pricingStrategyConverter.convertDO2VOList(bo.getPricingStrategies()));
        }

        return vo;
    }
}
