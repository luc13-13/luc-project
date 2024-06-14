package com.lc.framework.security.service;

import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

/**
 * <pre>
 *     oauth2 响应转换接口， 解决多种oauth2-server返回格式不一致问题
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/30 15:47
 */
public interface OAuth2ResponseConverter<T extends OAuth2AccessTokenResponse> {
}
