package com.lc.product.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.product.center.domain.dto.ProductSkuDTO;
import com.lc.product.center.domain.entity.ProductSkuDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 产品SKU表(product_center.product_sku)表数据库访问层
 * 
 * <p>
 * 采用单一动态查询入口，通过 DTO 参数控制查询条件
 * </p>
 *
 * @author lucheng
 * @since 2025-12-21
 */
@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSkuDO> {

        /**
         * 动态条件查询（唯一的自定义查询入口）
         *
         * @param queryDTO 查询条件
         * @return SKU列表
         */
        List<ProductSkuDO> selectByCondition(ProductSkuDTO queryDTO);
}
