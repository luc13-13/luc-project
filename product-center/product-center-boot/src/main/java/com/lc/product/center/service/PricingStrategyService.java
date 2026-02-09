package com.lc.product.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.domain.dto.PricingStrategyDTO;
import com.lc.product.center.domain.entity.PricingStrategyDO;
import com.lc.product.center.domain.vo.PricingStrategyVO;

import java.util.List;

/**
 * 定价策略表(product_center.pricing_strategy)表服务接口
 *
 * @author lucheng
 * @since 2026-02-06
 */
public interface PricingStrategyService extends IService<PricingStrategyDO> {

    /**
     * 列表查询
     *
     * @param queryDTO 查询条件
     * @return 定价策略列表
     */
    List<PricingStrategyVO> listByCondition(PricingStrategyDTO queryDTO);

    /**
     * 分页查询
     *
     * @param queryDTO 查询条件（含分页参数）
     * @return 分页结果
     */
    PaginationResult<PricingStrategyVO> pageByCondition(PricingStrategyDTO queryDTO);

    /**
     * 根据ID查询详情
     *
     * @param id 主键ID
     * @return 定价策略VO
     */
    PricingStrategyVO getDetailById(Long id);

    /**
     * 创建定价策略
     *
     * @param dto 定价策略DTO
     * @return 创建后的定价策略VO
     */
    PricingStrategyVO createStrategy(PricingStrategyDTO dto);

    /**
     * 更新定价策略
     *
     * @param dto 定价策略DTO
     * @return 更新后的定价策略VO
     */
    PricingStrategyVO updateStrategy(PricingStrategyDTO dto);

    /**
     * 删除定价策略
     *
     * @param id 主键ID
     * @return 是否成功
     */
    Boolean deleteStrategy(Long id);
}
