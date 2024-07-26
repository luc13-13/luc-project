package com.lc.framework.security.client;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

/**
 * <pre>
 *     查询系统支持的OAuth2客户端信息
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/7/16 16:15
 */
public interface LucClientRegistrationRepository extends ClientRegistrationRepository {

    /**
     * 注册OAuth2客户端
     * @param clientRegistration 封装客户端信息
     * @return true注册成功，false注册失败
     */
    boolean register(ClientRegistration clientRegistration);

    /**
     *
     * @param registrationId
     * @return
     */
    boolean delete(String registrationId);
}
