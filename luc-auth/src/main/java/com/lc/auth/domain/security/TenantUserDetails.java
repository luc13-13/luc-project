package com.lc.auth.domain.security;

import com.lc.auth.domain.entity.Tenant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <pre>
 * 自定义租户用户详情实现
 * 实现Spring Security的UserDetails接口，用于认证和授权
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TenantUserDetails implements UserDetails {

    /**
     * 租户信息
     */
    private Tenant tenant;

    /**
     * 权限集合
     */
    private Set<String> authorities;

    /**
     * 角色集合
     */
    private Set<String> roles;

    /**
     * 构造函数
     *
     * @param tenant 租户信息
     */
    public TenantUserDetails(Tenant tenant) {
        this.tenant = tenant;
        this.authorities = Collections.emptySet();
        this.roles = Collections.emptySet();
    }

    /**
     * 构造函数
     *
     * @param tenant      租户信息
     * @param authorities 权限集合
     * @param roles       角色集合
     */
    public TenantUserDetails(Tenant tenant, Set<String> authorities, Set<String> roles) {
        this.tenant = tenant;
        this.authorities = authorities != null ? authorities : Collections.emptySet();
        this.roles = roles != null ? roles : Collections.emptySet();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = this.authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        // 添加角色权限（角色需要以ROLE_前缀）
        this.roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .forEach(grantedAuthorities::add);

        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return tenant != null ? tenant.getPassword() : null;
    }

    @Override
    public String getUsername() {
        return tenant != null ? tenant.getUsername() : null;
    }

    /**
     * 获取租户ID
     *
     * @return 租户ID
     */
    public String getTenantId() {
        return tenant != null ? tenant.getTenantId() : null;
    }

    /**
     * 获取手机号
     *
     * @return 手机号
     */
    public String getPhone() {
        return tenant != null ? tenant.getPhone() : null;
    }

    /**
     * 获取邮箱
     *
     * @return 邮箱
     */
    public String getEmail() {
        return tenant != null ? tenant.getEmail() : null;
    }

    /**
     * 获取真实姓名
     *
     * @return 真实姓名
     */
    public String getRealName() {
        return tenant != null ? tenant.getRealName() : null;
    }

    /**
     * 获取头像
     *
     * @return 头像URL
     */
    public String getAvatar() {
        return tenant != null ? tenant.getAvatar() : null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 状态为2表示锁定
        return tenant == null || !Integer.valueOf(2).equals(tenant.getStatus());
    }

    @Override
    public boolean isEnabled() {
        // 状态为1表示启用
        return tenant != null && Integer.valueOf(1).equals(tenant.getStatus());
    }
}
