package com.lc.auth.server.security.core;

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
    LoginUserDetail loadByMobile(String mobile);

    /**
     * @param mail 邮箱
     * @return 用户信息
     */
    LoginUserDetail loadByMail(String mail);

    /**
     * @param userId 用户id
     * @return 用户信息
     */
    LoginUserDetail loadByUserId(String userId);

    /**
     * @param clientId 客户端id， 用于校验是否支持当前的客户端请求
     * @return true支持，false不支持
     */
    boolean support(String clientId);
}
