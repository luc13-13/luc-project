package com.lc.product.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.domain.bo.QueryFilter;
import com.lc.product.center.domain.dto.ProductInfoDTO;
import com.lc.product.center.domain.entity.ProductInfoDO;
import com.lc.product.center.domain.vo.ProductInfoVO;

import java.util.List;

/**
 * 产品信息表(product_center.product_info)表服务接口
 *
 * @author lucheng
 * @since 2026-02-06
 */
public interface ProductInfoService extends IService<ProductInfoDO> {

    /**
     * 列表查询
     *
     * @param queryDTO 查询条件
     * @return 产品信息列表
     */
    List<ProductInfoVO> listByCondition(ProductInfoDTO queryDTO);

    /**
     * 分页查询
     *
     * @param queryDTO 查询条件（含分页参数）
     * @return 分页结果
     */
    PaginationResult<ProductInfoVO> pageByCondition(ProductInfoDTO queryDTO);

    /**
     * 根据ID查询详情
     *
     * @param id 主键ID
     * @return 产品信息VO
     */
    ProductInfoVO getDetailById(Long id);

    /**
     * 创建产品信息
     *
     * @param dto 产品信息DTO
     * @return 创建后的产品信息VO
     */
    ProductInfoVO createProduct(ProductInfoDTO dto);

    /**
     * 更新产品信息
     *
     * @param dto 产品信息DTO
     * @return 更新后的产品信息VO
     */
    ProductInfoVO updateProduct(ProductInfoDTO dto);

    /**
     * 删除产品信息（逻辑删除）
     *
     * @param id 主键ID
     * @return 是否成功
     */
    Boolean deleteProduct(Long id);

    /**
     * 批量删除产品信息
     *
     * @param ids ID列表
     * @return 是否成功
     */
    Boolean batchDeleteProduct(List<Long> ids);

    /**
     * 获取去重的产品编码列表（级联筛选）
     *
     * @param tenantId 租户ID
     * @return QueryFilter列表
     */
    List<QueryFilter> getProductCodes(String tenantId);

    /**
     * 获取去重的规格族编码列表（级联筛选）
     *
     * @param tenantId    租户ID
     * @param productCode 产品编码
     * @return QueryFilter列表
     */
    List<QueryFilter> getSubProductCodes(String tenantId, String productCode);

    /**
     * 获取去重的计费项编码列表（级联筛选）
     *
     * @param tenantId       租户ID
     * @param productCode    产品编码
     * @param subProductCode 规格族编码
     * @return QueryFilter列表
     */
    List<QueryFilter> getBillingItemCodes(String tenantId, String productCode, String subProductCode);
}
