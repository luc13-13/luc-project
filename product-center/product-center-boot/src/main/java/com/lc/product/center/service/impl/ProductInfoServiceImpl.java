package com.lc.product.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.framework.core.mvc.BizException;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.domain.dto.ProductInfoDTO;
import com.lc.product.center.domain.entity.ProductInfoDO;
import com.lc.product.center.domain.vo.ProductInfoVO;
import com.lc.product.center.mapper.ProductInfoMapper;
import com.lc.product.center.service.ProductInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品信息表(product_center.product_info)表服务实现类
 *
 * @author lucheng
 * @since 2025-08-31
 */
@Service("productInfoService")
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfoDO>
        implements ProductInfoService {

    private static final String DEFAULT_TENANT = "DEFAULT";

    @Override
    public PaginationResult<ProductInfoVO> queryProductPage(ProductInfoDTO queryDTO) {
        Page<ProductInfoDO> page = Page.of(queryDTO.getPageIndex(), queryDTO.getPageSize());

        LambdaQueryWrapper<ProductInfoDO> queryWrapper = buildQueryWrapper(queryDTO);
        queryWrapper.orderByAsc(ProductInfoDO::getSortOrder)
                .orderByDesc(ProductInfoDO::getDtCreated);

        IPage<ProductInfoDO> pageResult = this.page(page, queryWrapper);
        queryDTO.setTotal(pageResult.getTotal());

        List<ProductInfoVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PaginationResult.success(voList, queryDTO);
    }

    @Override
    public List<ProductInfoVO> queryProductList(ProductInfoDTO queryDTO) {
        LambdaQueryWrapper<ProductInfoDO> queryWrapper = buildQueryWrapper(queryDTO);
        queryWrapper.orderByAsc(ProductInfoDO::getSortOrder)
                .orderByDesc(ProductInfoDO::getDtCreated);

        List<ProductInfoDO> list = this.list(queryWrapper);
        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductInfoVO getProductById(Long id) {
        ProductInfoDO productDO = this.getById(id);
        if (productDO == null) {
            return null;
        }
        return convertToVO(productDO);
    }

    @Override
    public ProductInfoVO getProductByFourLevelCode(String tenantId, String productCode,
            String subProductCode, String billingItemCode,
            String subBillingItemCode) {
        ProductInfoDO productDO = baseMapper.findByFourLevelCode(
                tenantId, productCode, subProductCode, billingItemCode, subBillingItemCode);
        if (productDO == null) {
            return null;
        }
        return convertToVO(productDO);
    }

    @Override
    public ProductInfoVO createProduct(ProductInfoDTO productDTO) {
        // 检查是否已存在相同的四层编码
        String tenantId = StringUtils.hasText(productDTO.getTenantId()) ? productDTO.getTenantId() : DEFAULT_TENANT;
        ProductInfoDO existing = baseMapper.findByFourLevelCode(
                tenantId,
                productDTO.getProductCode(),
                productDTO.getSubProductCode(),
                productDTO.getBillingItemCode(),
                productDTO.getSubBillingItemCode());
        if (existing != null) {
            throw BizException.exp("产品配置已存在");
        }

        ProductInfoDO productDO = new ProductInfoDO();
        BeanUtils.copyProperties(productDTO, productDO);
        productDO.setTenantId(tenantId);

        // 设置默认值
        if (productDO.getPriceFactor() == null) {
            productDO.setPriceFactor(BigDecimal.ONE);
        }
        if (!StringUtils.hasText(productDO.getStatus())) {
            productDO.setStatus("ACTIVE");
        }
        if (productDO.getSortOrder() == null) {
            productDO.setSortOrder(0);
        }

        Date now = new Date();
        productDO.setDtCreated(now);
        productDO.setDeleted(0);

        this.save(productDO);
        return convertToVO(productDO);
    }

    @Override
    public ProductInfoVO updateProduct(Long id, ProductInfoDTO productDTO) {
        ProductInfoDO existingProduct = this.getById(id);
        if (existingProduct == null) {
            throw BizException.exp("产品不存在");
        }

        // 不更新四层编码和租户ID
        productDTO.setProductCode(null);
        productDTO.setSubProductCode(null);
        productDTO.setBillingItemCode(null);
        productDTO.setSubBillingItemCode(null);
        productDTO.setTenantId(null);

        BeanUtils.copyProperties(productDTO, existingProduct, getNullPropertyNames(productDTO));
        existingProduct.setDtModified(new Date());

        this.updateById(existingProduct);
        return convertToVO(existingProduct);
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
        String tenant = StringUtils.hasText(tenantId) ? tenantId : DEFAULT_TENANT;
        return baseMapper.findDistinctProductCodes(tenant);
    }

    @Override
    public List<String> getSubProductCodes(String tenantId, String productCode) {
        String tenant = StringUtils.hasText(tenantId) ? tenantId : DEFAULT_TENANT;
        return baseMapper.findDistinctSubProductCodes(tenant, productCode);
    }

    @Override
    public List<String> getBillingItemCodes(String tenantId, String productCode, String subProductCode) {
        String tenant = StringUtils.hasText(tenantId) ? tenantId : DEFAULT_TENANT;
        return baseMapper.findDistinctBillingItemCodes(tenant, productCode, subProductCode);
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<ProductInfoDO> buildQueryWrapper(ProductInfoDTO queryDTO) {
        LambdaQueryWrapper<ProductInfoDO> queryWrapper = new LambdaQueryWrapper<>();

        if (queryDTO != null) {
            // 租户过滤
            if (StringUtils.hasText(queryDTO.getTenantId())) {
                queryWrapper.eq(ProductInfoDO::getTenantId, queryDTO.getTenantId());
            }

            // 产品编码精确匹配
            if (StringUtils.hasText(queryDTO.getProductCode())) {
                queryWrapper.eq(ProductInfoDO::getProductCode, queryDTO.getProductCode());
            }

            // 规格族编码
            if (StringUtils.hasText(queryDTO.getSubProductCode())) {
                queryWrapper.eq(ProductInfoDO::getSubProductCode, queryDTO.getSubProductCode());
            }

            // 计费项编码
            if (StringUtils.hasText(queryDTO.getBillingItemCode())) {
                queryWrapper.eq(ProductInfoDO::getBillingItemCode, queryDTO.getBillingItemCode());
            }

            // 计费规格编码
            if (StringUtils.hasText(queryDTO.getSubBillingItemCode())) {
                queryWrapper.eq(ProductInfoDO::getSubBillingItemCode, queryDTO.getSubBillingItemCode());
            }

            // 产品名称模糊查询
            if (StringUtils.hasText(queryDTO.getProductName())) {
                queryWrapper.like(ProductInfoDO::getProductName, queryDTO.getProductName());
            }

            // 状态过滤
            if (StringUtils.hasText(queryDTO.getStatus())) {
                queryWrapper.eq(ProductInfoDO::getStatus, queryDTO.getStatus());
            }
        }

        return queryWrapper;
    }

    /**
     * 转换为VO对象
     */
    private ProductInfoVO convertToVO(ProductInfoDO productDO) {
        ProductInfoVO vo = new ProductInfoVO();
        BeanUtils.copyProperties(productDO, vo);

        // 计算单价 = basePrice * priceFactor
        if (productDO.getBasePrice() != null && productDO.getPriceFactor() != null) {
            vo.setUnitPrice(productDO.getBasePrice().multiply(productDO.getPriceFactor()));
        } else if (productDO.getBasePrice() != null) {
            vo.setUnitPrice(productDO.getBasePrice());
        }

        // 设置状态描述
        if (productDO.getStatus() != null) {
            switch (productDO.getStatus()) {
                case "ACTIVE":
                    vo.setStatusDesc("生效");
                    break;
                case "INACTIVE":
                    vo.setStatusDesc("失效");
                    break;
                case "DRAFT":
                    vo.setStatusDesc("草稿");
                    break;
                default:
                    vo.setStatusDesc(productDO.getStatus());
            }
        }

        return vo;
    }

    /**
     * 获取对象中值为null的属性名数组
     */
    private String[] getNullPropertyNames(Object source) {
        final org.springframework.beans.BeanWrapper src = new org.springframework.beans.BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        java.util.Set<String> emptyNames = new java.util.HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
