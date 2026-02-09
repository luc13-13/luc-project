package com.lc.product.center.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.framework.core.mvc.BizException;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.constants.ProductDefaultConstants;
import com.lc.product.center.constants.ProductStatusEnum;
import com.lc.product.center.converter.PricingStrategyConverter;
import com.lc.product.center.domain.dto.PricingStrategyDTO;
import com.lc.product.center.domain.entity.PricingStrategyDO;
import com.lc.product.center.domain.vo.PricingStrategyVO;
import com.lc.product.center.mapper.PricingStrategyMapper;
import com.lc.product.center.service.PricingStrategyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 定价策略表(product_center.pricing_strategy)表服务实现类
 *
 * @author lucheng
 * @since 2026-02-06
 */
@Service("pricingStrategyService")
@AllArgsConstructor
public class PricingStrategyServiceImpl extends ServiceImpl<PricingStrategyMapper, PricingStrategyDO>
        implements PricingStrategyService {

    private final PricingStrategyConverter pricingStrategyConverter;

    @Override
    public List<PricingStrategyVO> listByCondition(PricingStrategyDTO queryDTO) {
        setDefaultTenantIfEmpty(queryDTO);
        List<PricingStrategyDO> list = baseMapper.selectByCondition(queryDTO);
        return pricingStrategyConverter.convertDO2VOList(list);
    }

    @Override
    public PaginationResult<PricingStrategyVO> pageByCondition(PricingStrategyDTO queryDTO) {
        setDefaultTenantIfEmpty(queryDTO);

        // 使用 MyBatis-Plus 原生分页
        IPage<PricingStrategyDO> page = new Page<>(queryDTO.getPageIndex(), queryDTO.getPageSize());
        IPage<PricingStrategyDO> result = baseMapper.selectPageByCondition(page, queryDTO);

        List<PricingStrategyVO> voList = pricingStrategyConverter.convertDO2VOList(result.getRecords());

        queryDTO.setTotal(result.getTotal());
        return PaginationResult.success(voList, queryDTO);
    }

    @Override
    public PricingStrategyVO getDetailById(Long id) {
        PricingStrategyDO entity = this.getById(id);
        if (entity == null) {
            return null;
        }
        return pricingStrategyConverter.convertDO2VO(entity);
    }

    @Override
    public PricingStrategyVO createStrategy(PricingStrategyDTO dto) {
        String tenantId = StringUtils.hasText(dto.getTenantId())
                ? dto.getTenantId()
                : ProductDefaultConstants.DEFAULT_TENANT;

        // 检查策略编码是否已存在
        PricingStrategyDTO checkDTO = PricingStrategyDTO.builder()
                .tenantId(tenantId)
                .strategyCode(dto.getStrategyCode())
                .build();
        List<PricingStrategyDO> existing = baseMapper.selectByCondition(checkDTO);
        if (!CollectionUtils.isEmpty(existing)) {
            throw BizException.exp("策略编码已存在: " + dto.getStrategyCode());
        }

        // 转换并设置默认值
        PricingStrategyDO entity = pricingStrategyConverter.convertDTO2DO(dto);
        entity.setTenantId(tenantId);

        if (!StringUtils.hasText(entity.getStatus())) {
            entity.setStatus(ProductStatusEnum.ACTIVE.getCode());
        }
        if (entity.getPriority() == null) {
            entity.setPriority(ProductDefaultConstants.DEFAULT_PRIORITY);
        }

        this.save(entity);
        return pricingStrategyConverter.convertDO2VO(entity);
    }

    @Override
    public PricingStrategyVO updateStrategy(PricingStrategyDTO dto) {
        PricingStrategyDO existing = this.getById(dto.getId());
        if (existing == null) {
            throw BizException.exp("定价策略不存在");
        }

        PricingStrategyDO updateDO = pricingStrategyConverter.convertDTO2DO(dto);
        updateDO.setId(existing.getId());

        this.updateById(updateDO);
        return pricingStrategyConverter.convertDO2VO(this.getById(dto.getId()));
    }

    @Override
    public Boolean deleteStrategy(Long id) {
        PricingStrategyDO existing = this.getById(id);
        if (existing == null) {
            throw BizException.exp("定价策略不存在");
        }
        return this.removeById(id);
    }

    private void setDefaultTenantIfEmpty(PricingStrategyDTO dto) {
        if (!StringUtils.hasText(dto.getTenantId())) {
            dto.setTenantId(ProductDefaultConstants.DEFAULT_TENANT);
        }
    }
}
