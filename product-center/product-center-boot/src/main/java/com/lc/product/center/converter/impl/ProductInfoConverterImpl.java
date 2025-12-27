package com.lc.product.center.converter.impl;

import com.lc.product.center.constants.ProductStatusEnum;
import com.lc.product.center.converter.ProductInfoConverter;
import com.lc.product.center.domain.bo.ProductInfoBO;
import com.lc.product.center.domain.dto.ProductInfoDTO;
import com.lc.product.center.domain.entity.ProductInfoDO;
import com.lc.product.center.domain.vo.ProductInfoVO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品信息表对象转换实现类
 *
 * @author lucheng
 * @since 2025-12-26
 */
@Service("productInfoConverter")
public class ProductInfoConverterImpl implements ProductInfoConverter {

    // ==================== 简单转换实现 ====================

    @Override
    public ProductInfoDO convertDTO2DO(ProductInfoDTO dto) {
        if (dto == null) {
            return null;
        }
        ProductInfoDO entity = new ProductInfoDO();
        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setProductCode(dto.getProductCode());
        entity.setSubProductCode(dto.getSubProductCode());
        entity.setBillingItemCode(dto.getBillingItemCode());
        entity.setSubBillingItemCode(dto.getSubBillingItemCode());
        entity.setProductName(dto.getProductName());
        entity.setSubProductName(dto.getSubProductName());
        entity.setBillingItemName(dto.getBillingItemName());
        entity.setSubBillingItemName(dto.getSubBillingItemName());
        entity.setSpecValue(dto.getSpecValue());
        entity.setSpecUnit(dto.getSpecUnit());
        entity.setBasePrice(dto.getBasePrice());
        entity.setPriceFactor(dto.getPriceFactor());
        entity.setMeteringUnit(dto.getMeteringUnit());
        entity.setStatus(dto.getStatus());
        entity.setSortOrder(dto.getSortOrder());
        return entity;
    }

    @Override
    public ProductInfoDO convertDTO2DOForCreate(ProductInfoDTO dto, String tenantId) {
        if (dto == null) {
            return null;
        }

        // 基础转换
        ProductInfoDO entity = convertDTO2DO(dto);

        // 设置默认值
        entity.setTenantId(tenantId);

        if (!org.springframework.util.StringUtils.hasText(entity.getStatus())) {
            entity.setStatus(ProductStatusEnum.ACTIVE.getCode());
        }
        if (entity.getSortOrder() == null) {
            entity.setSortOrder(com.lc.product.center.constants.ProductDefaultConstants.DEFAULT_SORT_ORDER);
        }

        return entity;
    }

    @Override
    public ProductInfoVO convertDO2VO(ProductInfoDO entity) {
        if (entity == null) {
            return null;
        }

        // 计算单价
        java.math.BigDecimal unitPrice = null;
        if (entity.getBasePrice() != null && entity.getPriceFactor() != null) {
            unitPrice = entity.getBasePrice().multiply(entity.getPriceFactor());
        } else if (entity.getBasePrice() != null) {
            unitPrice = entity.getBasePrice();
        }

        ProductInfoVO vo = ProductInfoVO.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .productCode(entity.getProductCode())
                .subProductCode(entity.getSubProductCode())
                .billingItemCode(entity.getBillingItemCode())
                .subBillingItemCode(entity.getSubBillingItemCode())
                .productName(entity.getProductName())
                .subProductName(entity.getSubProductName())
                .billingItemName(entity.getBillingItemName())
                .subBillingItemName(entity.getSubBillingItemName())
                .specValue(entity.getSpecValue())
                .specUnit(entity.getSpecUnit())
                .basePrice(entity.getBasePrice())
                .priceFactor(entity.getPriceFactor())
                .unitPrice(unitPrice)
                .meteringUnit(entity.getMeteringUnit())
                .status(entity.getStatus())
                .sortOrder(entity.getSortOrder())
                .createdBy(entity.getCreatedBy())
                .dtCreated(entity.getDtCreated())
                .modifiedBy(entity.getModifiedBy())
                .dtModified(entity.getDtModified())
                .build();

        vo.setStatusDesc(ProductStatusEnum.getDescByCode(entity.getStatus()));
        return vo;
    }

    @Override
    public List<ProductInfoVO> convertDO2VO(List<ProductInfoDO> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::convertDO2VO)
                .collect(Collectors.toList());
    }

    // ==================== 复杂转换实现（经过BO） ====================

    @Override
    public ProductInfoBO convertDO2BO(ProductInfoDO entity) {
        if (entity == null) {
            return null;
        }
        ProductInfoBO bo = ProductInfoBO.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .productCode(entity.getProductCode())
                .subProductCode(entity.getSubProductCode())
                .billingItemCode(entity.getBillingItemCode())
                .subBillingItemCode(entity.getSubBillingItemCode())
                .productName(entity.getProductName())
                .subProductName(entity.getSubProductName())
                .billingItemName(entity.getBillingItemName())
                .subBillingItemName(entity.getSubBillingItemName())
                .specValue(entity.getSpecValue())
                .specUnit(entity.getSpecUnit())
                .basePrice(entity.getBasePrice())
                .priceFactor(entity.getPriceFactor())
                .meteringUnit(entity.getMeteringUnit())
                .status(entity.getStatus())
                .sortOrder(entity.getSortOrder())
                .createdBy(entity.getCreatedBy())
                .dtCreated(entity.getDtCreated())
                .modifiedBy(entity.getModifiedBy())
                .dtModified(entity.getDtModified())
                .deleted(entity.getDeleted())
                .build();

        // 计算单价
        bo.calculateUnitPrice();

        return bo;
    }

    @Override
    public ProductInfoVO convertBO2VO(ProductInfoBO bo) {
        if (bo == null) {
            return null;
        }

        // 确保单价已计算
        if (bo.getUnitPrice() == null) {
            bo.calculateUnitPrice();
        }

        ProductInfoVO vo = ProductInfoVO.builder()
                .id(bo.getId())
                .tenantId(bo.getTenantId())
                .productCode(bo.getProductCode())
                .subProductCode(bo.getSubProductCode())
                .billingItemCode(bo.getBillingItemCode())
                .subBillingItemCode(bo.getSubBillingItemCode())
                .productName(bo.getProductName())
                .subProductName(bo.getSubProductName())
                .billingItemName(bo.getBillingItemName())
                .subBillingItemName(bo.getSubBillingItemName())
                .specValue(bo.getSpecValue())
                .specUnit(bo.getSpecUnit())
                .basePrice(bo.getBasePrice())
                .priceFactor(bo.getPriceFactor())
                .unitPrice(bo.getUnitPrice())
                .meteringUnit(bo.getMeteringUnit())
                .status(bo.getStatus())
                .sortOrder(bo.getSortOrder())
                .createdBy(bo.getCreatedBy())
                .dtCreated(bo.getDtCreated())
                .modifiedBy(bo.getModifiedBy())
                .dtModified(bo.getDtModified())
                .build();

        // 设置状态描述
        vo.setStatusDesc(ProductStatusEnum.getDescByCode(bo.getStatus()));

        return vo;
    }

    @Override
    public List<ProductInfoBO> convertDO2BO(List<ProductInfoDO> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::convertDO2BO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductInfoVO> convertBO2VO(List<ProductInfoBO> bos) {
        if (CollectionUtils.isEmpty(bos)) {
            return Collections.emptyList();
        }
        return bos.stream()
                .map(this::convertBO2VO)
                .collect(Collectors.toList());
    }
}
