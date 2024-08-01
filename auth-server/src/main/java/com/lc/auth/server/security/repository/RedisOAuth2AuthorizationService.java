package com.lc.auth.server.security.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;

/**
 * <pre>
 *     授权结果存入redis中
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/3/11 9:52
 */
@Slf4j
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {
    @Override
    public void save(OAuth2Authorization authorization) {

    }

    @Override
    public void remove(OAuth2Authorization authorization) {

    }

    @Override
    public OAuth2Authorization findById(String id) {
        return null;
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        return null;
    }
}
