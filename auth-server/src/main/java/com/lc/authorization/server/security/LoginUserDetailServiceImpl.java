package com.lc.authorization.server.security;

import com.lc.authorization.server.feign.InstitutionCenterFeignClient;
import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.security.core.LoginUserDetail;
import com.lc.framework.security.service.LoginUserDetailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/28 9:20
 */
@Service
public class LoginUserDetailServiceImpl implements LoginUserDetailService, UserDetailsPasswordService {

    private InstitutionCenterFeignClient institutionCenterFeignClient;

    public LoginUserDetailServiceImpl(InstitutionCenterFeignClient institutionCenterFeignClient) {
        this.institutionCenterFeignClient = institutionCenterFeignClient;
    }

    @Override
    public UserDetails loadByMobile(String mobile) {
        return null;
    }

    @Override
    public UserDetails loadByMail(String mail) {
        return null;
    }

    @Override
    public LoginUserDetail loadByUserId(String userId) {
        // 调用用户服务获取用户详情
        WebResult<String> userDetail = institutionCenterFeignClient.getUserDetail(userId);
        // 将用户详情转为UserDetail
        return convert2UserDetail(userDetail.getData());
    }

    @Override
    public boolean support(String clientId) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 调用用户服务获取用户详情
        WebResult<String> userDetail = institutionCenterFeignClient.getUserDetail(username);
        // 将用户详情转为UserDetail
        return convert2UserDetail(userDetail.getData());
    }

    public LoginUserDetail convert2UserDetail(String origin) {
        return new LoginUserDetail("admin",new BCryptPasswordEncoder().encode("123456"), new HashSet<>());
    }

    /**
     * 更新用户名密码
     * @param user the user to modify the password for
     * @param newPassword the password to change to, encoded by the configured
     * {@code PasswordEncoder}
     * @return 更新密码后的UserDetails
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }
}
