package com.lc.product.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.product.center.domain.entity.ProductInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 产品信息表(product_center.product_info)表数据库访问层
 *
 * @author lucheng
 * @since 2025-08-31
 */
@Mapper
public interface ProductInfoMapper extends BaseMapper<ProductInfoDO> {

    /**
     * 根据四层编码查询产品信息
     *
     * @param tenantId           租户ID
     * @param productCode        产品编码
     * @param subProductCode     规格族编码
     * @param billingItemCode    计费项编码
     * @param subBillingItemCode 计费规格编码
     * @return 产品信息
     */
    @Select("SELECT * FROM product_info WHERE tenant_id = #{tenantId} " +
            "AND product_code = #{productCode} " +
            "AND sub_product_code = #{subProductCode} " +
            "AND billing_item_code = #{billingItemCode} " +
            "AND sub_billing_item_code = #{subBillingItemCode} " +
            "AND deleted = 0")
    ProductInfoDO findByFourLevelCode(@Param("tenantId") String tenantId,
            @Param("productCode") String productCode,
            @Param("subProductCode") String subProductCode,
            @Param("billingItemCode") String billingItemCode,
            @Param("subBillingItemCode") String subBillingItemCode);

    /**
     * 查询产品编码列表（去重）
     *
     * @param tenantId 租户ID
     * @return 产品编码列表
     */
    @Select("SELECT DISTINCT product_code FROM product_info WHERE tenant_id = #{tenantId} AND deleted = 0 AND status = 'ACTIVE'")
    List<String> findDistinctProductCodes(@Param("tenantId") String tenantId);

    /**
     * 根据产品编码查询规格族列表（去重）
     *
     * @param tenantId    租户ID
     * @param productCode 产品编码
     * @return 规格族编码列表
     */
    @Select("SELECT DISTINCT sub_product_code FROM product_info WHERE tenant_id = #{tenantId} " +
            "AND product_code = #{productCode} AND deleted = 0 AND status = 'ACTIVE'")
    List<String> findDistinctSubProductCodes(@Param("tenantId") String tenantId,
            @Param("productCode") String productCode);

    /**
     * 根据产品编码和规格族查询计费项列表（去重）
     *
     * @param tenantId       租户ID
     * @param productCode    产品编码
     * @param subProductCode 规格族编码
     * @return 计费项编码列表
     */
    @Select("SELECT DISTINCT billing_item_code FROM product_info WHERE tenant_id = #{tenantId} " +
            "AND product_code = #{productCode} AND sub_product_code = #{subProductCode} " +
            "AND deleted = 0 AND status = 'ACTIVE'")
    List<String> findDistinctBillingItemCodes(@Param("tenantId") String tenantId,
            @Param("productCode") String productCode,
            @Param("subProductCode") String subProductCode);
}
