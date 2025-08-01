package com.lc.auth.security;

import com.lc.auth.domain.security.TenantUserDetails;
import com.lc.auth.service.SmsService;
import com.lc.auth.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 短信验证码认证提供者
 * 支持手机号+验证码的认证方式
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private final TenantService tenantService;
    private final SmsService smsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        
        String phone = (String) authenticationToken.getPrincipal();
        String code = (String) authenticationToken.getCredentials();
        
        log.debug("短信验证码认证: phone={}", phone);
        
        // 验证验证码
        if (!smsService.verifyCode(phone, code, "login")) {
            throw new BadCredentialsException("验证码错误或已过期");
        }
        
        // 加载用户信息
        TenantUserDetails userDetails = tenantService.loadUserByPhone(phone);
        if (userDetails == null) {
            throw new BadCredentialsException("手机号未注册");
        }
        
        // 检查账号状态
        if (!userDetails.isEnabled()) {
            throw new BadCredentialsException("账号已被禁用");
        }
        
        if (!userDetails.isAccountNonLocked()) {
            throw new BadCredentialsException("账号已被锁定");
        }
        
        // 创建认证成功的Token
        SmsCodeAuthenticationToken successAuthentication = new SmsCodeAuthenticationToken(
                userDetails, code, userDetails.getAuthorities());
        successAuthentication.setDetails(authenticationToken.getDetails());
        
        log.info("短信验证码认证成功: phone={}, tenantId={}", phone, userDetails.getTenantId());
        return successAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
