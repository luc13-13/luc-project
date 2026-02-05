package com.lc.product.center.converter.impl;

import com.lc.product.center.constants.ProductStatusEnum;
import com.lc.product.center.constants.SkuTypeConstants;
import com.lc.product.center.converter.ProductSkuConverter;
import com.lc.product.center.domain.bo.ProductSkuBO;
import com.lc.product.center.domain.dto.ProductSkuDTO;
import com.lc.product.center.domain.entity.ProductSkuDO;
import com.lc.product.center.domain.vo.ProductSkuVO;
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
@Service("productSkuConverter")
public class ProductSkuConverterImpl implements ProductSkuConverter {

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

        ProductSkuVO vo = ProductSkuVO.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .skuCode(entity.getSkuCode())
                .skuName(entity.getSkuName())
                .revision(entity.getRevision())
                .skuType(entity.getSkuType())
                .baseUnitPrice(entity.getBaseUnitPrice())
                .currency(entity.getCurrency())
                .saleable(entity.getSaleable())
                .visible(entity.getVisible())
                .quotaLimit(entity.getQuotaLimit())
                .isCurrent(entity.getIsCurrent())
                .effectiveTime(entity.getEffectiveTime())
                .expiryTime(entity.getExpiryTime())
                .status(entity.getStatus())
                .publishTime(entity.getPublishTime())
                .createdBy(entity.getCreatedBy())
                .dtCreated(entity.getDtCreated())
                .modifiedBy(entity.getModifiedBy())
                .dtModified(entity.getDtModified())
                .build();

        vo.setSkuTypeDesc(getSkuTypeDesc(entity.getSkuType()));
        vo.setStatusDesc(ProductStatusEnum.getDescByCode(entity.getStatus()));
        return vo;
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
    public ProductSkuBO convertDO2BO(ProductSkuDO entity) {
        if (entity == null) {
            return null;
        }
        return ProductSkuBO.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .skuCode(entity.getSkuCode())
                .skuName(entity.getSkuName())
                .revision(entity.getRevision())
                .skuType(entity.getSkuType())
                .baseUnitPrice(entity.getBaseUnitPrice())
                .currency(entity.getCurrency())
                .saleable(entity.getSaleable())
                .visible(entity.getVisible())
                .quotaLimit(entity.getQuotaLimit())
                .isCurrent(entity.getIsCurrent())
                .effectiveTime(entity.getEffectiveTime())
                .expiryTime(entity.getExpiryTime())
                .status(entity.getStatus())
                .publishTime(entity.getPublishTime())
                .createdBy(entity.getCreatedBy())
                .dtCreated(entity.getDtCreated())
                .modifiedBy(entity.getModifiedBy())
                .dtModified(entity.getDtModified())
                .deleted(entity.getDeleted())
                .build();
    }

    @Override
    public ProductSkuVO convertBO2VO(ProductSkuBO bo) {
        if (bo == null) {
            return null;
        }

        ProductSkuVO vo = ProductSkuVO.builder()
                .id(bo.getId())
                .tenantId(bo.getTenantId())
                .skuCode(bo.getSkuCode())
                .skuName(bo.getSkuName())
                .revision(bo.getRevision())
                .skuType(bo.getSkuType())
                .baseUnitPrice(bo.getBaseUnitPrice())
                .currency(bo.getCurrency())
                .productCode(bo.getProductCode())
                .productName(bo.getProductName())
                .subProductCode(bo.getSubProductCode())
                .subProductName(bo.getSubProductName())
                .saleable(bo.getSaleable())
                .visible(bo.getVisible())
                .quotaLimit(bo.getQuotaLimit())
                .isCurrent(bo.getIsCurrent())
                .effectiveTime(bo.getEffectiveTime())
                .expiryTime(bo.getExpiryTime())
                .status(bo.getStatus())
                .publishTime(bo.getPublishTime())
                .hourlyPrice(bo.getHourlyPrice())
                .monthlyPrice(bo.getMonthlyPrice())
                .yearlyPrice(bo.getYearlyPrice())
                .createdBy(bo.getCreatedBy())
                .dtCreated(bo.getDtCreated())
                .modifiedBy(bo.getModifiedBy())
                .dtModified(bo.getDtModified())
                .build();

        // 设置类型描述
        vo.setSkuTypeDesc(getSkuTypeDesc(bo.getSkuType()));

        // 设置状态描述
        vo.setStatusDesc(ProductStatusEnum.getDescByCode(bo.getStatus()));

        return vo;
    }

    @Override
    public List<ProductSkuBO> convertDO2BO(List<ProductSkuDO> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::convertDO2BO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductSkuVO> convertBO2VO(List<ProductSkuBO> bos) {
        if (CollectionUtils.isEmpty(bos)) {
            return Collections.emptyList();
        }
        return bos.stream()
                .map(this::convertBO2VO)
                .collect(Collectors.toList());
    }

    /**
     * 获取SKU类型描述
     */
    private String getSkuTypeDesc(String skuType) {
        if (skuType == null) {
            return null;
        }
        return switch (skuType) {
            case SkuTypeConstants.INSTANCE -> "实例";
            case SkuTypeConstants.ADDON -> "附加项";
            case SkuTypeConstants.BUNDLE -> "套餐";
            case SkuTypeConstants.SUBSCRIPTION -> "订阅";
            default -> skuType;
        };
    }
}
