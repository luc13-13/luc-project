package com.lc.product.center.converter;

import com.lc.product.center.domain.dto.ProductInfoDTO;
import com.lc.product.center.domain.entity.ProductInfoDO;
import com.lc.product.center.domain.vo.ProductInfoVO;

import java.util.List;

/**
 * 产品信息表对象转换接口
 *
 * @author lucheng
 * @since 2025-12-26
 */
public interface ProductInfoConverter {

    // ==================== 简单转换（不经过BO） ====================

    /**
     * 转换DTO为DO（简单场景：创建/更新）
     *
     * @param dto 数据传输对象
     * @return 数据库对象
     */
    ProductInfoDO convertDTO2DO(ProductInfoDTO dto);

    /**
     * 转换DTO为DO（创建场景：设置默认值）
     *
     * @param dto      数据传输对象
     * @param tenantId 租户ID
     * @return 数据库对象
     */
    ProductInfoDO convertDTO2DOForCreate(ProductInfoDTO dto, String tenantId);

    /**
     * 转换DO为VO（简单场景：查询展示）
     *
     * @param entity 数据库对象
     * @return 视图对象
     */
    ProductInfoVO convertDO2VO(ProductInfoDO entity);

    /**
     * 批量转换DO为VO（简单场景）
     *
     * @param entities 数据库对象列表
     * @return 视图对象列表
     */
    List<ProductInfoVO> convertDO2VO(List<ProductInfoDO> entities);
}
