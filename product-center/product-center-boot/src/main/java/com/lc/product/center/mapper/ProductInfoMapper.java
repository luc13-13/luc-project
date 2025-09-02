package com.lc.product.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.product.center.domain.entity.ProductInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 产品信息表(product_center.product_info)表数据库访问层
 *
 * @author lucheng
 * @since 2025-08-31
 */
@Mapper
public interface ProductInfoMapper extends BaseMapper<ProductInfoDO> {

}

