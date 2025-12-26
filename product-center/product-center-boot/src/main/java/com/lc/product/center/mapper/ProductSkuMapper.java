package com.lc.product.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.product.center.domain.entity.ProductSkuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 产品SKU表(product_center.product_sku)表数据库访问层
 *
 * @author lucheng
 * @since 2025-12-21
 */
@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSkuDO> {

    /**
     * 根据SKU编码查询
     *
     * @param tenantId 租户ID
     * @param skuCode  SKU编码
     * @return SKU信息
     */
    @Select("SELECT * FROM product_sku WHERE tenant_id = #{tenantId} " +
            "AND sku_code = #{skuCode} AND deleted = 0")
    ProductSkuDO findBySkuCode(@Param("tenantId") String tenantId,
            @Param("skuCode") String skuCode);

    /**
     * 根据产品编码查询SKU列表
     *
     * @param tenantId    租户ID
     * @param productCode 产品编码
     * @return SKU列表
     */
    @Select("SELECT * FROM product_sku WHERE tenant_id = #{tenantId} " +
            "AND product_code = #{productCode} AND deleted = 0 AND status = 'ACTIVE'")
    List<ProductSkuDO> findByProductCode(@Param("tenantId") String tenantId,
            @Param("productCode") String productCode);

    /**
     * 根据产品编码和规格族编码查询SKU列表
     *
     * @param tenantId       租户ID
     * @param productCode    产品编码
     * @param subProductCode 规格族编码
     * @return SKU列表
     */
    @Select("SELECT * FROM product_sku WHERE tenant_id = #{tenantId} " +
            "AND product_code = #{productCode} AND sub_product_code = #{subProductCode} " +
            "AND deleted = 0 AND status = 'ACTIVE'")
    List<ProductSkuDO> findByProductAndSubProduct(@Param("tenantId") String tenantId,
            @Param("productCode") String productCode,
            @Param("subProductCode") String subProductCode);

    /**
     * 查询可售SKU列表
     *
     * @param tenantId 租户ID
     * @return 可售SKU列表
     */
    @Select("SELECT * FROM product_sku WHERE tenant_id = #{tenantId} " +
            "AND deleted = 0 AND status = 'ACTIVE' AND saleable = 1 AND visible = 1")
    List<ProductSkuDO> findSaleableSkus(@Param("tenantId") String tenantId);
}
