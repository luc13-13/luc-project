package com.lc.auth.server.security.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LoginUserDetail extends User implements OAuth2AuthenticatedPrincipal {

    /**
     * uid
     */
    private String id;

    /**
     * tenantId
     */
    private String tenantId;

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
        super(username, password, authorities);
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
}
