package com.lc.product.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.framework.core.mvc.BizException;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.constants.ProductDefaultConstants;
import com.lc.product.center.converter.SkuItemCombinationConverter;
import com.lc.product.center.domain.dto.SkuItemCombinationDTO;
import com.lc.product.center.domain.entity.SkuItemCombinationDO;
import com.lc.product.center.domain.vo.SkuItemCombinationVO;
import com.lc.product.center.mapper.SkuItemCombinationMapper;
import com.lc.product.center.service.SkuItemCombinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * SKU计费项组合表(product_center.sku_item_combination)表服务实现类
 *
 * @author lucheng
 * @since 2025-12-27
 */
@Service("skuItemCombinationService")
public class SkuItemCombinationServiceImpl extends ServiceImpl<SkuItemCombinationMapper, SkuItemCombinationDO>
        implements SkuItemCombinationService {

    @Autowired
    private SkuItemCombinationConverter skuItemCombinationConverter;

    @Override
    public PaginationResult<SkuItemCombinationVO> queryCombinationPage(SkuItemCombinationDTO queryDTO) {
        Page<SkuItemCombinationDO> page = Page.of(queryDTO.getPageIndex(), queryDTO.getPageSize());

        LambdaQueryWrapper<SkuItemCombinationDO> queryWrapper = buildQueryWrapper(queryDTO);
        queryWrapper.orderByDesc(SkuItemCombinationDO::getDtCreated);

        IPage<SkuItemCombinationDO> pageResult = this.page(page, queryWrapper);
        queryDTO.setTotal(pageResult.getTotal());

        List<SkuItemCombinationVO> voList = skuItemCombinationConverter.convertDO2VO(pageResult.getRecords());
        return PaginationResult.success(voList, queryDTO);
    }

    @Override
    public List<SkuItemCombinationVO> queryCombinationList(SkuItemCombinationDTO queryDTO) {
        List<SkuItemCombinationDO> list = baseMapper.selectByCondition(queryDTO);
        return skuItemCombinationConverter.convertDO2VO(list);
    }

    @Override
    public SkuItemCombinationVO getCombinationById(Long id) {
        SkuItemCombinationDO entity = this.getById(id);
        if (entity == null) {
            return null;
        }
        return skuItemCombinationConverter.convertDO2VO(entity);
    }

    @Override
    public List<SkuItemCombinationVO> getCombinationsBySkuCode(String tenantId, String skuCode) {
        SkuItemCombinationDTO queryDTO = SkuItemCombinationDTO.builder()
                .tenantId(StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT)
                .skuCode(skuCode)
                .build();
        List<SkuItemCombinationDO> list = baseMapper.selectByCondition(queryDTO);
        return skuItemCombinationConverter.convertDO2VO(list);
    }

    @Override
    public SkuItemCombinationVO createCombination(SkuItemCombinationDTO combinationDTO) {
        String tenantId = StringUtils.hasText(combinationDTO.getTenantId())
                ? combinationDTO.getTenantId()
                : ProductDefaultConstants.DEFAULT_TENANT;

        SkuItemCombinationDO entity = skuItemCombinationConverter.convertDTO2DOForCreate(combinationDTO, tenantId);
        this.save(entity);
        return skuItemCombinationConverter.convertDO2VO(entity);
    }

    @Override
    public List<SkuItemCombinationVO> batchCreateCombinations(List<SkuItemCombinationDTO> combinationDTOs) {
        if (CollectionUtils.isEmpty(combinationDTOs)) {
            return new ArrayList<>();
        }
        List<SkuItemCombinationVO> results = new ArrayList<>();
        for (SkuItemCombinationDTO dto : combinationDTOs) {
            results.add(createCombination(dto));
        }
        return results;
    }

    @Override
    public SkuItemCombinationVO updateCombination(SkuItemCombinationDTO combinationDTO) {
        SkuItemCombinationDO existing = this.getById(combinationDTO.getId());
        if (existing == null) {
            throw BizException.exp("组合不存在");
        }
        SkuItemCombinationDO updateDO = skuItemCombinationConverter.convertDTO2DO(combinationDTO);
        this.updateById(updateDO);
        return skuItemCombinationConverter.convertDO2VO(this.getById(combinationDTO.getId()));
    }

    @Override
    public Boolean deleteCombination(Long id) {
        SkuItemCombinationDO entity = this.getById(id);
        if (entity == null) {
            throw BizException.exp("组合不存在");
        }
        return this.removeById(entity);
    }

    @Override
    public Boolean deleteCombinationsBySkuCode(String tenantId, String skuCode) {
        String tenant = StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT;
        LambdaQueryWrapper<SkuItemCombinationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuItemCombinationDO::getTenantId, tenant)
                .eq(SkuItemCombinationDO::getSkuCode, skuCode);
        return this.remove(wrapper);
    }

    private LambdaQueryWrapper<SkuItemCombinationDO> buildQueryWrapper(SkuItemCombinationDTO queryDTO) {
        LambdaQueryWrapper<SkuItemCombinationDO> queryWrapper = new LambdaQueryWrapper<>();
        if (queryDTO != null) {
            if (StringUtils.hasText(queryDTO.getTenantId())) {
                queryWrapper.eq(SkuItemCombinationDO::getTenantId, queryDTO.getTenantId());
            }
            if (StringUtils.hasText(queryDTO.getSkuCode())) {
                queryWrapper.eq(SkuItemCombinationDO::getSkuCode, queryDTO.getSkuCode());
            }
            if (StringUtils.hasText(queryDTO.getProductCode())) {
                queryWrapper.eq(SkuItemCombinationDO::getProductCode, queryDTO.getProductCode());
            }
            if (StringUtils.hasText(queryDTO.getBillingItemCode())) {
                queryWrapper.eq(SkuItemCombinationDO::getBillingItemCode, queryDTO.getBillingItemCode());
            }
        }
        return queryWrapper;
    }
}
