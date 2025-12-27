package com.lc.product.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.domain.dto.SkuItemCombinationDTO;
import com.lc.product.center.domain.entity.SkuItemCombinationDO;
import com.lc.product.center.domain.vo.SkuItemCombinationVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SKU计费项组合表(product_center.sku_item_combination)表服务接口
 *
 * @author lucheng
 * @since 2025-12-27
 */
public interface SkuItemCombinationService extends IService<SkuItemCombinationDO> {

    PaginationResult<SkuItemCombinationVO> queryCombinationPage(SkuItemCombinationDTO queryDTO);

    List<SkuItemCombinationVO> queryCombinationList(SkuItemCombinationDTO queryDTO);

    SkuItemCombinationVO getCombinationById(Long id);

    List<SkuItemCombinationVO> getCombinationsBySkuCode(String tenantId, String skuCode);

    @Transactional(rollbackFor = Exception.class)
    SkuItemCombinationVO createCombination(SkuItemCombinationDTO combinationDTO);

    @Transactional(rollbackFor = Exception.class)
    List<SkuItemCombinationVO> batchCreateCombinations(List<SkuItemCombinationDTO> combinationDTOs);

    @Transactional(rollbackFor = Exception.class)
    SkuItemCombinationVO updateCombination(SkuItemCombinationDTO combinationDTO);

    @Transactional(rollbackFor = Exception.class)
    Boolean deleteCombination(Long id);

    @Transactional(rollbackFor = Exception.class)
    Boolean deleteCombinationsBySkuCode(String tenantId, String skuCode);
}
