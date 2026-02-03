package com.lc.product.center.converter;

import com.lc.product.center.domain.dto.PricingStrategyDTO;
import com.lc.product.center.domain.entity.PricingStrategyDO;
import com.lc.product.center.domain.vo.PricingStrategyVO;

import java.util.List;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2/2/26 09:35
 * @version : 1.0
 */
public interface PricingStrategyConverter {

    List<PricingStrategyVO> convertDO2VOList(List<PricingStrategyDO> entityList);

    PricingStrategyDO convertDTO2DO(PricingStrategyDTO dto);

    PricingStrategyVO convertDO2VO(PricingStrategyDO entity);
}
