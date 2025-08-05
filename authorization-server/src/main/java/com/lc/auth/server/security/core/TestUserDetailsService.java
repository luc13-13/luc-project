package com.lc.auth.server.security.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("尝试加载用户: {}", username);
        
        if ("admin".equals(username)) {
            // 创建 admin 用户，密码为 admin
            String encodedPassword = passwordEncoder.encode("admin");
            log.info("用户 admin 找到，编码后密码: {}", encodedPassword);
            
            UserDetails user = User.builder()
                    .username("admin")
                    .password(encodedPassword)
                    .authorities("ROLE_ADMIN", "ROLE_USER")
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();
                    
            log.info("返回用户详情: {}", user);
            return user;
        }
        
        log.warn("用户不存在: {}", username);
        throw new UsernameNotFoundException("用户不存在: " + username);
    }

    @Override
    public UserDetails loadByMobile(String mobile) {
        try {
            // 简化处理：如果手机号是 13800138000，则映射到 admin 用户
            if ("13800138000".equals(mobile)) {
                UserDetails user = User.builder()
                        .username("admin")
                        .authorities("ROLE_ADMIN", "ROLE_USER")
                        .accountExpired(false)
                        .accountLocked(false)
                        .credentialsExpired(false)
                        .disabled(false)
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
    public UserDetails loadByMail(String mail) {
        return null;
    }

    @Override
    public UserDetails loadByUserId(String userId) {
        return null;
    }

    @Override
    public boolean support(String clientId) {
        return false;
    }
}
