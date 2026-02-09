package com.lc.product.center.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.framework.core.mvc.BizException;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.constants.ProductDefaultConstants;
import com.lc.product.center.converter.ProductInfoConverter;
import com.lc.product.center.domain.bo.QueryFilter;
import com.lc.product.center.domain.dto.ProductInfoDTO;
import com.lc.product.center.domain.entity.ProductInfoDO;
import com.lc.product.center.domain.vo.ProductInfoVO;
import com.lc.product.center.mapper.ProductInfoMapper;
import com.lc.product.center.service.ProductInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 产品信息表(product_center.product_info)表服务实现类
 *
 * @author lucheng
 * @since 2026-02-06
 */
@Service
@AllArgsConstructor
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfoDO>
        implements ProductInfoService {

    private final ProductInfoConverter productInfoConverter;

    @Override
    public List<ProductInfoVO> listByCondition(ProductInfoDTO queryDTO) {
        // 设置默认租户
        setDefaultTenantIfEmpty(queryDTO);

        List<ProductInfoDO> list = baseMapper.selectByCondition(queryDTO);
        return productInfoConverter.convertDO2VO(list);
    }

    @Override
    public PaginationResult<ProductInfoVO> pageByCondition(ProductInfoDTO queryDTO) {
        // 设置默认租户
        setDefaultTenantIfEmpty(queryDTO);

        // 使用 MyBatis-Plus 原生分页
        IPage<ProductInfoDO> page = new Page<>(queryDTO.getPageIndex(), queryDTO.getPageSize());
        IPage<ProductInfoDO> result = baseMapper.selectPageByCondition(page, queryDTO);

        List<ProductInfoVO> voList = productInfoConverter.convertDO2VO(result.getRecords());

        // 设置 total 用于分页结果
        queryDTO.setTotal(result.getTotal());
        return PaginationResult.success(voList, queryDTO);
    }

    @Override
    public ProductInfoVO getDetailById(Long id) {
        ProductInfoDO entity = this.getById(id);
        if (entity == null) {
            return null;
        }
        return productInfoConverter.convertDO2VO(entity);
    }

    @Override
    public ProductInfoVO createProduct(ProductInfoDTO dto) {
        String tenantId = StringUtils.hasText(dto.getTenantId())
                ? dto.getTenantId()
                : ProductDefaultConstants.DEFAULT_TENANT;

        // 检查四层编码是否已存在
        ProductInfoDTO checkDTO = ProductInfoDTO.builder()
                .tenantId(tenantId)
                .productCode(dto.getProductCode())
                .subProductCode(dto.getSubProductCode())
                .billingItemCode(dto.getBillingItemCode())
                .subBillingItemCode(dto.getSubBillingItemCode())
                .build();
        List<ProductInfoDO> existing = baseMapper.selectByCondition(checkDTO);
        if (!CollectionUtils.isEmpty(existing)) {
            throw BizException.exp("产品编码已存在: " + dto.getProductCode() + "/"
                    + dto.getSubProductCode() + "/" + dto.getBillingItemCode() + "/" + dto.getSubBillingItemCode());
        }

        // 转换并设置默认值
        ProductInfoDO entity = productInfoConverter.convertDTO2DOForCreate(dto, tenantId);
        this.save(entity);

        return productInfoConverter.convertDO2VO(entity);
    }

    @Override
    public ProductInfoVO updateProduct(ProductInfoDTO dto) {
        ProductInfoDO existing = this.getById(dto.getId());
        if (existing == null) {
            throw BizException.exp("产品信息不存在");
        }

        // 转换 DTO 为 DO
        ProductInfoDO updateDO = productInfoConverter.convertDTO2DO(dto);
        updateDO.setId(existing.getId());

        this.updateById(updateDO);

        return productInfoConverter.convertDO2VO(this.getById(dto.getId()));
    }

    @Override
    public Boolean deleteProduct(Long id) {
        ProductInfoDO existing = this.getById(id);
        if (existing == null) {
            throw BizException.exp("产品信息不存在");
        }
        return this.removeById(id);
    }

    @Override
    public Boolean batchDeleteProduct(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        return this.removeByIds(ids);
    }

    @Override
    public List<QueryFilter> getProductCodes(String tenantId) {
        String tenant = StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT;
        ProductInfoDTO queryDTO = ProductInfoDTO.builder().tenantId(tenant).build();
        List<ProductInfoDO> list = baseMapper.selectByCondition(queryDTO);
        return list.stream()
                .map(item -> new QueryFilter(item.getProductCode(), item.getProductName()))
                .distinct()
                .sorted()
                .toList();
    }

    @Override
    public List<QueryFilter> getSubProductCodes(String tenantId, String productCode) {
        String tenant = StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT;
        ProductInfoDTO queryDTO = ProductInfoDTO.builder()
                .tenantId(tenant)
                .productCode(productCode)
                .build();
        List<ProductInfoDO> list = baseMapper.selectByCondition(queryDTO);
        return list.stream()
                .map(item -> new QueryFilter(item.getSubProductCode(), item.getSubProductName()))
                .distinct()
                .sorted()
                .toList();
    }

    @Override
    public List<QueryFilter> getBillingItemCodes(String tenantId, String productCode, String subProductCode) {
        String tenant = StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT;
        ProductInfoDTO queryDTO = ProductInfoDTO.builder()
                .tenantId(tenant)
                .productCode(productCode)
                .subProductCode(subProductCode)
                .build();
        List<ProductInfoDO> list = baseMapper.selectByCondition(queryDTO);
        return list.stream()
                .map(item -> new QueryFilter(item.getBillingItemCode(), item.getBillingItemName()))
                .distinct()
                .sorted()
                .toList();
    }

    /**
     * 设置默认租户
     */
    private void setDefaultTenantIfEmpty(ProductInfoDTO dto) {
        if (!StringUtils.hasText(dto.getTenantId())) {
            dto.setTenantId(ProductDefaultConstants.DEFAULT_TENANT);
        }
    }
}
