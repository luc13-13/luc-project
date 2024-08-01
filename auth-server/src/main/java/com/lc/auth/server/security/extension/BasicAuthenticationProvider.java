package com.lc.auth.server.security.extension;

import com.lc.auth.server.utils.OAuth2EndpointUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.Principal;
import java.util.*;

import static com.lc.framework.core.constants.RequestHeaderConstants.ACCESS_TOKEN;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/8 9:02
 */
@Slf4j
public abstract class BasicAuthenticationProvider<T extends BasicAuthenticationToken> implements AuthenticationProvider, InitializingBean {

    private final Class<T> innerType;

    // 将权限保存
    private final OAuth2AuthorizationService authorizationService;

    // 将Token转为UsernamePasswordToken， 从而调用DaoAuthenticationProvider， 从数据库获取用户信息
    private final AuthenticationManager authenticationManager;

    // 从tokenContext创建accessToken和refreshToken
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    @SuppressWarnings("unchecked")
    public BasicAuthenticationProvider(HttpSecurity http) {
        // 初始化时保存泛型实现类的Class信息，避免泛型擦除
        innerType = this.getType();
        this.tokenGenerator = http.getSharedObject(OAuth2TokenGenerator.class);
        this.authenticationManager = http.getSharedObject(AuthenticationManager.class);
        this.authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);
    }

    @SuppressWarnings("unchecked")
    private Class<T> getType() {
        Type superClass = getClass().getGenericSuperclass();
        if (!(superClass instanceof ParameterizedType)) {
            throw new IllegalArgumentException("not parameterized type!");
        }
        return (Class<T>) ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    @SuppressWarnings({"unchecked", "deprecation", "unused"})
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("接收到认证请求： {}", authentication);

        T resourceOwnerToken = (T) authentication;

        // 获取认证信息，要求用户登录后才可以获取token：登录可分为客户端认证、表单登录
        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(resourceOwnerToken);

        // 校验客户端信息
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        log.info("获取到客户端信息：{}", registeredClient);
        if (registeredClient == null) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
        }
        checkRegisteredClient(registeredClient);

        // 获取客户端注册的权限，校验请求是否符合客户端注册信息
        Set<String> authorizedScopes;
        if (!CollectionUtils.isEmpty(resourceOwnerToken.getScopes())) {
            for (String requestedScope : resourceOwnerToken.getScopes()) {
                if (registeredClient.getScopes() == null || !registeredClient.getScopes().contains(requestedScope)) {
                    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
                }
            }
            authorizedScopes = new LinkedHashSet<>(resourceOwnerToken.getScopes());
        } else {
            authorizedScopes = new LinkedHashSet<>();
        }
        // 交给子类构建token
        UsernamePasswordAuthenticationToken usernamePasswordToken = buildToken(resourceOwnerToken);
        try {
            Authentication usernamePasswordAuthentication = authenticationManager.authenticate(usernamePasswordToken);

            // @formatter:off
            DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                    .registeredClient(registeredClient)
                    .principal(usernamePasswordAuthentication)
                    .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                    .authorizedScopes(authorizedScopes)
                    .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                    .authorizationGrant(resourceOwnerToken);
            // @formatter:on

            OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
                    .withRegisteredClient(registeredClient)
                    .principalName(usernamePasswordAuthentication.getName())
                    .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                    .authorizedScopes(authorizedScopes);

            // ----- Access token -----
            OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
            OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
            if (generatedAccessToken == null) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the access token.", OAuth2EndpointUtils.DEFAULT_ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }
            OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                    generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                    generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
            if (generatedAccessToken instanceof ClaimAccessor) {
                authorizationBuilder
                        // 暂时不指定id， 因为token长度超过了表的id列限制
//                        .id(accessToken.getTokenValue())
                        .token(accessToken,
                                (metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                                        ((ClaimAccessor) generatedAccessToken).getClaims()))
                        // 0.4.0 新增的方法
                        .authorizedScopes(authorizedScopes)
                        .attribute(Principal.class.getName(), usernamePasswordAuthentication);
            } else {
                authorizationBuilder
//                        .id(accessToken.getTokenValue())
                        .accessToken(accessToken);
            }

            // ----- Refresh token -----
            OAuth2RefreshToken refreshToken = null;
            if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                    // Do not issue refresh token to public client
                    !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

                tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
                OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
                if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                    OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                            "The token generator failed to generate the refresh token.", OAuth2EndpointUtils.DEFAULT_ERROR_URI);
                    throw new OAuth2AuthenticationException(error);
                }
                refreshToken = (OAuth2RefreshToken) generatedRefreshToken;

                authorizationBuilder.refreshToken(refreshToken);
            }

            // 保存认证信息
            OAuth2Authorization authorization = authorizationBuilder.build();
            authorizationService.save(authorization);

            // 封装与token相关的额外参数
            Map<String, Object> additionalParameters = Collections.singletonMap(ACCESS_TOKEN, "");

            // 返回认证成功的token
            return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken, additionalParameters);
        } catch (Exception ex) {
//            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.SERVER_ERROR, OAuth2ErrorCodes.INVALID_TOKEN, DEFAULT_ERROR_URI);
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.SERVER_ERROR);
        }
        // 检查token有效性
    }

    /**
     * 构建token
     *
     * @return 子类实现的token
     */
    public abstract UsernamePasswordAuthenticationToken buildToken(T resourceOwnerToken);

    protected void checkRegisteredClient(RegisteredClient registeredClient) {
        if (registeredClient == null) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean support = innerType.isAssignableFrom(authentication);
        if (support) log.info("supports authentication={}", authentication);
        return support;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(innerType, "innerType must be defined");
        Assert.notNull(authenticationManager, "authenticationManager must be defined");
        Assert.notNull(authorizationService, "authorizationService must be defined");
        Assert.notNull(tokenGenerator, "tokenGenerator must be defined");
    }

    private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(
            Authentication authentication) {

        OAuth2ClientAuthenticationToken clientPrincipal = null;

        // 客户端发来的认证请求
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }

        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }

        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }
}
