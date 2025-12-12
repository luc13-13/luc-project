package com.lc.product.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.domain.dto.ProductInfoDTO;
import com.lc.product.center.domain.entity.ProductInfoDO;
import com.lc.product.center.domain.vo.ProductInfoVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 产品信息表(product_center.product_info)表服务接口
 *
 * @author lucheng
 * @since 2025-08-31
 */
public interface ProductInfoService extends IService<ProductInfoDO> {

    /**
     * 分页查询产品列表
     *
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    PaginationResult<ProductInfoVO> queryProductPage(ProductInfoDTO queryDTO);

    /**
     * 查询产品列表
     *
     * @param queryDTO 查询参数
     * @return 产品列表
     */
    List<ProductInfoVO> queryProductList(ProductInfoDTO queryDTO);

    /**
     * 根据ID查询产品详情
     *
     * @param id 产品ID
     * @return 产品详情
     */
    ProductInfoVO getProductById(Long id);

    /**
     * 创建产品
     *
     * @param productDTO 产品信息
     * @return 创建的产品信息
     */
    ProductInfoVO createProduct(ProductInfoDTO productDTO);

    /**
     * 更新产品
     *
     * @param id 产品ID
     * @param productDTO 产品信息
     * @return 更新的产品信息
     */
    ProductInfoVO updateProduct(Long id, ProductInfoDTO productDTO);

    /**
     * 删除产品
     *
     * @param id 产品ID
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    Boolean deleteProduct(Long id);

    /**
     * 批量删除产品
     *
     * @param ids 产品ID列表
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    Boolean batchDeleteProduct(List<Long> ids);
}

