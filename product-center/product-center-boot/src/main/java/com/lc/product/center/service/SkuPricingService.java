package com.lc.product.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.domain.dto.SkuPricingDTO;
import com.lc.product.center.domain.entity.SkuPricingDO;
import com.lc.product.center.domain.vo.SkuPricingVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 定价模板表(product_center.sku_pricing)表服务接口
 *
 * @author lucheng
 * @since 2026-01-31
 */
public interface SkuPricingService extends IService<SkuPricingDO> {

    PaginationResult<SkuPricingVO> queryPricingPage(SkuPricingDTO queryDTO);

    List<SkuPricingVO> queryPricingList(SkuPricingDTO queryDTO);

    SkuPricingVO getPricingById(Long id);

    /**
     * 根据定价编码查询定价模板列表
     *
     * @param tenantId    租户ID
     * @param pricingCode 定价编码
     * @return 定价模板列表
     */
    List<SkuPricingVO> getPricingsByCode(String tenantId, String pricingCode);

    /**
     * 根据定价编码和计费周期查询
     *
     * @param tenantId     租户ID
     * @param pricingCode  定价编码
     * @param billingCycle 计费周期
     * @return 定价模板
     */
    SkuPricingVO getPricingByCodeAndCycle(String tenantId, String pricingCode, String billingCycle);

    /**
     * 获取有效的定价模板
     *
     * @param tenantId     租户ID
     * @param pricingCode  定价编码
     * @param billingCycle 计费周期
     * @return 有效的定价模板
     */
    SkuPricingVO getEffectivePricing(String tenantId, String pricingCode, String billingCycle);

    @Transactional(rollbackFor = Exception.class)
    SkuPricingVO createPricing(SkuPricingDTO pricingDTO);

    @Transactional(rollbackFor = Exception.class)
    List<SkuPricingVO> batchCreatePricings(List<SkuPricingDTO> pricingDTOs);

    @Transactional(rollbackFor = Exception.class)
    SkuPricingVO updatePricing(SkuPricingDTO pricingDTO);

    @Transactional(rollbackFor = Exception.class)
    Boolean deletePricing(Long id);

    /**
     * 删除指定定价编码的所有定价模板
     *
     * @param tenantId    租户ID
     * @param pricingCode 定价编码
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    Boolean deletePricingsByCode(String tenantId, String pricingCode);
}
