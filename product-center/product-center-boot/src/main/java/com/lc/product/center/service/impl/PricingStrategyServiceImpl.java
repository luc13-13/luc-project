package com.lc.product.center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.product.center.mapper.PricingStrategyMapper;
import com.lc.product.center.domain.entity.PricingStrategyDO;
import com.lc.product.center.service.PricingStrategyService;
import org.springframework.stereotype.Service;

/**
 * 定价策略表(product_center.pricing_strategy)表服务实现类
 *
 * @author lucheng
 * @since 2026-02-06
 */
@Service("pricingStrategyService")
public class PricingStrategyServiceImpl extends ServiceImpl<PricingStrategyMapper, PricingStrategyDO> implements PricingStrategyService {

}

