package com.lc.product.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.domain.dto.SkuPricingDTO;
import com.lc.product.center.domain.entity.SkuPricingDO;
import com.lc.product.center.domain.vo.SkuPricingVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SKU定价表(product_center.sku_pricing)表服务接口
 *
 * @author lucheng
 * @since 2025-12-27
 */
public interface SkuPricingService extends IService<SkuPricingDO> {

    PaginationResult<SkuPricingVO> queryPricingPage(SkuPricingDTO queryDTO);

    List<SkuPricingVO> queryPricingList(SkuPricingDTO queryDTO);

    SkuPricingVO getPricingById(Long id);

    List<SkuPricingVO> getPricingsBySkuCode(String tenantId, String skuCode);

    SkuPricingVO getPricingBySkuCodeAndCycle(String tenantId, String skuCode, String billingPeriod);

    SkuPricingVO getEffectivePricing(String tenantId, String skuCode, String billingPeriod);

    @Transactional(rollbackFor = Exception.class)
    SkuPricingVO createPricing(SkuPricingDTO pricingDTO);

    @Transactional(rollbackFor = Exception.class)
    List<SkuPricingVO> batchCreatePricings(List<SkuPricingDTO> pricingDTOs);

    @Transactional(rollbackFor = Exception.class)
    SkuPricingVO updatePricing(SkuPricingDTO pricingDTO);

    @Transactional(rollbackFor = Exception.class)
    Boolean deletePricing(Long id);

    @Transactional(rollbackFor = Exception.class)
    Boolean deletePricingsBySkuCode(String tenantId, String skuCode);
}
