package com.lc.product.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.framework.core.mvc.BizException;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.constants.ProductDefaultConstants;
import com.lc.product.center.constants.ProductStatusEnum;
import com.lc.product.center.converter.PricingStrategyConverter;
import com.lc.product.center.converter.PricingStrategyParamConverter;
import com.lc.product.center.domain.dto.PricingStrategyDTO;
import com.lc.product.center.domain.dto.PricingStrategyParamDTO;
import com.lc.product.center.domain.entity.PricingStrategyDO;
import com.lc.product.center.domain.entity.PricingStrategyParamDO;
import com.lc.product.center.domain.vo.PricingStrategyParamVO;
import com.lc.product.center.domain.vo.PricingStrategyVO;
import com.lc.product.center.mapper.PricingStrategyMapper;
import com.lc.product.center.mapper.PricingStrategyParamMapper;
import com.lc.product.center.service.PricingStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 定价策略表(product_center.pricing_strategy)表服务实现类
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Service("pricingStrategyService")
public class PricingStrategyServiceImpl extends ServiceImpl<PricingStrategyMapper, PricingStrategyDO>
        implements PricingStrategyService {

    @Autowired
    private PricingStrategyParamMapper paramMapper;

    @Autowired
    private PricingStrategyConverter pricingStrategyConverter;

    @Autowired
    private PricingStrategyParamConverter pricingStrategyParamConverter;

    @Override
    public PaginationResult<PricingStrategyVO> queryStrategyPage(PricingStrategyDTO queryDTO) {
        Page<PricingStrategyDO> page = Page.of(queryDTO.getPageIndex(), queryDTO.getPageSize());

        LambdaQueryWrapper<PricingStrategyDO> queryWrapper = buildQueryWrapper(queryDTO);
        queryWrapper.orderByDesc(PricingStrategyDO::getPriority)
                .orderByDesc(PricingStrategyDO::getDtCreated);

        IPage<PricingStrategyDO> pageResult = this.page(page, queryWrapper);
        queryDTO.setTotal(pageResult.getTotal());

        List<PricingStrategyVO> voList = pricingStrategyConverter.convertDO2VOList(pageResult.getRecords());
        return PaginationResult.success(voList, queryDTO);
    }

    @Override
    public List<PricingStrategyVO> queryStrategyList(PricingStrategyDTO queryDTO) {
        List<PricingStrategyDO> list = baseMapper.selectByCondition(queryDTO);
        return pricingStrategyConverter.convertDO2VOList(list);
    }

    @Override
    public PricingStrategyVO getStrategyById(Long id) {
        PricingStrategyDO entity = this.getById(id);
        if (entity == null) {
            return null;
        }
        PricingStrategyVO vo = pricingStrategyConverter.convertDO2VO(entity);
        // 加载阶梯参数
        List<PricingStrategyParamDO> params = paramMapper.selectByStrategyCode(
                entity.getTenantId(), entity.getStrategyCode());
        vo.setStrategyParams(convertParamsToVO(params));
        return vo;
    }

    @Override
    public PricingStrategyVO getStrategyByCode(String tenantId, String strategyCode) {
        String tenant = StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT;
        PricingStrategyDO entity = baseMapper.selectByCode(tenant, strategyCode);
        if (entity == null) {
            return null;
        }
        PricingStrategyVO vo = pricingStrategyConverter.convertDO2VO(entity);
        List<PricingStrategyParamDO> params = paramMapper.selectByStrategyCode(tenant, strategyCode);
        vo.setStrategyParams(convertParamsToVO(params));
        return vo;
    }

    @Override
    public List<PricingStrategyVO> getEffectiveStrategies(String tenantId, String strategyType) {
        String tenant = StringUtils.hasText(tenantId) ? tenantId : ProductDefaultConstants.DEFAULT_TENANT;
        List<PricingStrategyDO> list = baseMapper.selectEffectiveStrategies(tenant, strategyType);
        return pricingStrategyConverter.convertDO2VOList(list);
    }

    @Override
    public PricingStrategyVO createStrategy(PricingStrategyDTO strategyDTO) {
        String tenantId = StringUtils.hasText(strategyDTO.getTenantId())
                ? strategyDTO.getTenantId()
                : ProductDefaultConstants.DEFAULT_TENANT;

        // 检查编码是否已存在
        PricingStrategyDO existing = baseMapper.selectByCode(tenantId, strategyDTO.getStrategyCode());
        if (existing != null) {
            throw BizException.exp("策略编码已存在");
        }

        PricingStrategyDO entity = pricingStrategyConverter.convertDTO2DO(strategyDTO);
        entity.setTenantId(tenantId);
        if (entity.getStatus() == null) {
            entity.setStatus(ProductStatusEnum.ACTIVE.getCode());
        }
        if (entity.getPriority() == null) {
            entity.setPriority(0);
        }

        this.save(entity);
        return pricingStrategyConverter.convertDO2VO(entity);
    }

    @Override
    public PricingStrategyVO updateStrategy(PricingStrategyDTO strategyDTO) {
        PricingStrategyDO existing = this.getById(strategyDTO.getId());
        if (existing == null) {
            throw BizException.exp("策略不存在");
        }

        PricingStrategyDO updateEntity = pricingStrategyConverter.convertDTO2DO(strategyDTO);
        updateEntity.setId(strategyDTO.getId());
        this.updateById(updateEntity);

        return getStrategyById(strategyDTO.getId());
    }

    @Override
    public Boolean deleteStrategy(Long id) {
        PricingStrategyDO entity = this.getById(id);
        if (entity == null) {
            throw BizException.exp("策略不存在");
        }
        // 删除关联的参数
        paramMapper.deleteByStrategyCode(entity.getTenantId(), entity.getStrategyCode());
        return this.removeById(id);
    }

    @Override
    public Boolean saveStrategyParams(Long strategyId, List<PricingStrategyParamDTO> params) {
        // 获取策略信息
        PricingStrategyDO entity = this.getById(strategyId);
        if (entity == null) {
            throw BizException.exp("策略不存在");
        }

        String tenantId = entity.getTenantId();
        String strategyCode = entity.getStrategyCode();

        // 先删除原有参数
        paramMapper.deleteByStrategyCode(tenantId, strategyCode);

        if (CollectionUtils.isEmpty(params)) {
            return true;
        }

        // 构建参数实体
        List<PricingStrategyParamDO> paramDOs = new ArrayList<>();
        for (int i = 0; i < params.size(); i++) {
            PricingStrategyParamDTO dto = params.get(i);
            dto.setTenantId(tenantId);
            dto.setStrategyCode(strategyCode);
            dto.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : i);
            paramDOs.add(pricingStrategyParamConverter.convertDTO2DO(dto));
        }
        paramMapper.batchInsert(paramDOs);
        return true;
    }

    // ==================== 私有方法 ====================

    private LambdaQueryWrapper<PricingStrategyDO> buildQueryWrapper(PricingStrategyDTO queryDTO) {
        LambdaQueryWrapper<PricingStrategyDO> queryWrapper = new LambdaQueryWrapper<>();
        if (queryDTO != null) {
            if (StringUtils.hasText(queryDTO.getTenantId())) {
                queryWrapper.eq(PricingStrategyDO::getTenantId, queryDTO.getTenantId());
            }
            if (StringUtils.hasText(queryDTO.getStrategyCode())) {
                queryWrapper.like(PricingStrategyDO::getStrategyCode, queryDTO.getStrategyCode());
            }
            if (StringUtils.hasText(queryDTO.getStrategyName())) {
                queryWrapper.like(PricingStrategyDO::getStrategyName, queryDTO.getStrategyName());
            }
            if (StringUtils.hasText(queryDTO.getStrategyType())) {
                queryWrapper.eq(PricingStrategyDO::getStrategyType, queryDTO.getStrategyType());
            }
            if (StringUtils.hasText(queryDTO.getApplyScope())) {
                queryWrapper.eq(PricingStrategyDO::getApplyScope, queryDTO.getApplyScope());
            }
            if (StringUtils.hasText(queryDTO.getStatus())) {
                queryWrapper.eq(PricingStrategyDO::getStatus, queryDTO.getStatus());
            }
        }
        return queryWrapper;
    }

    private List<PricingStrategyParamVO> convertParamsToVO(List<PricingStrategyParamDO> params) {
        if (CollectionUtils.isEmpty(params)) {
            return new ArrayList<>();
        }
        return params.stream().map(param -> {
            String rangeDesc = param.getRangeStart() + " - " +
                    (param.getRangeEnd() != null ? param.getRangeEnd() : "∞");
            return PricingStrategyParamVO.builder()
                    .id(param.getId())
                    .tenantId(param.getTenantId())
                    .strategyCode(param.getStrategyCode())
                    .paramType(param.getParamType())
                    .rangeStart(param.getRangeStart())
                    .rangeEnd(param.getRangeEnd())
                    .rangeDesc(rangeDesc)
                    .value(param.getValue())
                    .sortOrder(param.getSortOrder())
                    .build();
        }).collect(Collectors.toList());
    }
}
