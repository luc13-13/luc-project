package com.lc.auth.server.security.authentication.extension.sms;

import com.lc.auth.server.security.core.LoginUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 短信验证码认证提供者
 */
@Slf4j
public class SmsAuthenticationProvider implements AuthenticationProvider {
    
    private final SmsCodeService smsCodeService;
    private final LoginUserDetailService userDetailsService;
    
    public SmsAuthenticationProvider(SmsCodeService smsCodeService, LoginUserDetailService userDetailsService) {
        this.smsCodeService = smsCodeService;
        this.userDetailsService = userDetailsService;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 只处理用户名密码认证Token
        if (!(authentication instanceof UsernamePasswordAuthenticationToken authenticationToken)) {
            return null;
        }

        String username = (String) authenticationToken.getPrincipal();
        String password = (String) authenticationToken.getCredentials();

        // 检查是否是手机号格式
        if (!username.matches("^1[3-9]\\d{9}$")) {
            return null; // 不是手机号，让其他Provider处理
        }

        log.info("检测到手机号登录请求，手机号: {}, 验证码: {}", username, password);

        // 验证短信验证码
        if (!smsCodeService.verifyCode(username, password)) {
            throw new BadCredentialsException("验证码错误或已过期");
        }

        // 根据手机号获取用户信息
        UserDetails userDetails = loadUserByPhone(username);

        // 创建已认证的Token
        UsernamePasswordAuthenticationToken authenticationResult = new UsernamePasswordAuthenticationToken(
                userDetails, password, userDetails.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());

        log.info("手机号登录认证成功，用户: {}", userDetails.getUsername());
        return authenticationResult;
    }


    
    /**
     * 根据手机号加载用户信息
     * 这里简化处理，实际项目中应该有手机号到用户名的映射
     */
    private UserDetails loadUserByPhone(String phone) {
        try {
            // 简化处理：如果手机号是 13800138000，则映射到 admin 用户
            if ("13800138000".equals(phone)) {
                return userDetailsService.loadUserByUsername("admin");
            }
            
            // 其他手机号暂时不支持
            throw new UsernameNotFoundException("手机号未绑定用户: " + phone);
            
        } catch (UsernameNotFoundException e) {
            log.error("根据手机号 {} 查找用户失败", phone);
            throw new BadCredentialsException("手机号未绑定用户");
        }
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
