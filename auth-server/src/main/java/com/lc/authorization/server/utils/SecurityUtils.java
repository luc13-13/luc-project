package com.lc.authorization.server.utils;

import com.lc.authorization.server.security.repository.RedisSecurityContextRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.lc.framework.core.constants.RequestHeaderConstants.ACCESS_TOKEN;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2023/10/21 11:51
 * @version : 1.0
 */
@Slf4j
public class SecurityUtils {

    /**
     * 重写{@link OAuth2AuthorizationServerConfiguration#applyDefaultSecurity}, 避免初始化后无法自定义放行接口
     *
     * @param http
     * @throws Exception
     */
    public static void applyDefaultSecurity(HttpSecurity http, RequestMatcher[] requestMatchers) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();
        RequestMatcher endpointsMatcher = authorizationServerConfigurer
                .getEndpointsMatcher();

        http
                .securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(requestMatchers).permitAll()
                        .anyRequest().authenticated())
                .with(authorizationServerConfigurer, Customizer.withDefaults());
        // 在授权服务器信息中注入自定义的授权类型，否则资源服务器和客户端无法通过自定义的类型进行授权
        authorizationServerConfigurer
                .authorizationServerMetadataEndpoint(meta -> meta
                        .authorizationServerMetadataCustomizer(metaCustomizer -> metaCustomizer
                                .grantType("wechat")
                                .grantType("password")
                                .grantType("sms")
                        )
                );
    }

    public static void applyDefault(HttpSecurity http,
                                    AuthenticationEntryPoint entryPoint,
                                    RedisSecurityContextRepository redisSecurityContextRepository) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                // Redirect to the login page when not authenticated from the authorization endpoint
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                Objects.isNull(entryPoint) ? new LoginUrlAuthenticationEntryPoint("/login") : entryPoint,
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML, MediaType.APPLICATION_JSON)))
                // 注入redis存储SecurityContext, 防止分布式情境下不同node之间数据不同步的问题
                .securityContext(Objects.nonNull(redisSecurityContextRepository) ? contextConfig -> contextConfig.securityContextRepository(redisSecurityContextRepository) : Customizer.withDefaults())
        ;
    }

    /**
     * 优先从cookie中取，因为登陆成功后cookie中的jsessionid会被更新，导致与request中的不一致
     * 找不到去请求参数中找，找不到获取当前session的id
     *  2023-07-11新增逻辑：获取当前session的sessionId
     *
     * @param request 当前请求
     * @return 随机字符串(sessionId)，这个字符串本来是前端生成，现在改为后端获取的sessionId
     */
    public static String getTokenKey(HttpServletRequest request) {
        String tokenKey = Objects.isNull(request.getAttribute(ACCESS_TOKEN)) ? null : String.valueOf(request.getAttribute(ACCESS_TOKEN));
        if (StringUtils.hasText(tokenKey)) {
            return tokenKey;
        }
        if (ArrayUtils.isNotEmpty(request.getCookies())) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equalsIgnoreCase(ACCESS_TOKEN)) {
                    tokenKey = cookie.getValue();
                }
            }
        }
        if (!StringUtils.hasText(tokenKey)) {
            tokenKey = request.getHeader(ACCESS_TOKEN);
            if (!StringUtils.hasText(tokenKey)) {
                tokenKey = request.getParameter(ACCESS_TOKEN);
            }
        }
        log.info("获取jsessionid: {}", tokenKey);
        return tokenKey;
    }

    public static void copyRequestCookieToResponse(HttpServletRequest request, HttpServletResponse response) {
        if (!ArrayUtils.isEmpty(request.getCookies())) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(ACCESS_TOKEN)) {
                    response.addCookie(cookie);
                    cookie.setHttpOnly(false);
                }
            }
        }
    }
}
