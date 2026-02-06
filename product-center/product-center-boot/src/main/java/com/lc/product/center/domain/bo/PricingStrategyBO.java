package com.lc.product.center.domain.bo;

import com.lc.product.center.domain.entity.PricingStrategyDO;
import com.lc.product.center.domain.entity.PricingStrategyParamDO;
import lombok.Data;

import java.util.List;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 6/2/26 15:11
 * @version : 1.0
 */
@Data
public class PricingStrategyBO {
    /**
     * 策略信息
     */
    private PricingStrategyDO pricingStrategyDO;

    /**
     * 策略参数
     */
    private List<PricingStrategyParamDO>  pricingStrategyParamDOList;
}
