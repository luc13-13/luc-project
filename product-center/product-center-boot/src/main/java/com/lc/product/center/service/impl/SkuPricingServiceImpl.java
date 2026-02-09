package com.lc.product.center.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.framework.core.mvc.BizException;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.constants.ProductDefaultConstants;
import com.lc.product.center.converter.SkuPricingConverter;
import com.lc.product.center.domain.dto.SkuPricingDTO;
import com.lc.product.center.domain.entity.SkuPricingDO;
import com.lc.product.center.domain.vo.SkuPricingVO;
import com.lc.product.center.mapper.SkuPricingMapper;
import com.lc.product.center.service.SkuPricingService;
import com.lc.framework.core.utils.RevisionUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 定价模板表(product_center.sku_pricing)表服务实现类
 *
 * @author lucheng
 * @since 2026-02-06
 */
@Service("skuPricingService")
@AllArgsConstructor
public class SkuPricingServiceImpl extends ServiceImpl<SkuPricingMapper, SkuPricingDO>
        implements SkuPricingService {

    private final SkuPricingConverter skuPricingConverter;

    @Override
    public List<SkuPricingVO> listByCondition(SkuPricingDTO queryDTO) {
        setDefaultTenantIfEmpty(queryDTO);
        List<SkuPricingDO> list = baseMapper.selectByCondition(queryDTO);
        return skuPricingConverter.convertDO2VO(list);
    }

    @Override
    public PaginationResult<SkuPricingVO> pageByCondition(SkuPricingDTO queryDTO) {
        setDefaultTenantIfEmpty(queryDTO);

        // 使用 MyBatis-Plus 原生分页
        IPage<SkuPricingDO> page = new Page<>(queryDTO.getPageIndex(), queryDTO.getPageSize());
        IPage<SkuPricingDO> result = baseMapper.selectPageByCondition(page, queryDTO);

        List<SkuPricingVO> voList = skuPricingConverter.convertDO2VO(result.getRecords());

        queryDTO.setTotal(result.getTotal());
        return PaginationResult.success(voList, queryDTO);
    }

    @Override
    public SkuPricingVO getDetailById(Long id) {
        SkuPricingDO entity = this.getById(id);
        if (entity == null) {
            return null;
        }
        return skuPricingConverter.convertDO2VO(entity);
    }

    @Override
    public SkuPricingVO createPricing(SkuPricingDTO dto) {
        String tenantId = StringUtils.hasText(dto.getTenantId())
                ? dto.getTenantId()
                : ProductDefaultConstants.DEFAULT_TENANT;

        // 生成版本号（带 V 前缀）
        String revision = RevisionUtils.generateTimestampRevision("PRC-");

        // 检查定价编码 + 版本号是否已存在
        SkuPricingDTO checkDTO = SkuPricingDTO.builder()
                .tenantId(tenantId)
                .pricingCode(dto.getPricingCode())
                .revision(revision)
                .build();
        List<SkuPricingDO> existing = baseMapper.selectByCondition(checkDTO);
        if (!CollectionUtils.isEmpty(existing)) {
            throw BizException.exp("定价编码+版本号已存在: " + dto.getPricingCode() + "-" + revision);
        }

        // 转换并设置默认值
        SkuPricingDO entity = skuPricingConverter.convertDTO2DOForCreate(dto, tenantId);
        entity.setRevision(revision);
        entity.setIsCurrent(true);

        // 如果存在旧版本，将其标记为非当前版本
        SkuPricingDTO oldDTO = SkuPricingDTO.builder()
                .tenantId(tenantId)
                .pricingCode(dto.getPricingCode())
                .isCurrent(true)
                .build();
        List<SkuPricingDO> oldVersions = baseMapper.selectByCondition(oldDTO);
        for (SkuPricingDO oldVersion : oldVersions) {
            oldVersion.setIsCurrent(false);
            this.updateById(oldVersion);
        }

        this.save(entity);
        return skuPricingConverter.convertDO2VO(entity);
    }

    @Override
    public SkuPricingVO updatePricing(SkuPricingDTO dto) {
        SkuPricingDO existing = this.getById(dto.getId());
        if (existing == null) {
            throw BizException.exp("定价模板不存在");
        }

        SkuPricingDO updateDO = skuPricingConverter.convertDTO2DO(dto);
        updateDO.setId(existing.getId());

        this.updateById(updateDO);
        return skuPricingConverter.convertDO2VO(this.getById(dto.getId()));
    }

    @Override
    public Boolean deletePricing(Long id) {
        SkuPricingDO existing = this.getById(id);
        if (existing == null) {
            throw BizException.exp("定价模板不存在");
        }
        return this.removeById(id);
    }

    private void setDefaultTenantIfEmpty(SkuPricingDTO dto) {
        if (!StringUtils.hasText(dto.getTenantId())) {
            dto.setTenantId(ProductDefaultConstants.DEFAULT_TENANT);
        }
    }
}
