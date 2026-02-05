package com.lc.product.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.framework.core.constants.NumberConstants;
import com.lc.framework.core.mvc.BizException;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.constants.ProductDefaultConstants;
import com.lc.product.center.constants.ProductStatusEnum;
import com.lc.product.center.converter.ProductSkuConverter;
import com.lc.product.center.domain.dto.ProductSkuDTO;
import com.lc.product.center.domain.dto.SkuItemCombinationDTO;
import com.lc.product.center.domain.dto.SkuPricingLinkDTO;
import com.lc.product.center.domain.dto.SkuPricingStrategyLinkDTO;
import com.lc.product.center.domain.entity.ProductSkuDO;
import com.lc.product.center.domain.entity.SkuPricingLinkDO;
import com.lc.product.center.domain.vo.*;
import com.lc.product.center.mapper.ProductSkuMapper;
import com.lc.product.center.mapper.SkuPricingLinkMapper;
import com.lc.product.center.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 产品SKU表(product_center.product_sku)表服务实现类
 *
 * @author lucheng
 * @since 2025-12-21
 */
@Service("productSkuService")
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSkuDO> implements ProductSkuService {

    @Autowired
    private ProductSkuConverter productSkuConverter;

    @Autowired
    private SkuItemCombinationService skuItemCombinationService;

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private SkuPricingLinkMapper skuPricingLinkMapper;

    @Autowired
    private SkuPricingService skuPricingService;

    @Autowired
    private SkuPricingStrategyLinkService skuPricingStrategyLinkService;

    @Autowired
    private PricingStrategyService pricingStrategyService;

    @Override
    public PaginationResult<ProductSkuVO> querySkuPage(ProductSkuDTO queryDTO) {
        Page<ProductSkuDO> page = Page.of(queryDTO.getPageIndex(), queryDTO.getPageSize());

        LambdaQueryWrapper<ProductSkuDO> queryWrapper = buildQueryWrapper(queryDTO);
        queryWrapper.orderByDesc(ProductSkuDO::getDtCreated);

        IPage<ProductSkuDO> pageResult = this.page(page, queryWrapper);
        queryDTO.setTotal(pageResult.getTotal());

        // 简单查询：DO → VO（不经过BO）
        List<ProductSkuVO> voList = productSkuConverter.convertDO2VO(pageResult.getRecords());

        return PaginationResult.success(voList, queryDTO);
    }

    @Override
    public List<ProductSkuVO> querySkuList(ProductSkuDTO queryDTO) {
        LambdaQueryWrapper<ProductSkuDO> queryWrapper = buildQueryWrapper(queryDTO);
        queryWrapper.orderByDesc(ProductSkuDO::getDtCreated);

        List<ProductSkuDO> list = this.list(queryWrapper);

        // 简单查询：DO → VO（不经过BO）
        return productSkuConverter.convertDO2VO(list);
    }

    @Override
    public ProductSkuVO getSkuById(Long id) {
        ProductSkuDO skuDO = this.getById(id);
        if (skuDO == null) {
            return null;
        }

        // 转换基本信息
        ProductSkuVO vo = productSkuConverter.convertDO2VO(skuDO);

        // 加载关联的计费项列表 (billingItems)
        loadBillingItems(vo, skuDO);

        // 加载关联的定价模板 (pricingTemplates)
        loadPricingTemplates(vo, skuDO);

        // 加载关联的定价策略 (pricingStrategies)
        loadPricingStrategies(vo, skuDO);

        return vo;
    }

    /**
     * 加载关联的计费项列表
     */
    private void loadBillingItems(ProductSkuVO vo, ProductSkuDO skuDO) {
        List<SkuItemCombinationVO> combinations = skuItemCombinationService.getCombinationsBySkuCode(
                skuDO.getTenantId(), skuDO.getSkuCode());
        if (!CollectionUtils.isEmpty(combinations)) {
            List<ProductInfoVO> billingItems = new ArrayList<>();
            for (SkuItemCombinationVO combo : combinations) {
                ProductInfoVO productInfo = productInfoService.getProductByFourLevelCode(
                        combo.getTenantId(),
                        combo.getProductCode(),
                        combo.getSubProductCode(),
                        combo.getBillingItemCode(),
                        combo.getSubBillingItemCode());
                if (productInfo != null) {
                    billingItems.add(productInfo);
                }
            }
            vo.setBillingItems(billingItems);
        }
    }

    /**
     * 加载关联的定价模板列表
     */
    private void loadPricingTemplates(ProductSkuVO vo, ProductSkuDO skuDO) {
        // 通过 sku_pricing_link 表查询关联的定价编码
        LambdaQueryWrapper<SkuPricingLinkDO> linkWrapper = new LambdaQueryWrapper<>();
        linkWrapper.eq(SkuPricingLinkDO::getTenantId, skuDO.getTenantId())
                .eq(SkuPricingLinkDO::getSkuCode, skuDO.getSkuCode())
                .eq(SkuPricingLinkDO::getStatus, ProductStatusEnum.ACTIVE.getCode());

        List<SkuPricingLinkDO> pricingLinks = skuPricingLinkMapper.selectList(linkWrapper);
        if (!CollectionUtils.isEmpty(pricingLinks)) {
            List<SkuPricingVO> pricingTemplates = new ArrayList<>();
            for (SkuPricingLinkDO link : pricingLinks) {
                List<SkuPricingVO> pricings = skuPricingService.getPricingsByCode(
                        link.getTenantId(), link.getPricingCode());
                pricingTemplates.addAll(pricings);
            }
            vo.setPricingTemplates(pricingTemplates);
        }
    }

    /**
     * 加载关联的定价策略列表
     */
    private void loadPricingStrategies(ProductSkuVO vo, ProductSkuDO skuDO) {
        // 通过 sku_pricing_strategy_link 表查询关联的策略
        List<SkuPricingStrategyLinkVO> strategyLinks = skuPricingStrategyLinkService.listBySkuCodeAndRevision(
                skuDO.getSkuCode(), skuDO.getRevision());
        if (!CollectionUtils.isEmpty(strategyLinks)) {
            List<PricingStrategyVO> pricingStrategies = new ArrayList<>();
            for (SkuPricingStrategyLinkVO link : strategyLinks) {
                PricingStrategyVO strategy = pricingStrategyService.getStrategyByCode(
                        skuDO.getTenantId(), link.getStrategyCode());
                if (strategy != null) {
                    pricingStrategies.add(strategy);
                }
            }
            vo.setPricingStrategies(pricingStrategies);
        }
    }

    @Override
    public ProductSkuVO getSkuByCode(String tenantId, String skuCode) {
        // 封装查询参数
        ProductSkuDTO queryDTO = ProductSkuDTO.builder()
                .tenantId(StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT)
                .skuCode(skuCode)
                .build();
        List<ProductSkuDO> list = baseMapper.selectByCondition(queryDTO);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return productSkuConverter.convertDO2VO(list.getFirst());
    }

    @Override
    public List<ProductSkuVO> getSkusBySkuType(String tenantId, String skuType) {
        // 封装查询参数
        ProductSkuDTO queryDTO = ProductSkuDTO.builder()
                .tenantId(StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT)
                .skuType(skuType)
                .build();
        List<ProductSkuDO> list = baseMapper.selectByCondition(queryDTO);
        return productSkuConverter.convertDO2VO(list);
    }

    @Override
    public List<ProductSkuVO> getSaleableSkus(String tenantId) {
        // 封装查询参数：设置 saleable=1, visible=1, status=ACTIVE
        ProductSkuDTO queryDTO = ProductSkuDTO.builder()
                .tenantId(StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT)
                .saleable(NumberConstants.STATUS_TRUE)
                .visible(NumberConstants.STATUS_TRUE)
                .status(ProductStatusEnum.ACTIVE.getCode())
                .build();
        List<ProductSkuDO> list = baseMapper.selectByCondition(queryDTO);
        return productSkuConverter.convertDO2VO(list);
    }

    @Override
    public ProductSkuVO createSku(ProductSkuDTO skuDTO) {
        String tenantId = StringUtils.hasText(skuDTO.getTenantId())
                ? skuDTO.getTenantId()
                : ProductDefaultConstants.DEFAULT_TENANT;

        // 检查SKU编码是否已存在（复用 selectByCondition）
        ProductSkuDTO checkDTO = ProductSkuDTO.builder()
                .tenantId(tenantId)
                .skuCode(skuDTO.getSkuCode())
                .build();
        List<ProductSkuDO> existing = baseMapper.selectByCondition(checkDTO);
        if (!CollectionUtils.isEmpty(existing)) {
            throw BizException.exp("SKU编码已存在: " + skuDTO.getSkuCode());
        }

        // 使用Converter设置默认值
        ProductSkuDO skuDO = productSkuConverter.convertDTO2DOForCreate(skuDTO, tenantId);

        this.save(skuDO);

        // ==================== 保存关联数据 ====================

        // 1. 保存计费项组合 (BOM)
        saveItemCombinations(skuDTO, tenantId, skuDO.getSkuCode(), skuDO.getRevision());

        // 2. 保存定价模板关联
        savePricingLinks(skuDTO, tenantId, skuDO.getSkuCode(), skuDO.getRevision());

        // 3. 保存定价策略关联
        saveStrategyLinks(skuDTO, tenantId, skuDO.getSkuCode(), skuDO.getRevision());

        // 返回完整数据（包含关联信息）
        return getSkuById(skuDO.getId());
    }

    /**
     * 保存计费项组合
     */
    private void saveItemCombinations(ProductSkuDTO skuDTO, String tenantId, String skuCode, String revision) {
        if (CollectionUtils.isEmpty(skuDTO.getItemCombinations())) {
            return;
        }
        for (SkuItemCombinationDTO combo : skuDTO.getItemCombinations()) {
            combo.setTenantId(tenantId);
            combo.setSkuCode(skuCode);
            combo.setSkuRevision(revision);
            skuItemCombinationService.createCombination(combo);
        }
    }

    /**
     * 保存定价模板关联
     */
    private void savePricingLinks(ProductSkuDTO skuDTO, String tenantId, String skuCode, String revision) {
        if (CollectionUtils.isEmpty(skuDTO.getPricingLinks())) {
            return;
        }
        for (SkuPricingLinkDTO link : skuDTO.getPricingLinks()) {
            SkuPricingLinkDO linkDO = new SkuPricingLinkDO();
            linkDO.setTenantId(tenantId);
            linkDO.setSkuCode(skuCode);
            linkDO.setSkuRevision(revision);
            linkDO.setPricingCode(link.getPricingCode());
            linkDO.setPricingRevision(link.getPricingRevision());
            linkDO.setOverrideFactor(link.getOverrideFactor());
            linkDO.setIsDefault(link.getIsDefault());
            linkDO.setStatus(
                    StringUtils.hasText(link.getStatus()) ? link.getStatus() : ProductStatusEnum.ACTIVE.getCode());
            skuPricingLinkMapper.insert(linkDO);
        }
    }

    /**
     * 保存定价策略关联
     */
    private void saveStrategyLinks(ProductSkuDTO skuDTO, String tenantId, String skuCode, String revision) {
        if (CollectionUtils.isEmpty(skuDTO.getStrategyLinks())) {
            return;
        }
        for (SkuPricingStrategyLinkDTO link : skuDTO.getStrategyLinks()) {
            link.setTenantId(tenantId);
            link.setSkuCode(skuCode);
            link.setSkuRevision(revision);
            skuPricingStrategyLinkService.createLink(link);
        }
    }

    @Override
    public ProductSkuVO updateSku(ProductSkuDTO skuDTO) {

        ProductSkuDO existingSku = this.getById(skuDTO.getId());
        if (existingSku == null) {
            throw BizException.exp("SKU不存在");
        }

        // 转换DTO为DO（MyBatis-Plus的updateById会自动忽略null字段）
        ProductSkuDO updateDO = productSkuConverter.convertDTO2DO(skuDTO);
        updateDO.setId(existingSku.getId());

        this.updateById(updateDO);

        // 重新查询完整数据后转换
        return productSkuConverter.convertDO2VO(this.getById(skuDTO.getId()));
    }

    @Override
    public Boolean deleteSku(Long id) {
        ProductSkuDO skuDO = this.getById(id);
        if (skuDO == null) {
            throw BizException.exp("SKU不存在");
        }
        return this.removeById(skuDO);
    }

    @Override
    public Boolean batchDeleteSku(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return this.removeByIds(ids);
    }

    @Override
    public Boolean publishSku(Long id) {
        ProductSkuDO skuDO = this.getById(id);
        if (skuDO == null) {
            throw BizException.exp("SKU不存在");
        }

        skuDO.setStatus(ProductStatusEnum.ACTIVE.getCode());
        skuDO.setSaleable(NumberConstants.STATUS_TRUE);
        skuDO.setVisible(NumberConstants.STATUS_TRUE);
        skuDO.setPublishTime(new Date());

        return this.updateById(skuDO);
    }

    @Override
    public Boolean unpublishSku(Long id) {
        ProductSkuDO skuDO = this.getById(id);
        if (skuDO == null) {
            throw BizException.exp("SKU不存在");
        }

        skuDO.setStatus(ProductStatusEnum.INACTIVE.getCode());
        skuDO.setSaleable(NumberConstants.STATUS_FALSE);

        return this.updateById(skuDO);
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<ProductSkuDO> buildQueryWrapper(ProductSkuDTO queryDTO) {
        LambdaQueryWrapper<ProductSkuDO> queryWrapper = new LambdaQueryWrapper<>();

        if (queryDTO != null) {
            if (StringUtils.hasText(queryDTO.getTenantId())) {
                queryWrapper.eq(ProductSkuDO::getTenantId, queryDTO.getTenantId());
            }

            if (StringUtils.hasText(queryDTO.getSkuCode())) {
                queryWrapper.like(ProductSkuDO::getSkuCode, queryDTO.getSkuCode());
            }

            if (StringUtils.hasText(queryDTO.getSkuName())) {
                queryWrapper.like(ProductSkuDO::getSkuName, queryDTO.getSkuName());
            }

            if (StringUtils.hasText(queryDTO.getRevision())) {
                queryWrapper.eq(ProductSkuDO::getRevision, queryDTO.getRevision());
            }

            if (StringUtils.hasText(queryDTO.getSkuType())) {
                queryWrapper.eq(ProductSkuDO::getSkuType, queryDTO.getSkuType());
            }

            if (StringUtils.hasText(queryDTO.getStatus())) {
                queryWrapper.eq(ProductSkuDO::getStatus, queryDTO.getStatus());
            }

            if (queryDTO.getSaleable() != null) {
                queryWrapper.eq(ProductSkuDO::getSaleable, queryDTO.getSaleable());
            }
        }

        return queryWrapper;
    }
}
