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
import com.lc.product.center.domain.entity.ProductSkuDO;
import com.lc.product.center.domain.vo.ProductSkuVO;
import com.lc.product.center.mapper.ProductSkuMapper;
import com.lc.product.center.service.ProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

    @Override
    public PaginationResult<ProductSkuVO> querySkuPage(ProductSkuDTO queryDTO) {
        long pageNum = queryDTO.getPageIndex() != null ? queryDTO.getPageIndex() : 1L;
        long pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10L;
        Page<ProductSkuDO> page = Page.of(pageNum, pageSize);

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

        // 简单查询：DO → VO（不经过BO）
        return productSkuConverter.convertDO2VO(skuDO);
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
        return productSkuConverter.convertDO2VO(list.get(0));
    }

    @Override
    public List<ProductSkuVO> getSkusByProductCode(String tenantId, String productCode) {
        // 封装查询参数
        ProductSkuDTO queryDTO = ProductSkuDTO.builder()
                .tenantId(StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT)
                .productCode(productCode)
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

        // 简单转换：DO → VO
        return productSkuConverter.convertDO2VO(skuDO);
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

        // 简单转换：DO → VO
        return productSkuConverter.convertDO2VO(updateDO);
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

            if (StringUtils.hasText(queryDTO.getProductCode())) {
                queryWrapper.eq(ProductSkuDO::getProductCode, queryDTO.getProductCode());
            }

            if (StringUtils.hasText(queryDTO.getSubProductCode())) {
                queryWrapper.eq(ProductSkuDO::getSubProductCode, queryDTO.getSubProductCode());
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
