package com.lc.authorization.server.security.extension;

import com.lc.framework.security.core.LoginUserDetail;
import com.lc.framework.security.service.LoginUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/28 10:49
 */
@Slf4j
public class DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    /**
     * The plaintext password used to perform
     * {@link PasswordEncoder#matches(CharSequence, String)} on when the user is not found
     * to avoid SEC-2056.
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    private volatile String userNotFoundEncodedPassword;

    private LoginUserDetailService loginUserDetailService;

    private UserDetailsPasswordService userDetailsPasswordService;

    private PasswordEncoder passwordEncoder;

    public DaoAuthenticationProvider(LoginUserDetailService loginUserDetailService) {
        this(loginUserDetailService, PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    /**
     * Creates a new instance using the provided {@link PasswordEncoder}
     * @param passwordEncoder the {@link PasswordEncoder} to use. Cannot be null.
     * @since 6.0.3
     */
    public DaoAuthenticationProvider(LoginUserDetailService loginUserDetailService, PasswordEncoder passwordEncoder) {
        super();
        this.loginUserDetailService = loginUserDetailService;
        setPasswordEncoder(passwordEncoder);
    }


    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        String presentedPassword = authentication.getCredentials().toString();
        log.info("检查登录请求 {}：{}",authentication.getPrincipal(), presentedPassword);
        log.info("查询到的用户信息 {}：{}", userDetails.getUsername(), userDetails.getPassword());
        // 如果登录类型为用户名密码登录，则进行密码校验
        // 这里matches方法第一个参数为明文密码，第二个参数为加密后的密码
        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            this.logger.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getPrincipal() instanceof LoginUserDetail loginUserDetail) {
            return loginUserDetailService.loadByUserId(loginUserDetail.getAttribute("id"));
        }
        return loginUserDetailService.loadUserByUsername(username);
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }

    public void setUserDetailsPasswordService(UserDetailsPasswordService userDetailsPasswordService) {
        this.userDetailsPasswordService = userDetailsPasswordService;
    }
}
