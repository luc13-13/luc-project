package com.lc.product.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.framework.core.mvc.BizException;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.constants.ProductDefaultConstants;

import com.lc.product.center.converter.ProductInfoConverter;
import com.lc.product.center.domain.dto.ProductInfoDTO;
import com.lc.product.center.domain.entity.ProductInfoDO;
import com.lc.product.center.domain.vo.ProductInfoVO;
import com.lc.product.center.mapper.ProductInfoMapper;
import com.lc.product.center.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 产品信息表(product_center.product_info)表服务实现类
 *
 * @author lucheng
 * @since 2025-08-31
 */
@Service("productInfoService")
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfoDO>
        implements ProductInfoService {

    @Autowired
    private ProductInfoConverter productInfoConverter;

    @Override
    public PaginationResult<ProductInfoVO> queryProductPage(ProductInfoDTO queryDTO) {
        long pageNum = queryDTO.getPageIndex() != null ? queryDTO.getPageIndex() : 1L;
        long pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10L;
        Page<ProductInfoDO> page = Page.of(pageNum, pageSize);

        LambdaQueryWrapper<ProductInfoDO> queryWrapper = buildQueryWrapper(queryDTO);
        queryWrapper.orderByAsc(ProductInfoDO::getSortOrder)
                .orderByDesc(ProductInfoDO::getDtCreated);

        IPage<ProductInfoDO> pageResult = this.page(page, queryWrapper);
        queryDTO.setTotal(pageResult.getTotal());

        // 简单查询：DO → VO（不经过BO）
        List<ProductInfoVO> voList = productInfoConverter.convertDO2VO(pageResult.getRecords());

        return PaginationResult.success(voList, queryDTO);
    }

    @Override
    public List<ProductInfoVO> queryProductList(ProductInfoDTO queryDTO) {
        LambdaQueryWrapper<ProductInfoDO> queryWrapper = buildQueryWrapper(queryDTO);
        queryWrapper.orderByAsc(ProductInfoDO::getSortOrder)
                .orderByDesc(ProductInfoDO::getDtCreated);

        List<ProductInfoDO> list = this.list(queryWrapper);

        // 简单查询：DO → VO（不经过BO）
        return productInfoConverter.convertDO2VO(list);
    }

    @Override
    public ProductInfoVO getProductById(Long id) {
        ProductInfoDO productDO = this.getById(id);
        if (productDO == null) {
            return null;
        }

        // 简单查询：DO → VO（不经过BO）
        return productInfoConverter.convertDO2VO(productDO);
    }

    @Override
    public ProductInfoVO getProductByFourLevelCode(String tenantId, String productCode,
            String subProductCode, String billingItemCode,
            String subBillingItemCode) {
        // 封装查询参数
        ProductInfoDTO queryDTO = ProductInfoDTO.builder()
                .tenantId(tenantId)
                .productCode(productCode)
                .subProductCode(subProductCode)
                .billingItemCode(billingItemCode)
                .subBillingItemCode(subBillingItemCode)
                .build();
        List<ProductInfoDO> list = baseMapper.selectByCondition(queryDTO);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return productInfoConverter.convertDO2VO(list.get(0));
    }

    @Override
    public ProductInfoVO createProduct(ProductInfoDTO productDTO) {
        // 检查是否已存在相同的四层编码（复用 selectByCondition）
        String tenantId = StringUtils.hasText(productDTO.getTenantId()) ? productDTO.getTenantId()
                : ProductDefaultConstants.DEFAULT_TENANT;
        ProductInfoDTO checkDTO = ProductInfoDTO.builder()
                .tenantId(tenantId)
                .productCode(productDTO.getProductCode())
                .subProductCode(productDTO.getSubProductCode())
                .billingItemCode(productDTO.getBillingItemCode())
                .subBillingItemCode(productDTO.getSubBillingItemCode())
                .build();
        List<ProductInfoDO> existing = baseMapper.selectByCondition(checkDTO);
        if (!CollectionUtils.isEmpty(existing)) {
            throw BizException.exp("产品配置已存在");
        }

        // 使用Converter设置默认值
        ProductInfoDO productDO = productInfoConverter.convertDTO2DOForCreate(productDTO, tenantId);

        this.save(productDO);

        // 简单转换：DO → VO
        return productInfoConverter.convertDO2VO(productDO);
    }

    @Override
    public ProductInfoVO updateProduct(ProductInfoDTO productDTO) {
        ProductInfoDO existingProduct = this.getById(productDTO.getId());
        if (existingProduct == null) {
            throw BizException.exp("产品不存在");
        }

        // 转换DTO为DO（MyBatis-Plus的updateById会自动忽略null字段）
        ProductInfoDO updateDO = productInfoConverter.convertDTO2DO(productDTO);
        updateDO.setId(existingProduct.getId());

        this.updateById(updateDO);

        // 简单转换：DO → VO
        return productInfoConverter.convertDO2VO(updateDO);
    }

    @Override
    public Boolean deleteProduct(Long id) {
        ProductInfoDO productDO = this.getById(id);
        if (productDO == null) {
            throw BizException.exp("产品不存在");
        }
        return this.removeById(productDO);
    }

    @Override
    public Boolean batchDeleteProduct(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return this.removeByIds(ids);
    }

    @Override
    public List<String> getProductCodes(String tenantId) {
        String tenant = StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT;
        return baseMapper.findDistinctProductCodes(tenant);
    }

    @Override
    public List<String> getSubProductCodes(String tenantId, String productCode) {
        String tenant = StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT;
        return baseMapper.findDistinctSubProductCodes(tenant, productCode);
    }

    @Override
    public List<String> getBillingItemCodes(String tenantId, String productCode, String subProductCode) {
        String tenant = StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT;
        return baseMapper.findDistinctBillingItemCodes(tenant, productCode, subProductCode);
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<ProductInfoDO> buildQueryWrapper(ProductInfoDTO queryDTO) {
        LambdaQueryWrapper<ProductInfoDO> queryWrapper = new LambdaQueryWrapper<>();

        if (queryDTO != null) {
            if (StringUtils.hasText(queryDTO.getTenantId())) {
                queryWrapper.eq(ProductInfoDO::getTenantId, queryDTO.getTenantId());
            }

            if (StringUtils.hasText(queryDTO.getProductCode())) {
                queryWrapper.eq(ProductInfoDO::getProductCode, queryDTO.getProductCode());
            }

            if (StringUtils.hasText(queryDTO.getSubProductCode())) {
                queryWrapper.eq(ProductInfoDO::getSubProductCode, queryDTO.getSubProductCode());
            }

            if (StringUtils.hasText(queryDTO.getBillingItemCode())) {
                queryWrapper.eq(ProductInfoDO::getBillingItemCode, queryDTO.getBillingItemCode());
            }

            if (StringUtils.hasText(queryDTO.getSubBillingItemCode())) {
                queryWrapper.eq(ProductInfoDO::getSubBillingItemCode, queryDTO.getSubBillingItemCode());
            }

            if (StringUtils.hasText(queryDTO.getProductName())) {
                queryWrapper.like(ProductInfoDO::getProductName, queryDTO.getProductName());
            }

            if (StringUtils.hasText(queryDTO.getStatus())) {
                queryWrapper.eq(ProductInfoDO::getStatus, queryDTO.getStatus());
            }
        }

        return queryWrapper;
    }
}
