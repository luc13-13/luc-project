package com.lc.framework.security.auth.server.authentication.extension.sms;

import com.lc.framework.security.core.user.LoginUserDetailService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 短信验证码认证提供者
 */
@Slf4j
public class SmsAuthenticationProvider implements AuthenticationProvider {
    
    private final SmsCodeService smsCodeService;
    private final LoginUserDetailService loginUserDetailService;
    
    public SmsAuthenticationProvider(SmsCodeService smsCodeService, LoginUserDetailService loginUserDetailService) {
        this.smsCodeService = smsCodeService;
        this.loginUserDetailService = loginUserDetailService;
    }
    
    @Override
    public Authentication authenticate(@Nullable Authentication authentication) throws AuthenticationException {

        // 只处理用户名密码认证Token
        if (!(authentication instanceof SmsAuthenticationToken authenticationToken)) {
            return null;
        }

        String phone = (String) authenticationToken.getPrincipal();
        String code = (String) authenticationToken.getCredentials();

        // 检查是否是手机号格式
        assert phone != null;
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            return null; // 不是手机号，让其他Provider处理
        }

        log.info("检测到手机号登录请求，手机号: {}, 验证码: {}", phone, code);

        // 验证短信验证码
        if (!smsCodeService.verifyCode(phone, code)) {
            throw new BadCredentialsException("验证码错误或已过期");
        }

        // 根据手机号获取用户信息
        UserDetails userDetails = loginUserDetailService.loadByMobile(phone);

        // 创建已认证的Token
        SmsAuthenticationToken authenticationResult = SmsAuthenticationToken.authenticated(
                phone, code, userDetails.getAuthorities());
        log.info("手机号登录认证成功，用户: {}", userDetails.getUsername());
        return authenticationResult;
    }
    
    @Override
    public boolean supports(@Nonnull Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
