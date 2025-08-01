package com.lc.auth.security;

import com.lc.auth.service.TenantThirdPartyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * 自定义OAuth2用户服务
 * 处理第三方登录后的用户信息
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final TenantThirdPartyService thirdPartyService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 调用父类方法获取OAuth2User
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        // 获取第三方平台类型
        String provider = userRequest.getClientRegistration().getRegistrationId();
        
        log.info("加载第三方用户信息: provider={}, user={}", provider, oAuth2User.getName());
        
        try {
            // 处理第三方登录，返回自定义的TenantOAuth2User
            return thirdPartyService.processThirdPartyLogin(provider, oAuth2User);
        } catch (Exception e) {
            log.error("处理第三方登录失败: provider={}, user={}", provider, oAuth2User.getName(), e);
            throw new OAuth2AuthenticationException("第三方登录处理失败: " + e.getMessage());
        }
    }
}
