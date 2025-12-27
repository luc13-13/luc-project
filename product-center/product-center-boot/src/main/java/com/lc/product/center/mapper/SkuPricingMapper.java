package com.lc.product.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.product.center.domain.dto.SkuPricingDTO;
import com.lc.product.center.domain.entity.SkuPricingDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * SKU定价表(product_center.sku_pricing)表Mapper接口
 * 
 * <p>
 * 采用单一动态查询入口，通过 DTO 参数控制查询条件
 * </p>
 *
 * @author lucheng
 * @since 2025-12-27
 */
@Mapper
public interface SkuPricingMapper extends BaseMapper<SkuPricingDO> {

        /**
         * 动态条件查询（唯一的自定义查询入口）
         * 
         * <p>
         * 通过 DTO 中不同字段的组合，实现：
         * <ul>
         * <li>按 SKU 查询：设置 tenantId + skuCode</li>
         * <li>按 SKU + 周期查询：设置 tenantId + skuCode + billingCycle</li>
         * <li>查询有效定价：设置 effectiveOnly = true</li>
         * </ul>
         * </p>
         *
         * @param queryDTO 查询条件
         * @return 定价列表
         */
        List<SkuPricingDO> selectByCondition(SkuPricingDTO queryDTO);
}
