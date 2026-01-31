package com.lc.product.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.product.center.domain.entity.SkuPricingLinkDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SKU与定价关联表(product_center.sku_pricing_link)表Mapper接口
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Mapper
public interface SkuPricingLinkMapper extends BaseMapper<SkuPricingLinkDO> {

    /**
     * 根据SKU版本查询关联的定价
     *
     * @param tenantId    租户ID
     * @param skuCode     SKU编码
     * @param skuRevision SKU版本号
     * @return 关联列表
     */
    List<SkuPricingLinkDO> selectBySkuRevision(
            @Param("tenantId") String tenantId,
            @Param("skuCode") String skuCode,
            @Param("skuRevision") String skuRevision);

    /**
     * 根据SKU版本查询默认定价关联
     *
     * @param tenantId    租户ID
     * @param skuCode     SKU编码
     * @param skuRevision SKU版本号
     * @return 默认定价关联
     */
    SkuPricingLinkDO selectDefaultBySkuRevision(
            @Param("tenantId") String tenantId,
            @Param("skuCode") String skuCode,
            @Param("skuRevision") String skuRevision);
}
