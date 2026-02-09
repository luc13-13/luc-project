package com.lc.product.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lc.product.center.domain.dto.ProductInfoDTO;
import com.lc.product.center.domain.entity.ProductInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品信息表(product_center.product_info)表数据库访问层
 * <p>
 * 采用单一动态查询入口，通过 DTO 参数控制查询条件
 * </p>
 *
 * @author lucheng
 * @since 2026-02-06
 */
@Mapper
public interface ProductInfoMapper extends BaseMapper<ProductInfoDO> {

        /**
         * 动态条件查询（列表查询）
         *
         * @param queryDTO 查询条件
         * @return 产品信息列表
         */
        List<ProductInfoDO> selectByCondition(@Param("dto") ProductInfoDTO queryDTO);

        /**
         * 动态条件分页查询（使用 MyBatis-Plus IPage）
         *
         * @param page     分页对象
         * @param queryDTO 查询条件
         * @return 分页结果
         */
        IPage<ProductInfoDO> selectPageByCondition(IPage<ProductInfoDO> page, @Param("dto") ProductInfoDTO queryDTO);
}
