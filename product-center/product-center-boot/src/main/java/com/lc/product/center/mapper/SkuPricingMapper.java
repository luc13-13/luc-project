package com.lc.product.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lc.product.center.domain.dto.SkuPricingDTO;
import com.lc.product.center.domain.entity.SkuPricingDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 定价模板表(product_center.sku_pricing)表数据库访问层
 * <p>
 * 采用单一动态查询入口，通过 DTO 参数控制查询条件
 * </p>
 *
 * @author lucheng
 * @since 2026-02-06
 */
@Mapper
public interface SkuPricingMapper extends BaseMapper<SkuPricingDO> {

    /**
     * 动态条件查询（列表查询）
     *
     * @param dto 查询条件
     * @return 定价模板列表
     */
    List<SkuPricingDO> selectByCondition(@Param("dto") SkuPricingDTO dto);

    /**
     * 动态条件分页查询（使用 MyBatis-Plus IPage）
     *
     * @param page 分页对象
     * @param dto  查询条件
     * @return 分页结果
     */
    IPage<SkuPricingDO> selectPageByCondition(IPage<SkuPricingDO> page, @Param("dto") SkuPricingDTO dto);
}
