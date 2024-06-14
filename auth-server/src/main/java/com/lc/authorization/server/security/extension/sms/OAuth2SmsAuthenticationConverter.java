package com.lc.authorization.server.security.extension.sms;

import com.lc.authorization.server.constants.OAuth2ParameterConstants;
import com.lc.authorization.server.security.extension.BasicAuthenticationConverter;
import com.lc.authorization.server.utils.OAuth2EndpointUtils;
import com.lc.framework.common.utils.SpringBeanUtils;
import com.lc.framework.redis.starter.utils.RedisHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * <pre>
 *     认证授权服务只关心获取UserDetail， 手机号与验证码的准确性交给
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/3/11 11:02
 */
public class OAuth2SmsAuthenticationConverter extends BasicAuthenticationConverter<OAuth2SmsAuthenticationToken> {

    public RedisHelper redisHelper;

    public static final AuthorizationGrantType SMS = new AuthorizationGrantType("sms");

    @Override
    public boolean support(String grantType) {
        return SMS.getValue().equals(grantType);
    }

    @Override
    protected void checkParams(HttpServletRequest request, MultiValueMap<String, String> parameters) {
        // 手机号有效性检验
        String mobile = parameters.getFirst(OAuth2ParameterConstants.mobile);
        String captcha = parameters.getFirst(OAuth2ParameterConstants.captcha);
        if (!StringUtils.hasText(mobile)) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterConstants.mobile, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        if (!StringUtils.hasText(captcha)) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterConstants.captcha, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        // 验证码存在性检验
        SpringBeanUtils.getBean(RedisHelper.class);
        //
    }

    @Override
    public OAuth2SmsAuthenticationToken unauthenticatedToken(Authentication clientPrincipal, Set<String> requestedScopes, MultiValueMap<String, String> requestParameters) {

        return new OAuth2SmsAuthenticationToken(SMS, clientPrincipal, requestedScopes, requestParameters.getFirst(OAuth2ParameterConstants.mobile), requestParameters.getFirst(OAuth2ParameterConstants.captcha));
    }
}
