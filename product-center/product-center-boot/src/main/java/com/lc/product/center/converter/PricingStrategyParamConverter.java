package com.lc.product.center.converter;

import com.lc.product.center.domain.dto.PricingStrategyParamDTO;
import com.lc.product.center.domain.entity.PricingStrategyParamDO;
import com.lc.product.center.domain.vo.PricingStrategyParamVO;

import java.util.List;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2/2/26 09:36
 * @version : 1.0
 */
public interface PricingStrategyParamConverter {

    List<PricingStrategyParamVO> convertDO2VOList(List<PricingStrategyParamDO> params);



    List<PricingStrategyParamDO> convertDTO2DOList(List<PricingStrategyParamDTO> dtoList);

    PricingStrategyParamDO convertDTO2DO(PricingStrategyParamDTO dto);
}
