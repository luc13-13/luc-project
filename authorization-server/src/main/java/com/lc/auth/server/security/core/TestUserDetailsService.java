package com.lc.auth.server.security.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

/**
 * 测试用户详情服务
 * 用于支持 admin/admin 登录
 */
@Service
@Slf4j
public class TestUserDetailsService implements LoginUserDetailService {
    
    private final PasswordEncoder passwordEncoder;
    
    public TestUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public LoginUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("尝试加载用户: {}", username);
        
        if ("admin".equals(username)) {
            // 创建 admin 用户，密码为 admin
            String encodedPassword = passwordEncoder.encode("admin");
            log.info("用户 admin 找到，编码后密码: {}", encodedPassword);

            LoginUserDetail user = LoginUserDetail.builder()
                    .id("admin001")
                    .username("admin")
                    .password(encodedPassword)
                    .authorities(Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER")))
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsIssuedAt(Instant.now())
                    .build();
                    
            log.info("返回用户详情: {}", user);
            return user;
        }
        
        log.warn("用户不存在: {}", username);
        throw new UsernameNotFoundException("用户不存在: " + username);
    }

    @Override
    public LoginUserDetail loadByMobile(String mobile) {
        try {
            // 简化处理：如果手机号是 13800138000，则映射到 admin 用户
            if ("13800138000".equals(mobile)) {
                LoginUserDetail user = LoginUserDetail.builder()
                        .username("admin")
                        .authorities(Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER")))
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsIssuedAt(Instant.now())
                        .build();

                log.info("根据手机号 {} 返回用户详情: {}", mobile, user);
                return user;
            }

            // 其他手机号暂时不支持
            throw new UsernameNotFoundException("手机号未绑定用户: " + mobile);

        } catch (UsernameNotFoundException e) {
            log.error("根据手机号 {} 查找用户失败", mobile);
            throw new BadCredentialsException("手机号未绑定用户");
        }
    }

    @Override
    public LoginUserDetail loadByMail(String mail) {
        return null;
    }

    @Override
    public LoginUserDetail loadByUserId(String userId) {
        return null;
    }

    @Override
    public boolean support(String clientId) {
        return false;
    }
}
