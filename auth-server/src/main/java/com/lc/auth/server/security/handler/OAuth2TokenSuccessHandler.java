package com.lc.auth.server.security.handler;

import com.lc.auth.server.domain.dto.OAuth2TokenDTO;
import com.lc.auth.server.utils.JsonUtils;
import com.lc.auth.server.utils.SecurityUtils;
import com.lc.framework.redis.starter.utils.RedisHelper;
import com.lc.framework.core.mvc.WebResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static com.lc.framework.core.constants.RequestHeaderConstants.*;

/**
 * <pre>
 *     只处理获取token成功，
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/18 9:37
 */
@Component
@Slf4j
public class OAuth2TokenSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private RedisHelper redisHelper;

    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter =
            new OAuth2AccessTokenResponseHttpMessageConverter();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication =
                (OAuth2AccessTokenAuthenticationToken) authentication;

        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        RegisteredClient registeredClient = accessTokenAuthentication.getRegisteredClient();

        OAuth2TokenDTO tokenDTO = OAuth2TokenDTO.builder()
                .tokenType(Objects.nonNull(accessToken.getTokenType()) ? accessToken.getTokenType().getValue() : "Bearer")
                .accessToken(accessToken.getTokenValue())
                .build();
        if (accessToken.getExpiresAt() != null) {
            tokenDTO.setAccessTokenExpiredAt(accessToken.getExpiresAt().toEpochMilli());
        }
        if (Objects.nonNull(refreshToken)) {
            tokenDTO.setRefreshToken(refreshToken.getTokenValue());
            if (refreshToken.getExpiresAt() != null) {
                tokenDTO.setRefreshTokenExpiredAt(refreshToken.getExpiresAt().toEpochMilli());
            }
        }
        String tokenKey = SecurityUtils.getTokenKey(request);
        String code;
        // 针对knife4j的认证请求特殊处理
        if(registeredClient != null && registeredClient.getClientId().equals("knife4j-client")) {
            tokenKey = KNIFE4J_TOKEN_KEY;
        }
        // 授权码认证则从redis中获取tokenKey TODO:获取授权码时向redis中存储tokenKey
        if ((code = request.getParameter("code")) != null) {
            // tokenKey 为空时， 如果是/oauth2/authorize接口， 则去redis中查找颁发的code对应的jsessionid
            // 授权码模式， 不做类型转换
            tokenKey = redisHelper.get(code);
            log.info("tokenKey为空，尝试为请求:{} 从redis获取tokenKey：{}", request.getRequestURI(), tokenKey);
            OAuth2AccessTokenResponse.Builder builder =
                    OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                            .tokenType(accessToken.getTokenType())
                            .scopes(accessToken.getScopes());
            if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
                builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
            }
            if (refreshToken != null) {
                builder.refreshToken(refreshToken.getTokenValue());
            }
            OAuth2AccessTokenResponse accessTokenResponse = builder.build();
            ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
            redisHelper.hPut(ACCESS_TOKEN, tokenKey, accessToken);
            redisHelper.hPut(REFRESH_TOKEN, tokenKey, refreshToken);
            this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
        } else {
            log.info("tokenKey为：{}, 缓存accessToken", tokenKey);
            redisHelper.hPut(ACCESS_TOKEN, tokenKey, accessToken);
            redisHelper.hPut(REFRESH_TOKEN, tokenKey, refreshToken);
            WebResult<OAuth2TokenDTO> result = WebResult.successData(tokenDTO);

            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(JsonUtils.objectCovertToJson(result));
            response.getWriter().flush();
        }
    }
}
