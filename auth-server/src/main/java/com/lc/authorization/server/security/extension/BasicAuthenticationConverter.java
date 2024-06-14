package com.lc.authorization.server.security.extension;

import com.lc.authorization.server.utils.OAuth2EndpointUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <pre>
 * 自定义认证方式的基础转换器。所有转换器的实现类都被封装在{@link DelegatingAuthenticationConverter}中,
 * {@link AuthenticationFilter} 对需要认证的请求，交给已经注册的provider进行判断与转换
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-12-07 09:29
 */
public abstract class BasicAuthenticationConverter<T extends BasicAuthenticationToken> implements AuthenticationConverter {
    // 从request中提取grantType后， 判断是否支持转换
    public abstract boolean support(String grantType);

    /**
     *  请求是否合法交给子类去校验
     *  例如
     *  （1）用户名密码登录时，需要校验用户名是否存在
     *  （2）短信登录时，需要校验验证码是否过期
      */
    protected void checkParams(HttpServletRequest request, MultiValueMap<String, String> parameters) {

    }

    /**
     *
     * @param clientPrincipal 当前请求授权的客户端信息
     * @param requestedScopes 请求参数中的scope
     * @return 未经过认证的Authentication， 交给Provider进行认证
     */
    public abstract T unauthenticatedToken(Authentication clientPrincipal, Set<String> requestedScopes, MultiValueMap<String, String> requestParameters);

    @Override
    public Authentication convert(HttpServletRequest request) {
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!support(grantType)) {
            return null;
        }
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
        // 校验必备的参数, 由子类根据自身认证方式实现该方法
        checkParams(request, parameters);
        Set<String> scopes = null;
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) &&
                parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.SCOPE, OAuth2EndpointUtils.DEFAULT_ERROR_URI);
        }
        if (StringUtils.hasText(scope)) {
            scopes = new HashSet<>(
                    Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }
        // 获取当前的客户端信息
        // 所有访问tokenEndpoint接口/oauth2/token的请求，getTokenIntrospectionEndpoint
        // 都会经过OAuth2ClientAuthenticationFilter,
        // 根据请求参数类型，经过JwtClientAssertionAuthenticationConverter/PublicClientAuthenticationConverter/ClientSecretBasicAuthenticationConverter/ClientSecretPostAuthenticationConverter
        // 转化为OAuth2ClientAuthenticationToken
        // 对请求参数中的client_id和client_secret校验成功后, 保存到ThreadLocal中， 然后request继续进入后续的Filter
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(clientPrincipal)) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ErrorCodes.INVALID_CLIENT, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        // 将请求转化为未认证的Authentication
        return unauthenticatedToken(clientPrincipal, scopes, parameters);
    }
}
