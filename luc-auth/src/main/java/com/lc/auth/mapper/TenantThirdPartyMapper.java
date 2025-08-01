package com.lc.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.auth.domain.entity.TenantThirdParty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <pre>
 * 租户第三方账号绑定数据访问层
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Mapper
public interface TenantThirdPartyMapper extends BaseMapper<TenantThirdParty> {

    /**
     * 根据第三方平台和用户ID查询绑定信息
     *
     * @param provider       第三方平台类型
     * @param providerUserId 第三方平台用户ID
     * @return 绑定信息
     */
    @Select("SELECT * FROM sys_tenant_third_party WHERE provider = #{provider} AND provider_user_id = #{providerUserId} AND deleted = 0")
    TenantThirdParty findByProviderAndUserId(@Param("provider") String provider, @Param("providerUserId") String providerUserId);

    /**
     * 根据租户ID查询所有绑定的第三方账号
     *
     * @param tenantId 租户ID
     * @return 绑定信息列表
     */
    @Select("SELECT * FROM sys_tenant_third_party WHERE tenant_id = #{tenantId} AND bind_status = 1 AND deleted = 0")
    List<TenantThirdParty> findByTenantId(@Param("tenantId") String tenantId);

    /**
     * 根据租户ID和第三方平台查询绑定信息
     *
     * @param tenantId 租户ID
     * @param provider 第三方平台类型
     * @return 绑定信息
     */
    @Select("SELECT * FROM sys_tenant_third_party WHERE tenant_id = #{tenantId} AND provider = #{provider} AND bind_status = 1 AND deleted = 0")
    TenantThirdParty findByTenantIdAndProvider(@Param("tenantId") String tenantId, @Param("provider") String provider);

    /**
     * 检查第三方账号是否已绑定其他租户
     *
     * @param provider       第三方平台类型
     * @param providerUserId 第三方平台用户ID
     * @param excludeTenantId 排除的租户ID
     * @return 绑定数量
     */
    @Select("SELECT COUNT(*) FROM sys_tenant_third_party WHERE provider = #{provider} AND provider_user_id = #{providerUserId} AND tenant_id != #{excludeTenantId} AND bind_status = 1 AND deleted = 0")
    Long countBoundToOtherTenant(@Param("provider") String provider, @Param("providerUserId") String providerUserId, @Param("excludeTenantId") String excludeTenantId);
}
