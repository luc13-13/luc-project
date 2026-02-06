package com.lc.product.center.converter;

import com.lc.product.center.domain.bo.ProductSkuBO;
import com.lc.product.center.domain.dto.ProductSkuDTO;
import com.lc.product.center.domain.entity.ProductSkuDO;
import com.lc.product.center.domain.vo.ProductSkuDetailsVO;
import com.lc.product.center.domain.vo.ProductSkuVO;

import java.util.List;

/**
 * 产品SKU表对象转换接口
 *
 * @author lucheng
 * @since 2025-12-26
 */
public interface ProductSkuConverter {

    // ==================== 简单转换（不经过BO） ====================

    /**
     * 转换DTO为DO（简单场景：创建/更新）
     *
     * @param dto 数据传输对象
     * @return 数据库对象
     */
    ProductSkuDO convertDTO2DO(ProductSkuDTO dto);

    /**
     * 转换DTO为DO并设置默认值（用于创建）
     *
     * @param dto      数据传输对象
     * @param tenantId 租户ID
     * @return 数据库对象（已设置默认值）
     */
    ProductSkuDO convertDTO2DOForCreate(ProductSkuDTO dto, String tenantId);

    /**
     * 转换DO为概览对象（简单场景）
     *
     * @param entity 数据库对象
     * @return 概览对象
     */
    ProductSkuVO convertDO2VO(ProductSkuDO entity);

    /**
     * 批量转换DO为概览对象（简单场景）
     *
     * @param entities 数据库对象列表
     * @return 概览对象列表
     */
    List<ProductSkuVO> convertDO2VO(List<ProductSkuDO> entities);

    /**
     * 转换DO为详情对象
     * @param entity 数据库对象
     * @return 详情对象
     */
    ProductSkuDetailsVO convertDO2DetailsVO(ProductSkuDO entity);

    // ==================== 复杂转换（经过BO，用于业务逻辑） ====================

    /**
     * 转换为详情对象，包含关联定价模板和定价策略
     * @param bo 业务对象封装
     * @return SKU详情
     */
    ProductSkuDetailsVO convertBO2DetailsVO(ProductSkuBO bo);
}
