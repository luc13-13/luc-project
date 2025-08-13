package com.lc.auth.server.web;

import com.lc.framework.core.mvc.WebResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2前端集成控制器
 * 专门为vben-ele等前端框架提供OAuth2授权码流程支持
 *
 * @author Lu Cheng
 * @date 2025/8/8
 */
@Slf4j
@RestController
@RequestMapping("/oauth2")
@AllArgsConstructor
public class OAuth2FrontendController {

    private final RegisteredClientRepository registeredClientRepository;
    private final JwtEncoder jwtEncoder;

    /**
     * 前端发起OAuth2授权请求
     * 用于vben-ele等SPA应用
     */
    @GetMapping("/authorize/frontend")
    public void authorizeForFrontend(
            @RequestParam(value = "client_id", defaultValue = "vben-ele-client") String clientId,
            @RequestParam(value = "redirect_uri", defaultValue = "http://127.0.0.1/oauth2/callback") String redirectUri,
            @RequestParam(value = "scope", defaultValue = "openid profile read write") String scope,
            @RequestParam(value = "state", required = false) String state,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        log.info("前端OAuth2授权请求 - clientId: {}, redirectUri: {}, scope: {}, state: {}",
                clientId, redirectUri, scope, state);

        // 构建标准OAuth2授权URL
        StringBuilder authUrl = new StringBuilder("/oauth2/authorize");
        authUrl.append("?response_type=code");
        authUrl.append("&client_id=").append(clientId);
        authUrl.append("&redirect_uri=").append(redirectUri);
        authUrl.append("&scope=").append(scope.replace(" ", "%20"));

        if (state != null && !state.isEmpty()) {
            authUrl.append("&state=").append(state);
        }

        log.info("重定向到OAuth2授权端点: {}", authUrl);
        response.sendRedirect(authUrl.toString());
    }

    /**
     * 获取OAuth2授权URL
     * 返回JSON格式，供前端使用
     */
    @GetMapping("/authorize-url")
    public WebResult<Map<String, Object>> getAuthorizeUrl(
            @RequestParam(value = "client_id", defaultValue = "vben-ele-client") String clientId,
            @RequestParam(value = "redirect_uri", defaultValue = "http://127.0.0.1/oauth2/callback") String redirectUri,
            @RequestParam(value = "scope", defaultValue = "openid profile read write") String scope,
            @RequestParam(value = "state", required = false) String state) {

        log.info("获取OAuth2授权URL - clientId: {}, redirectUri: {}, scope: {}", clientId, redirectUri, scope);

        // 构建授权URL
        StringBuilder authUrl = new StringBuilder("http://127.0.0.1:8889/oauth2/authorize");
        authUrl.append("?response_type=code");
        authUrl.append("&client_id=").append(clientId);
        authUrl.append("&redirect_uri=").append(redirectUri);
        authUrl.append("&scope=").append(scope.replace(" ", "%20"));

        if (state != null && !state.isEmpty()) {
            authUrl.append("&state=").append(state);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("authorizeUrl", authUrl.toString());
        result.put("clientId", clientId);
        result.put("redirectUri", redirectUri);
        result.put("scope", scope);
        result.put("state", state);

        return WebResult.success(result);
    }

    /**
     * 检查用户登录状态
     */
    @GetMapping("/check-login")
    public WebResult<Map<String, Object>> checkLoginStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> result = new HashMap<>();

        if (authentication != null && authentication.isAuthenticated()
            && !"anonymousUser".equals(authentication.getName())) {
            result.put("isLoggedIn", true);
            result.put("username", authentication.getName());
            result.put("authorities", authentication.getAuthorities());
            return WebResult.success(result);
        } else {
            result.put("isLoggedIn", false);
            result.put("loginUrl", "http://127.0.0.1:8889/login");
            return WebResult.success(result);
        }
    }

    /**
     * 前端OAuth2登录入口
     * 检查登录状态，如果未登录则返回登录URL，如果已登录则返回授权URL
     */
    @PostMapping("/login")
    public WebResult<Map<String, Object>> oauth2Login(
            @RequestParam(value = "client_id", defaultValue = "vben-ele-client") String clientId,
            @RequestParam(value = "redirect_uri", defaultValue = "http://127.0.0.1/oauth2/callback") String redirectUri,
            @RequestParam(value = "scope", defaultValue = "openid profile read write") String scope,
            @RequestParam(value = "state", required = false) String state) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> result = new HashMap<>();

        if (authentication != null && authentication.isAuthenticated()
            && !"anonymousUser".equals(authentication.getName())) {
            // 用户已登录，返回授权URL
            StringBuilder authUrl = new StringBuilder("http://127.0.0.1:8889/oauth2/authorize");
            authUrl.append("?response_type=code");
            authUrl.append("&client_id=").append(clientId);
            authUrl.append("&redirect_uri=").append(redirectUri);
            authUrl.append("&scope=").append(scope.replace(" ", "%20"));

            if (state != null && !state.isEmpty()) {
                authUrl.append("&state=").append(state);
            }

            result.put("needLogin", false);
            result.put("authorizeUrl", authUrl.toString());
            result.put("username", authentication.getName());

            return WebResult.success(result);
        } else {
            // 用户未登录，返回登录URL
            String loginUrl = "http://127.0.0.1:8889/login?redirect=" +
                             java.net.URLEncoder.encode(redirectUri, java.nio.charset.StandardCharsets.UTF_8);

            result.put("needLogin", true);
            result.put("loginUrl", loginUrl);

            return WebResult.success(result);
        }
    }

    /**
     * 获取客户端信息
     */
    @GetMapping("/client-info/{clientId}")
    public WebResult<Map<String, Object>> getClientInfo(@PathVariable String clientId) {
        try {
            var registeredClient = registeredClientRepository.findByClientId(clientId);

            if (registeredClient == null) {
                return WebResult.bizError("客户端不存在: " + clientId);
            }

            Map<String, Object> clientInfo = new HashMap<>();
            clientInfo.put("clientId", registeredClient.getClientId());
            clientInfo.put("clientName", registeredClient.getClientName());
            clientInfo.put("redirectUris", registeredClient.getRedirectUris());
            clientInfo.put("scopes", registeredClient.getScopes());
            clientInfo.put("authorizationGrantTypes", registeredClient.getAuthorizationGrantTypes());

            return WebResult.success(clientInfo);
        } catch (Exception e) {
            log.error("获取客户端信息失败", e);
            return WebResult.bizError("获取客户端信息失败: " + e.getMessage());
        }
    }
    /**
     * 使用会话令牌（luc-auth-token）申请 AccessToken
     * 前端表单登录成功后，用收到的 luc-auth-token 调用此接口获取 AccessToken
     */
    @PostMapping("/token")
    public WebResult<Map<String, Object>> getAccessToken(HttpServletRequest request) {
        try {
            // 从请求中提取 luc-auth-token
            String sessionToken = request.getHeader("luc-auth-token");
            if (sessionToken == null) {
                Cookie cookie = org.springframework.web.util.WebUtils.getCookie(request, "luc-auth-token");
                if (cookie != null) {
                    sessionToken = cookie.getValue();
                }
            }

            if (!org.springframework.util.StringUtils.hasText(sessionToken)) {
                return WebResult.bizError("缺少会话令牌");
            }

            // 验证会话令牌并获取用户信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getName())) {
                return WebResult.bizError("会话令牌无效或已过期");
            }

            // 生成 AccessToken（JWT）
            Instant now = Instant.now();
            Instant exp = now.plusSeconds(7200); // 2小时有效期

            org.springframework.security.oauth2.jwt.JwtClaimsSet claims =
                org.springframework.security.oauth2.jwt.JwtClaimsSet.builder()
                    .subject(authentication.getName())
                    .claim("sid", sessionToken) // 会话标识
                    .claim("scope", "read write") // 权限范围
                    .issuedAt(now)
                    .expiresAt(exp)
                    .build();

            String accessToken = this.jwtEncoder.encode(
                org.springframework.security.oauth2.jwt.JwtEncoderParameters.from(
                    org.springframework.security.oauth2.jwt.JwsHeader.with(
                        org.springframework.security.oauth2.jose.jws.SignatureAlgorithm.RS256
                    ).build(),
                    claims
                )
            ).getTokenValue();

            Map<String, Object> result = new HashMap<>();
            result.put("accessToken", accessToken);
            result.put("tokenType", "Bearer");
            result.put("expiresIn", 7200);

            log.info("为用户 {} 签发 AccessToken", authentication.getName());
            return WebResult.success(result);

        } catch (Exception e) {
            log.error("签发 AccessToken 失败", e);
            return WebResult.bizError("签发 AccessToken 失败: " + e.getMessage());
        }
    }
}
