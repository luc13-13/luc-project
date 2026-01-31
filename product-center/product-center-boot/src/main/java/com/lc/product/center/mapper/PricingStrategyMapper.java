package com.lc.product.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.product.center.domain.dto.PricingStrategyDTO;
import com.lc.product.center.domain.entity.PricingStrategyDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 定价策略表(product_center.pricing_strategy)Mapper接口
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Mapper
public interface PricingStrategyMapper extends BaseMapper<PricingStrategyDO> {

    /**
     * 根据条件查询定价策略列表
     *
     * @param queryDTO 查询条件
     * @return 策略列表
     */
    List<PricingStrategyDO> selectByCondition(@Param("query") PricingStrategyDTO queryDTO);

    /**
     * 根据策略编码查询
     *
     * @param tenantId     租户ID
     * @param strategyCode 策略编码
     * @return 策略信息
     */
    PricingStrategyDO selectByCode(@Param("tenantId") String tenantId, @Param("strategyCode") String strategyCode);

    /**
     * 查询有效的策略列表
     *
     * @param tenantId     租户ID
     * @param strategyType 策略类型(可选)
     * @return 策略列表
     */
    List<PricingStrategyDO> selectEffectiveStrategies(@Param("tenantId") String tenantId,
            @Param("strategyType") String strategyType);
}
