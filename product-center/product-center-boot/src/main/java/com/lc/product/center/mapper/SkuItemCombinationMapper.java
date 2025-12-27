package com.lc.product.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.product.center.domain.dto.SkuItemCombinationDTO;
import com.lc.product.center.domain.entity.SkuItemCombinationDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * SKU计费项组合表(product_center.sku_item_combination)表Mapper接口
 * 
 * <p>
 * 采用单一动态查询入口，通过 DTO 参数控制查询条件
 * </p>
 *
 * @author lucheng
 * @since 2025-12-27
 */
@Mapper
public interface SkuItemCombinationMapper extends BaseMapper<SkuItemCombinationDO> {

    /**
     * 动态条件查询（唯一的自定义查询入口）
     *
     * @param queryDTO 查询条件
     * @return 组合列表
     */
    List<SkuItemCombinationDO> selectByCondition(SkuItemCombinationDTO queryDTO);
}
