package com.lc.product.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.domain.dto.PricingStrategyDTO;
import com.lc.product.center.domain.dto.PricingStrategyParamDTO;
import com.lc.product.center.domain.entity.PricingStrategyDO;
import com.lc.product.center.domain.vo.PricingStrategyVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 定价策略表(product_center.pricing_strategy)表服务接口
 *
 * @author lucheng
 * @since 2026-01-31
 */
public interface PricingStrategyService extends IService<PricingStrategyDO> {

    /**
     * 分页查询定价策略
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PaginationResult<PricingStrategyVO> queryStrategyPage(PricingStrategyDTO queryDTO);

    /**
     * 查询策略列表
     *
     * @param queryDTO 查询条件
     * @return 策略列表
     */
    List<PricingStrategyVO> queryStrategyList(PricingStrategyDTO queryDTO);

    /**
     * 根据ID查询策略详情
     *
     * @param id 策略ID
     * @return 策略详情(含参数)
     */
    PricingStrategyVO getStrategyById(Long id);

    /**
     * 根据策略编码查询
     *
     * @param tenantId     租户ID
     * @param strategyCode 策略编码
     * @return 策略详情
     */
    PricingStrategyVO getStrategyByCode(String tenantId, String strategyCode);

    /**
     * 查询有效的策略列表
     *
     * @param tenantId     租户ID
     * @param strategyType 策略类型(可选)
     * @return 策略列表
     */
    List<PricingStrategyVO> getEffectiveStrategies(String tenantId, String strategyType);

    /**
     * 创建定价策略
     *
     * @param strategyDTO 策略信息
     * @return 创建的策略
     */
    @Transactional(rollbackFor = Exception.class)
    PricingStrategyVO createStrategy(PricingStrategyDTO strategyDTO);

    /**
     * 更新定价策略
     *
     * @param strategyDTO 策略信息
     * @return 更新的策略
     */
    @Transactional(rollbackFor = Exception.class)
    PricingStrategyVO updateStrategy(PricingStrategyDTO strategyDTO);

    /**
     * 删除定价策略
     *
     * @param id 策略ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    Boolean deleteStrategy(Long id);

    /**
     * 保存策略参数(阶梯配置)
     *
     * @param strategyId 策略ID
     * @param params     参数列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    Boolean saveStrategyParams(Long strategyId, List<PricingStrategyParamDTO> params);
}
