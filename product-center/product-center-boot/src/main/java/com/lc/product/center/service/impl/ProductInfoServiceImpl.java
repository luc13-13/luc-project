package com.lc.product.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.domain.dto.ProductInfoDTO;
import com.lc.product.center.domain.entity.ProductInfoDO;
import com.lc.product.center.domain.vo.ProductInfoVO;
import com.lc.product.center.mapper.ProductInfoMapper;
import com.lc.product.center.service.ProductInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfoDO> implements ProductInfoService {

    @Override
    public PaginationResult<ProductInfoVO> queryProductPage(ProductInfoDTO queryDTO) {
        long pageNum = queryDTO.getPageIndex() != null ? queryDTO.getPageIndex() : 1L;
        long pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10L;
        Page<ProductInfoDO> page = Page.of(pageNum, pageSize);

        LambdaQueryWrapper<ProductInfoDO> queryWrapper = buildQueryWrapper(queryDTO);
        queryWrapper.orderByDesc(ProductInfoDO::getDtCreated);

        IPage<ProductInfoDO> pageResult = this.page(page, queryWrapper);
        // 设置总数
        queryDTO.setTotal(pageResult.getTotal());
        // 转换为VO
        List<ProductInfoVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PaginationResult.success(voList, queryDTO);
    }

    @Override
    public List<ProductInfoVO> queryProductList(ProductInfoDTO queryDTO) {
        LambdaQueryWrapper<ProductInfoDO> queryWrapper = buildQueryWrapper(queryDTO);
        queryWrapper.orderByDesc(ProductInfoDO::getDtCreated);

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
    public ProductInfoVO createProduct(ProductInfoDTO productDTO) {
        ProductInfoDO productDO = new ProductInfoDO();
        BeanUtils.copyProperties(productDTO, productDO);

        // 设置创建时间和更新时间
        Date now = new Date();
        productDO.setDtCreated(now);
        productDO.setDtModified(now);
        productDO.setDeleted((short) 0);

        this.save(productDO);
        return convertToVO(productDO);
    }

    @Override
    public ProductInfoVO updateProduct(Long id, ProductInfoDTO productDTO) {
        ProductInfoDO existingProduct = this.getById(id);
        if (existingProduct == null) {
            throw new RuntimeException("产品不存在");
        }

        BeanUtils.copyProperties(productDTO, existingProduct);
        existingProduct.setDtModified(new Date());

        this.updateById(existingProduct);
        return convertToVO(existingProduct);
    }

    @Override
    public Boolean deleteProduct(Long id) {
        ProductInfoDO productDO = this.getById(id);
        if (productDO == null) {
            return false;
        }

        // 逻辑删除
        productDO.setDeleted((short) 1);
        productDO.setDtModified(new Date());

        return this.updateById(productDO);
    }

    @Override
    public Boolean batchDeleteProduct(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        List<ProductInfoDO> products = this.listByIds(ids);
        Date now = new Date();

        products.forEach(product -> {
            product.setDeleted((short) 1);
            product.setDtModified(now);
        });

        return this.updateBatchById(products);
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<ProductInfoDO> buildQueryWrapper(ProductInfoDTO queryDTO) {
        LambdaQueryWrapper<ProductInfoDO> queryWrapper = new LambdaQueryWrapper<>();

        // 未删除的记录
        queryWrapper.eq(ProductInfoDO::getDeleted, 0);

        if (queryDTO != null) {
            if (StringUtils.hasText(queryDTO.getProductName())) {
                queryWrapper.like(ProductInfoDO::getProductName, queryDTO.getProductName());
            }

            if (StringUtils.hasText(queryDTO.getProductCode())) {
                queryWrapper.like(ProductInfoDO::getProductCode, queryDTO.getProductCode());
            }

            if (queryDTO.getStatus() != null) {
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

        // 设置状态描述
        if (productDO.getStatus() != null) {
            vo.setStatusDesc(productDO.getStatus() == 1 ? "生效" : "失效");
        }

        return vo;
    }
}

