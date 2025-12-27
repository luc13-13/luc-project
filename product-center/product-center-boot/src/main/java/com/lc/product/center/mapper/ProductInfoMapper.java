package com.lc.product.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.product.center.domain.dto.ProductInfoDTO;
import com.lc.product.center.domain.entity.ProductInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品信息表(product_center.product_info)表数据库访问层
 * 
 * <p>
 * 采用单一动态查询入口，通过 DTO 参数控制查询条件
 * </p>
 *
 * @author lucheng
 * @since 2025-08-31
 */
@Mapper
public interface ProductInfoMapper extends BaseMapper<ProductInfoDO> {

        /**
         * 动态条件查询（唯一的自定义查询入口）
         *
         * @param queryDTO 查询条件
         * @return 产品列表
         */
        List<ProductInfoDO> selectByCondition(ProductInfoDTO queryDTO);

        /**
         * 查询产品编码列表（去重）
         *
         * @param tenantId 租户ID
         * @return 产品编码列表
         */
        List<String> findDistinctProductCodes(@Param("tenantId") String tenantId);

        /**
         * 根据产品编码查询规格族列表（去重）
         *
         * @param tenantId    租户ID
         * @param productCode 产品编码
         * @return 规格族编码列表
         */
        List<String> findDistinctSubProductCodes(@Param("tenantId") String tenantId,
                        @Param("productCode") String productCode);

        /**
         * 根据产品编码和规格族查询计费项列表（去重）
         *
         * @param tenantId       租户ID
         * @param productCode    产品编码
         * @param subProductCode 规格族编码
         * @return 计费项编码列表
         */
        List<String> findDistinctBillingItemCodes(@Param("tenantId") String tenantId,
                        @Param("productCode") String productCode,
                        @Param("subProductCode") String subProductCode);
}
