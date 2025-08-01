package com.lc.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.auth.domain.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <pre>
 * 租户数据访问层
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {

    /**
     * 根据用户名查询租户
     *
     * @param username 用户名
     * @return 租户信息
     */
    @Select("SELECT * FROM sys_tenant WHERE username = #{username} AND deleted = 0")
    Tenant findByUsername(@Param("username") String username);

    /**
     * 根据手机号查询租户
     *
     * @param phone 手机号
     * @return 租户信息
     */
    @Select("SELECT * FROM sys_tenant WHERE phone = #{phone} AND deleted = 0")
    Tenant findByPhone(@Param("phone") String phone);

    /**
     * 根据邮箱查询租户
     *
     * @param email 邮箱
     * @return 租户信息
     */
    @Select("SELECT * FROM sys_tenant WHERE email = #{email} AND deleted = 0")
    Tenant findByEmail(@Param("email") String email);

    /**
     * 根据租户ID查询租户
     *
     * @param tenantId 租户ID
     * @return 租户信息
     */
    @Select("SELECT * FROM sys_tenant WHERE tenant_id = #{tenantId} AND deleted = 0")
    Tenant findByTenantId(@Param("tenantId") String tenantId);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 存在数量
     */
    @Select("SELECT COUNT(*) FROM sys_tenant WHERE username = #{username} AND deleted = 0")
    Long countByUsername(@Param("username") String username);

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @return 存在数量
     */
    @Select("SELECT COUNT(*) FROM sys_tenant WHERE phone = #{phone} AND deleted = 0")
    Long countByPhone(@Param("phone") String phone);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 存在数量
     */
    @Select("SELECT COUNT(*) FROM sys_tenant WHERE email = #{email} AND deleted = 0")
    Long countByEmail(@Param("email") String email);
}
