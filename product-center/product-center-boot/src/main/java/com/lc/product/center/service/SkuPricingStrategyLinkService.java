package com.lc.product.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.product.center.domain.dto.SkuPricingStrategyLinkDTO;
import com.lc.product.center.domain.entity.SkuPricingStrategyLinkDO;
import com.lc.product.center.domain.vo.SkuPricingStrategyLinkVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SKU与策略关联表(product_center.sku_pricing_strategy_link)服务接口
 *
 * @author lucheng
 * @since 2026-02-05
 */
public interface SkuPricingStrategyLinkService extends IService<SkuPricingStrategyLinkDO> {

    /**
     * 创建SKU-策略关联
     *
     * @param dto 关联信息
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    boolean createLink(SkuPricingStrategyLinkDTO dto);

    /**
     * 更新SKU-策略关联
     *
     * @param dto 关联信息
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateLink(SkuPricingStrategyLinkDTO dto);

    /**
     * 删除SKU-策略关联
     *
     * @param id 关联ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    boolean deleteLink(Long id);

    /**
     * 根据SKU编码和版本查询关联策略
     *
     * @param skuCode     SKU编码
     * @param skuRevision SKU版本
     * @return 关联策略列表
     */
    List<SkuPricingStrategyLinkVO> listBySkuCodeAndRevision(String skuCode, String skuRevision);

    /**
     * 根据策略编码查询关联的SKU
     *
     * @param strategyCode 策略编码
     * @return 关联SKU列表
     */
    List<SkuPricingStrategyLinkVO> listByStrategyCode(String strategyCode);

    /**
     * 批量创建SKU-策略关联
     *
     * @param dtoList 关联信息列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    boolean batchCreateLinks(List<SkuPricingStrategyLinkDTO> dtoList);
}
