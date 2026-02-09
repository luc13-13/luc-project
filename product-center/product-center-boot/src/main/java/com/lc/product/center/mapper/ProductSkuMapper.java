package com.lc.product.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lc.product.center.domain.bo.ProductSkuBO;
import com.lc.product.center.domain.dto.ProductSkuDTO;
import com.lc.product.center.domain.entity.ProductSkuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品SKU表(product_center.product_sku)表数据库访问层
 * <p>
 * 采用单一动态查询入口，通过 DTO 参数控制查询条件
 * </p>
 *
 * @author lucheng
 * @since 2026-02-06
 */
@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSkuDO> {

    /**
     * 动态条件查询（列表查询）
     *
     * @param dto 查询条件
     * @return 产品SKU列表
     */
    List<ProductSkuDO> selectByCondition(@Param("dto") ProductSkuDTO dto);

    /**
     * 动态条件分页查询（使用 MyBatis-Plus IPage）
     *
     * @param page 分页对象
     * @param dto  查询条件
     * @return 分页结果
     */
    IPage<ProductSkuDO> selectPageByCondition(IPage<ProductSkuDO> page, @Param("dto") ProductSkuDTO dto);

    /**
     * 根据 SKU 编码查询详情（多表聚合查询）
     * <p>
     * 聚合：product_sku + sku_item_combination + sku_pricing_link +
     * sku_pricing_strategy_link
     * </p>
     *
     * @param tenantId    租户ID
     * @param skuCode     SKU编码
     * @param skuRevision SKU版本号（可选，为空则查询当前版本）
     * @return 产品SKU业务对象
     */
    ProductSkuBO selectDetailBySkuCode(@Param("tenantId") String tenantId,
            @Param("skuCode") String skuCode,
            @Param("skuRevision") String skuRevision);
}
