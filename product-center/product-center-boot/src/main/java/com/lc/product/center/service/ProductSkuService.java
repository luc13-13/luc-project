package com.lc.product.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.domain.dto.ProductSkuDTO;
import com.lc.product.center.domain.entity.ProductSkuDO;
import com.lc.product.center.domain.vo.ProductSkuVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 产品SKU表(product_center.product_sku)表服务接口
 *
 * @author lucheng
 * @since 2025-12-21
 */
public interface ProductSkuService extends IService<ProductSkuDO> {

    /**
     * 分页查询SKU列表
     *
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    PaginationResult<ProductSkuVO> querySkuPage(ProductSkuDTO queryDTO);

    /**
     * 查询SKU列表
     *
     * @param queryDTO 查询参数
     * @return SKU列表
     */
    List<ProductSkuVO> querySkuList(ProductSkuDTO queryDTO);

    /**
     * 根据ID查询SKU详情
     *
     * @param id SKU ID
     * @return SKU详情
     */
    ProductSkuVO getSkuById(Long id);

    /**
     * 根据SKU编码查询
     *
     * @param tenantId 租户ID
     * @param skuCode  SKU编码
     * @return SKU详情
     */
    ProductSkuVO getSkuByCode(String tenantId, String skuCode);

    /**
     * 根据产品编码查询SKU列表
     *
     * @param tenantId    租户ID
     * @param productCode 产品编码
     * @return SKU列表
     */
    List<ProductSkuVO> getSkusByProductCode(String tenantId, String productCode);

    /**
     * 查询可售SKU列表
     *
     * @param tenantId 租户ID
     * @return 可售SKU列表
     */
    List<ProductSkuVO> getSaleableSkus(String tenantId);

    /**
     * 创建SKU
     *
     * @param skuDTO SKU信息
     * @return 创建的SKU信息
     */
    @Transactional(rollbackFor = Exception.class)
    ProductSkuVO createSku(ProductSkuDTO skuDTO);

    /**
     * 更新SKU信息
     *
     * @param skuDTO SKU DTO（包含ID）
     * @return 更新后的SKU信息
     */
    @Transactional(rollbackFor = Exception.class)
    ProductSkuVO updateSku(ProductSkuDTO skuDTO);

    /**
     * 删除SKU
     *
     * @param id SKU ID
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    Boolean deleteSku(Long id);

    /**
     * 批量删除SKU
     *
     * @param ids SKU ID列表
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    Boolean batchDeleteSku(List<Long> ids);

    /**
     * 上架SKU
     *
     * @param id SKU ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    Boolean publishSku(Long id);

    /**
     * 下架SKU
     *
     * @param id SKU ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    Boolean unpublishSku(Long id);
}
