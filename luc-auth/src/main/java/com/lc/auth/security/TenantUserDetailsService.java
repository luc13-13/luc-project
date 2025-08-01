package com.lc.auth.security;

import com.lc.auth.domain.security.TenantUserDetails;
import com.lc.auth.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * 租户用户详情服务
 * 实现Spring Security的UserDetailsService接口
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantUserDetailsService implements UserDetailsService {

    private final TenantService tenantService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("加载用户详情: username={}", username);
        
        TenantUserDetails userDetails = tenantService.loadUserByUsername(username);
        if (userDetails == null) {
            log.warn("用户不存在: username={}", username);
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        
        log.debug("用户详情加载成功: username={}, tenantId={}", username, userDetails.getTenantId());
        return userDetails;
    }

    /**
     * 根据手机号加载用户详情
     *
     * @param phone 手机号
     * @return 用户详情
     * @throws UsernameNotFoundException 用户不存在异常
     */
    public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
        log.debug("根据手机号加载用户详情: phone={}", phone);
        
        TenantUserDetails userDetails = tenantService.loadUserByPhone(phone);
        if (userDetails == null) {
            log.warn("手机号对应的用户不存在: phone={}", phone);
            throw new UsernameNotFoundException("手机号对应的用户不存在: " + phone);
        }
        
        log.debug("用户详情加载成功: phone={}, tenantId={}", phone, userDetails.getTenantId());
        return userDetails;
    }
}
