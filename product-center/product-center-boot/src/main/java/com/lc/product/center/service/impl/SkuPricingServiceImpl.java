package com.lc.product.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.framework.core.mvc.BizException;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.constants.ProductDefaultConstants;
import com.lc.product.center.constants.ProductStatusEnum;
import com.lc.product.center.converter.SkuPricingConverter;
import com.lc.product.center.domain.dto.SkuPricingDTO;
import com.lc.product.center.domain.entity.SkuPricingDO;
import com.lc.product.center.domain.vo.SkuPricingVO;
import com.lc.product.center.mapper.SkuPricingMapper;
import com.lc.product.center.service.SkuPricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 定价模板表(product_center.sku_pricing)表服务实现类
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Service("skuPricingService")
public class SkuPricingServiceImpl extends ServiceImpl<SkuPricingMapper, SkuPricingDO>
        implements SkuPricingService {

    @Autowired
    private SkuPricingConverter skuPricingConverter;

    @Override
    public PaginationResult<SkuPricingVO> queryPricingPage(SkuPricingDTO queryDTO) {
        Page<SkuPricingDO> page = Page.of(queryDTO.getPageIndex(), queryDTO.getPageSize());

        LambdaQueryWrapper<SkuPricingDO> queryWrapper = buildQueryWrapper(queryDTO);
        queryWrapper.orderByDesc(SkuPricingDO::getPriority)
                .orderByDesc(SkuPricingDO::getDtCreated);

        IPage<SkuPricingDO> pageResult = this.page(page, queryWrapper);
        queryDTO.setTotal(pageResult.getTotal());

        List<SkuPricingVO> voList = skuPricingConverter.convertDO2VO(pageResult.getRecords());
        return PaginationResult.success(voList, queryDTO);
    }

    @Override
    public List<SkuPricingVO> queryPricingList(SkuPricingDTO queryDTO) {
        List<SkuPricingDO> list = baseMapper.selectByCondition(queryDTO);
        return skuPricingConverter.convertDO2VO(list);
    }

    @Override
    public SkuPricingVO getPricingById(Long id) {
        SkuPricingDO entity = this.getById(id);
        if (entity == null) {
            return null;
        }
        return skuPricingConverter.convertDO2VO(entity);
    }

    @Override
    public List<SkuPricingVO> getPricingsByCode(String tenantId, String pricingCode) {
        SkuPricingDTO queryDTO = SkuPricingDTO.builder()
                .tenantId(StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT)
                .pricingCode(pricingCode)
                .build();
        List<SkuPricingDO> list = baseMapper.selectByCondition(queryDTO);
        return skuPricingConverter.convertDO2VO(list);
    }

    @Override
    public SkuPricingVO getPricingByCodeAndCycle(String tenantId, String pricingCode, String billingCycle) {
        SkuPricingDTO queryDTO = SkuPricingDTO.builder()
                .tenantId(StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT)
                .pricingCode(pricingCode)
                .billingCycle(billingCycle)
                .build();
        List<SkuPricingDO> list = baseMapper.selectByCondition(queryDTO);
        return CollectionUtils.isEmpty(list) ? null : skuPricingConverter.convertDO2VO(list.getFirst());
    }

    @Override
    public SkuPricingVO getEffectivePricing(String tenantId, String pricingCode, String billingCycle) {
        SkuPricingDTO queryDTO = SkuPricingDTO.builder()
                .tenantId(StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT)
                .pricingCode(pricingCode)
                .billingCycle(billingCycle)
                .status(ProductStatusEnum.ACTIVE.getCode())
                .effectiveOnly(true)
                .build();
        List<SkuPricingDO> list = baseMapper.selectByCondition(queryDTO);
        return CollectionUtils.isEmpty(list) ? null : skuPricingConverter.convertDO2VO(list.getFirst());
    }

    @Override
    public SkuPricingVO createPricing(SkuPricingDTO pricingDTO) {
        String tenantId = StringUtils.hasText(pricingDTO.getTenantId())
                ? pricingDTO.getTenantId()
                : ProductDefaultConstants.DEFAULT_TENANT;

        // 检查是否已存在相同的定价配置
        SkuPricingDTO checkDTO = SkuPricingDTO.builder()
                .tenantId(tenantId)
                .pricingCode(pricingDTO.getPricingCode())
                .meteringMode(pricingDTO.getMeteringMode())
                .paymentMode(pricingDTO.getPaymentMode())
                .billingCycle(pricingDTO.getBillingCycle())
                .revision(pricingDTO.getRevision())
                .build();
        List<SkuPricingDO> existing = baseMapper.selectByCondition(checkDTO);
        if (!CollectionUtils.isEmpty(existing)) {
            throw BizException.exp("该定价配置已存在");
        }

        SkuPricingDO entity = skuPricingConverter.convertDTO2DOForCreate(pricingDTO, tenantId);
        this.save(entity);
        return skuPricingConverter.convertDO2VO(entity);
    }

    @Override
    public List<SkuPricingVO> batchCreatePricings(List<SkuPricingDTO> pricingDTOs) {
        if (CollectionUtils.isEmpty(pricingDTOs)) {
            return new ArrayList<>();
        }
        List<SkuPricingVO> results = new ArrayList<>();
        for (SkuPricingDTO dto : pricingDTOs) {
            results.add(createPricing(dto));
        }
        return results;
    }

    @Override
    public SkuPricingVO updatePricing(SkuPricingDTO pricingDTO) {
        SkuPricingDO existing = this.getById(pricingDTO.getId());
        if (existing == null) {
            throw BizException.exp("定价不存在");
        }
        SkuPricingDO updateDO = skuPricingConverter.convertDTO2DO(pricingDTO);
        this.updateById(updateDO);
        return skuPricingConverter.convertDO2VO(this.getById(pricingDTO.getId()));
    }

    @Override
    public Boolean deletePricing(Long id) {
        SkuPricingDO entity = this.getById(id);
        if (entity == null) {
            throw BizException.exp("定价不存在");
        }
        return this.removeById(entity);
    }

    @Override
    public Boolean deletePricingsByCode(String tenantId, String pricingCode) {
        String tenant = StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT;
        LambdaQueryWrapper<SkuPricingDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuPricingDO::getTenantId, tenant)
                .eq(SkuPricingDO::getPricingCode, pricingCode);
        return this.remove(wrapper);
    }

    private LambdaQueryWrapper<SkuPricingDO> buildQueryWrapper(SkuPricingDTO queryDTO) {
        LambdaQueryWrapper<SkuPricingDO> queryWrapper = new LambdaQueryWrapper<>();
        if (queryDTO != null) {
            if (StringUtils.hasText(queryDTO.getTenantId())) {
                queryWrapper.eq(SkuPricingDO::getTenantId, queryDTO.getTenantId());
            }
            if (StringUtils.hasText(queryDTO.getPricingCode())) {
                queryWrapper.eq(SkuPricingDO::getPricingCode, queryDTO.getPricingCode());
            }
            if (StringUtils.hasText(queryDTO.getMeteringMode())) {
                queryWrapper.eq(SkuPricingDO::getMeteringMode, queryDTO.getMeteringMode());
            }
            if (StringUtils.hasText(queryDTO.getPaymentMode())) {
                queryWrapper.eq(SkuPricingDO::getPaymentMode, queryDTO.getPaymentMode());
            }
            if (StringUtils.hasText(queryDTO.getBillingCycle())) {
                queryWrapper.eq(SkuPricingDO::getBillingCycle, queryDTO.getBillingCycle());
            }
            if (StringUtils.hasText(queryDTO.getStatus())) {
                queryWrapper.eq(SkuPricingDO::getStatus, queryDTO.getStatus());
            }
            if (queryDTO.getIsCurrent() != null) {
                queryWrapper.eq(SkuPricingDO::getIsCurrent, queryDTO.getIsCurrent());
            }
        }
        return queryWrapper;
    }
}
