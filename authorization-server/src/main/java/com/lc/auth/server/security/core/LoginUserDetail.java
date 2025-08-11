package com.lc.auth.server.security.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <pre>
 *     定义系统登录用户属性封装类
 *     自定义的UserDetails实现类需要被@JacksonAnnotation标注， 否则无法被OAuth2AuthorizationService#ObjectMapper解析
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/27 17:13
 */
@Data
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LoginUserDetail implements OAuth2AuthenticatedPrincipal, UserDetails, CredentialsContainer {

    /**
     * uid
     */
    private String id;

    /**
     * tenantId
     */
    private String tenantId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 权限集合
     */
    private Set<GrantedAuthority> authorities;

    /**
     * 账号是否未过期：<br/>true未过期<br/>false过期
     */
    private boolean accountNonExpired;

    /**
     * 账号是否未锁定：<br/>true未锁定<br/>false锁定
     */
    private boolean accountNonLocked;

    /**
     * 认证签发时间
     */
    private Instant credentialsIssuedAt;

    /**
     * 认证过期时间
     */
    private Instant credentialsExpiredAt;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 登陆过期时间
     */
    private LocalDateTime expirationDateTime;

    /**
     * 账号授权类型
     */
    private String grantType;

    /**
     * 账号锁定状态
     */
    private final AtomicBoolean nonLocked = new AtomicBoolean(true);

    private final Map<String, Object> attributes = new HashMap<>();

    public LoginUserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
    }


    public boolean lock() {
        return nonLocked.compareAndSet(true, false);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return this.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return accountNonExpired && accountNonLocked;
    }

    /**
     * 认证信息是否未过期
     * @return true未过期，false过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        if (credentialsIssuedAt == null) {
            return false;
        }
        if (credentialsExpiredAt == null) {
            return true;
        }
        return !credentialsExpiredAt.isBefore(Instant.now());
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        // Ensure array iteration order is predictable (as per
        // UserDetails.getAuthorities() contract and SEC-717)
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(Comparator.nullsLast(Comparator.comparing(GrantedAuthority::getAuthority)));
        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }
        return sortedAuthorities;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
