package com.lc.product.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.domain.dto.ProductSkuDTO;
import com.lc.product.center.domain.entity.ProductSkuDO;
import com.lc.product.center.domain.vo.ProductSkuDetailsVO;
import com.lc.product.center.domain.vo.ProductSkuVO;

import java.util.List;

/**
 * 产品SKU表(product_center.product_sku)表服务接口
 *
 * @author lucheng
 * @since 2026-02-06
 */
public interface ProductSkuService extends IService<ProductSkuDO> {

    /**
     * 列表查询
     *
     * @param queryDTO 查询条件
     * @return 产品SKU列表
     */
    List<ProductSkuVO> listByCondition(ProductSkuDTO queryDTO);

    /**
     * 分页查询
     *
     * @param queryDTO 查询条件（含分页参数）
     * @return 分页结果
     */
    PaginationResult<ProductSkuVO> pageByCondition(ProductSkuDTO queryDTO);

    /**
     * 根据ID查询详情
     *
     * @param queryDTO 查询参数
     * @return 产品SKU详情VO
     */
    ProductSkuDetailsVO getDetail(ProductSkuDTO queryDTO);


    /**
     * 创建产品SKU
     *
     * @param dto 产品SKU DTO
     * @return 创建后的产品SKU VO
     */
    ProductSkuVO createSku(ProductSkuDTO dto);

    /**
     * 更新产品SKU
     *
     * @param dto 产品SKU DTO
     * @return 更新后的产品SKU VO
     */
    ProductSkuVO updateSku(ProductSkuDTO dto);

    /**
     * 删除产品SKU
     *
     * @param id 主键ID
     * @return 是否成功
     */
    Boolean deleteSku(Long id);
}
