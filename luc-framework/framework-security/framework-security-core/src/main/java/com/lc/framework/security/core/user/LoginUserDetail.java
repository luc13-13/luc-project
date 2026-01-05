package com.lc.framework.security.core.user;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <pre>
 *     定义系统登录用户属性封装类
 *     使用 @JsonTypeInfo 注解确保序列化时包含类型信息，便于从 Redis 反序列化
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/27 17:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
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
     * 账号是否未过期：<br/>
     * true未过期<br/>
     * false过期
     */
    private boolean accountNonExpired;

    /**
     * 账号是否未锁定：<br/>
     * true未锁定<br/>
     * false锁定
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

    private final Map<String, Object> attributes = new HashMap<>();

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    /**
     * Jackson 反序列化时使用此方法设置 attributes
     * 由于 attributes 是 final 字段，无法直接赋值，需要通过 putAll 合并数据
     */
    @com.fasterxml.jackson.annotation.JsonSetter("attributes")
    public void setAttributes(Map<String, Object> attributes) {
        if (attributes != null) {
            this.attributes.clear();
            this.attributes.putAll(attributes);
        }
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
     * 
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
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(
                Comparator.nullsLast(Comparator.comparing(GrantedAuthority::getAuthority)));
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
