package com.lc.product.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.product.center.domain.entity.PricingStrategyParamDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 定价策略参数细目表(product_center.pricing_strategy_param)Mapper接口
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Mapper
public interface PricingStrategyParamMapper extends BaseMapper<PricingStrategyParamDO> {

    /**
     * 根据策略编码查询参数列表
     *
     * @param tenantId     租户ID
     * @param strategyCode 策略编码
     * @return 参数列表
     */
    List<PricingStrategyParamDO> selectByStrategyCode(@Param("tenantId") String tenantId,
            @Param("strategyCode") String strategyCode);

    /**
     * 根据定价编码和版本查询参数列表
     *
     * @param tenantId        租户ID
     * @param pricingCode     定价编码
     * @param pricingRevision 定价版本
     * @return 参数列表
     */
    List<PricingStrategyParamDO> selectByPricingCode(@Param("tenantId") String tenantId,
            @Param("pricingCode") String pricingCode,
            @Param("pricingRevision") String pricingRevision);

    /**
     * 删除策略关联的参数
     *
     * @param tenantId     租户ID
     * @param strategyCode 策略编码
     * @return 删除数量
     */
    int deleteByStrategyCode(@Param("tenantId") String tenantId,
            @Param("strategyCode") String strategyCode);

    /**
     * 批量插入参数
     *
     * @param params 参数列表
     * @return 插入数量
     */
    int batchInsert(@Param("list") List<PricingStrategyParamDO> params);
}
