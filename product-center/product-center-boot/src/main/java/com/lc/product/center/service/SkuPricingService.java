package com.lc.product.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.domain.dto.SkuPricingDTO;
import com.lc.product.center.domain.entity.SkuPricingDO;
import com.lc.product.center.domain.vo.SkuPricingVO;

import java.util.List;

/**
 * 定价模板表(product_center.sku_pricing)表服务接口
 *
 * @author lucheng
 * @since 2026-02-06
 */
public interface SkuPricingService extends IService<SkuPricingDO> {

    /**
     * 列表查询
     *
     * @param queryDTO 查询条件
     * @return 定价模板列表
     */
    List<SkuPricingVO> listByCondition(SkuPricingDTO queryDTO);

    /**
     * 分页查询
     *
     * @param queryDTO 查询条件（含分页参数）
     * @return 分页结果
     */
    PaginationResult<SkuPricingVO> pageByCondition(SkuPricingDTO queryDTO);

    /**
     * 根据ID查询详情
     *
     * @param id 主键ID
     * @return 定价模板VO
     */
    SkuPricingVO getDetailById(Long id);

    /**
     * 创建定价模板
     *
     * @param dto 定价模板DTO
     * @return 创建后的定价模板VO
     */
    SkuPricingVO createPricing(SkuPricingDTO dto);

    /**
     * 更新定价模板
     *
     * @param dto 定价模板DTO
     * @return 更新后的定价模板VO
     */
    SkuPricingVO updatePricing(SkuPricingDTO dto);

    /**
     * 删除定价模板
     *
     * @param id 主键ID
     * @return 是否成功
     */
    Boolean deletePricing(Long id);
}
