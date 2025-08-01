package com.lc.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.auth.domain.entity.OAuth2Client;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <pre>
 * OAuth2客户端数据访问层
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Mapper
public interface OAuth2ClientMapper extends BaseMapper<OAuth2Client> {

    /**
     * 根据客户端ID查询客户端信息
     *
     * @param clientId 客户端ID
     * @return 客户端信息
     */
    @Select("SELECT * FROM oauth2_registered_client WHERE client_id = #{clientId} AND deleted = 0")
    OAuth2Client findByClientId(@Param("clientId") String clientId);

    /**
     * 检查客户端ID是否存在
     *
     * @param clientId 客户端ID
     * @return 存在数量
     */
    @Select("SELECT COUNT(*) FROM oauth2_registered_client WHERE client_id = #{clientId} AND deleted = 0")
    Long countByClientId(@Param("clientId") String clientId);
}
