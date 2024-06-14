package com.lc.framework.security.service;

import com.lc.framework.security.core.LoginUserDetail;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <pre>
 *  获取用户信息的接口，用于DaoAuthenticationProvider获取用户信息, 调用方进行方法实现
 *  返回类型可以使用{@link LoginUserDetail}
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/28 8:58
 */
public interface LoginUserDetailService extends UserDetailsService {

    /**
     * @param mobile 手机号
     * @return 用户信息
     */
    UserDetails loadByMobile(String mobile);

    /**
     * @param mail 邮箱
     * @return 用户信息
     */
    UserDetails loadByMail(String mail);

    /**
     * @param userId 用户id
     * @return 用户信息
     */
    UserDetails loadByUserId(String userId);

    default UserDetails loadByUser(LoginUserDetail userDetail) {
        return this.loadUserByUsername(userDetail.getUsername());
    }

    /**
     * @param clientId 客户端id， 用于校验是否支持当前的客户端请求
     * @return true支持，false不支持
     */
    boolean support(String clientId);
}
