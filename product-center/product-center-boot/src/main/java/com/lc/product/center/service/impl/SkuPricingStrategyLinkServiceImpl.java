package com.lc.product.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.product.center.constants.ProductStatusEnum;
import com.lc.product.center.domain.dto.SkuPricingStrategyLinkDTO;
import com.lc.product.center.domain.entity.SkuPricingStrategyLinkDO;
import com.lc.product.center.domain.vo.SkuPricingStrategyLinkVO;
import com.lc.product.center.mapper.SkuPricingStrategyLinkMapper;
import com.lc.product.center.service.SkuPricingStrategyLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SKU与策略关联表(product_center.sku_pricing_strategy_link)服务实现
 *
 * @author lucheng
 * @since 2026-02-05
 */
@Slf4j
@Service("skuPricingStrategyLinkService")
public class SkuPricingStrategyLinkServiceImpl
        extends ServiceImpl<SkuPricingStrategyLinkMapper, SkuPricingStrategyLinkDO>
        implements SkuPricingStrategyLinkService {

    @Override
    public boolean createLink(SkuPricingStrategyLinkDTO dto) {
        SkuPricingStrategyLinkDO entity = convertDTO2DO(dto);
        entity.setStatus(ProductStatusEnum.ACTIVE.getCode());
        return this.save(entity);
    }

    @Override
    public boolean updateLink(SkuPricingStrategyLinkDTO dto) {
        SkuPricingStrategyLinkDO entity = convertDTO2DO(dto);
        return this.updateById(entity);
    }

    @Override
    public boolean deleteLink(Long id) {
        return this.removeById(id);
    }

    @Override
    public List<SkuPricingStrategyLinkVO> listBySkuCodeAndRevision(String skuCode, String skuRevision) {
        LambdaQueryWrapper<SkuPricingStrategyLinkDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SkuPricingStrategyLinkDO::getSkuCode, skuCode)
                .eq(SkuPricingStrategyLinkDO::getSkuRevision, skuRevision)
                .eq(SkuPricingStrategyLinkDO::getStatus, ProductStatusEnum.ACTIVE.getCode())
                .orderByDesc(SkuPricingStrategyLinkDO::getPriority);

        List<SkuPricingStrategyLinkDO> links = this.list(queryWrapper);
        return convertDO2VOList(links);
    }

    @Override
    public List<SkuPricingStrategyLinkVO> listByStrategyCode(String strategyCode) {
        LambdaQueryWrapper<SkuPricingStrategyLinkDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SkuPricingStrategyLinkDO::getStrategyCode, strategyCode)
                .eq(SkuPricingStrategyLinkDO::getStatus, ProductStatusEnum.ACTIVE.getCode())
                .orderByDesc(SkuPricingStrategyLinkDO::getDtCreated);

        List<SkuPricingStrategyLinkDO> links = this.list(queryWrapper);
        return convertDO2VOList(links);
    }

    @Override
    public boolean batchCreateLinks(List<SkuPricingStrategyLinkDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return true;
        }
        List<SkuPricingStrategyLinkDO> entities = dtoList.stream()
                .map(dto -> {
                    SkuPricingStrategyLinkDO entity = convertDTO2DO(dto);
                    entity.setStatus(ProductStatusEnum.ACTIVE.getCode());
                    return entity;
                })
                .collect(Collectors.toList());

        return this.saveBatch(entities);
    }

    // ==================== 转换方法 ====================

    private SkuPricingStrategyLinkDO convertDTO2DO(SkuPricingStrategyLinkDTO dto) {
        SkuPricingStrategyLinkDO entity = new SkuPricingStrategyLinkDO();
        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setSkuCode(dto.getSkuCode());
        entity.setSkuRevision(dto.getSkuRevision());
        entity.setStrategyCode(dto.getStrategyCode());
        entity.setPriority(dto.getPriority());
        entity.setEffectiveTime(dto.getEffectiveTime());
        entity.setExpiryTime(dto.getExpiryTime());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    private List<SkuPricingStrategyLinkVO> convertDO2VOList(List<SkuPricingStrategyLinkDO> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return List.of();
        }
        return entities.stream()
                .map(this::convertDO2VO)
                .collect(Collectors.toList());
    }

    private SkuPricingStrategyLinkVO convertDO2VO(SkuPricingStrategyLinkDO entity) {
        return SkuPricingStrategyLinkVO.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .skuCode(entity.getSkuCode())
                .skuRevision(entity.getSkuRevision())
                .strategyCode(entity.getStrategyCode())
                .priority(entity.getPriority())
                .effectiveTime(entity.getEffectiveTime())
                .expiryTime(entity.getExpiryTime())
                .status(entity.getStatus())
                .statusDesc(ProductStatusEnum.getDescByCode(entity.getStatus()))
                .dtCreated(entity.getDtCreated())
                .dtModified(entity.getDtModified())
                .build();
    }
}
