package com.lc.product.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.product.center.domain.entity.PricingStrategyDO;
import com.lc.product.center.domain.dto.PricingStrategyDTO;
import com.lc.product.center.domain.bo.PricingStrategyBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 定价策略表(product_center.pricing_strategy)表数据库访问层
 *
 * @author lucheng
 * @since 2026-02-06
 */
@Mapper
public interface PricingStrategyMapper extends BaseMapper<PricingStrategyDO> {

    /** 
     * 条件查询列表
     */
    List<PricingStrategyBO> selectListByDTO(@Param("dto") PricingStrategyDTO dto);
}

