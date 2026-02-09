package com.lc.product.center.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.framework.core.mvc.BizException;
import com.lc.framework.core.page.PaginationResult;
import com.lc.product.center.constants.ProductDefaultConstants;
import com.lc.product.center.constants.ProductStatusEnum;
import com.lc.product.center.converter.ProductSkuConverter;
import com.lc.product.center.domain.bo.ProductSkuBO;
import com.lc.product.center.domain.dto.ProductSkuDTO;
import com.lc.product.center.domain.entity.ProductSkuDO;
import com.lc.product.center.domain.vo.ProductSkuDetailsVO;
import com.lc.product.center.domain.vo.ProductSkuVO;
import com.lc.product.center.mapper.ProductSkuMapper;
import com.lc.product.center.service.ProductSkuService;
import com.lc.framework.core.utils.RevisionUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 产品SKU表(product_center.product_sku)表服务实现类
 *
 * @author lucheng
 * @since 2026-02-06
 */
@Service("productSkuService")
@AllArgsConstructor
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSkuDO>
        implements ProductSkuService {

    private final ProductSkuConverter productSkuConverter;

    @Override
    public List<ProductSkuVO> listByCondition(ProductSkuDTO queryDTO) {
        setDefaultTenantIfEmpty(queryDTO);
        List<ProductSkuDO> list = baseMapper.selectByCondition(queryDTO);
        return productSkuConverter.convertDO2VO(list);
    }

    @Override
    public PaginationResult<ProductSkuVO> pageByCondition(ProductSkuDTO queryDTO) {
        setDefaultTenantIfEmpty(queryDTO);

        // 使用 MyBatis-Plus 原生分页
        IPage<ProductSkuDO> page = new Page<>(queryDTO.getPageIndex(), queryDTO.getPageSize());
        IPage<ProductSkuDO> result = baseMapper.selectPageByCondition(page, queryDTO);

        List<ProductSkuVO> voList = productSkuConverter.convertDO2VO(result.getRecords());

        queryDTO.setTotal(result.getTotal());
        return PaginationResult.success(voList, queryDTO);
    }

    @Override
    public ProductSkuDetailsVO getDetail(ProductSkuDTO queryDTO) {
        setDefaultTenantIfEmpty(queryDTO);

        // 使用多表聚合查询获取 BO
        ProductSkuBO bo = baseMapper.selectDetailBySkuCode(
                queryDTO.getTenantId(),
                queryDTO.getSkuCode(),
                queryDTO.getRevision());

        if (bo == null || bo.getProductSkuDO() == null) {
            throw BizException.exp("产品SKU不存在: " + queryDTO.getSkuCode());
        }

        // BO 转换为 DetailsVO
        return productSkuConverter.convertBO2DetailsVO(bo);
    }

    @Override
    public ProductSkuVO createSku(ProductSkuDTO dto) {
        String tenantId = StringUtils.hasText(dto.getTenantId())
                ? dto.getTenantId()
                : ProductDefaultConstants.DEFAULT_TENANT;

        // 生成版本号（带 V 前缀）
        String revision = RevisionUtils.generateTimestampRevision("SKU-");

        // 检查 SKU 编码 + 版本号是否已存在
        ProductSkuDTO checkDTO = ProductSkuDTO.builder()
                .tenantId(tenantId)
                .skuCode(dto.getSkuCode())
                .revision(revision)
                .build();
        List<ProductSkuDO> existing = baseMapper.selectByCondition(checkDTO);
        if (!CollectionUtils.isEmpty(existing)) {
            throw BizException.exp("SKU编码+版本号已存在: " + dto.getSkuCode() + "-" + revision);
        }

        // 转换并设置默认值
        ProductSkuDO entity = productSkuConverter.convertDTO2DOForCreate(dto, tenantId);
        entity.setRevision(revision);
        entity.setIsCurrent(true);
        entity.setStatus(ProductStatusEnum.DRAFT.getCode());

        // 如果存在旧版本，将其标记为非当前版本
        ProductSkuDTO oldDTO = ProductSkuDTO.builder()
                .tenantId(tenantId)
                .skuCode(dto.getSkuCode())
                .isCurrent(true)
                .build();
        List<ProductSkuDO> oldVersions = baseMapper.selectByCondition(oldDTO);
        for (ProductSkuDO oldVersion : oldVersions) {
            oldVersion.setIsCurrent(false);
            this.updateById(oldVersion);
        }

        this.save(entity);
        return productSkuConverter.convertDO2VO(entity);
    }

    @Override
    public ProductSkuVO updateSku(ProductSkuDTO dto) {
        ProductSkuDO existing = this.getById(dto.getId());
        if (existing == null) {
            throw BizException.exp("产品SKU不存在");
        }

        ProductSkuDO updateDO = productSkuConverter.convertDTO2DO(dto);
        updateDO.setId(existing.getId());

        this.updateById(updateDO);
        return productSkuConverter.convertDO2VO(this.getById(dto.getId()));
    }

    @Override
    public Boolean deleteSku(Long id) {
        ProductSkuDO existing = this.getById(id);
        if (existing == null) {
            throw BizException.exp("产品SKU不存在");
        }
        return this.removeById(id);
    }

    private void setDefaultTenantIfEmpty(ProductSkuDTO dto) {
        if (!StringUtils.hasText(dto.getTenantId())) {
            dto.setTenantId(ProductDefaultConstants.DEFAULT_TENANT);
        }
    }
}
