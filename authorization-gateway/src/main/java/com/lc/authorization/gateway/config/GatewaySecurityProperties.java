package com.lc.authorization.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 网关安全配置属性
 *
 * @author Lu Cheng
 * @date 2025/8/15
 */
@Data
@Component
@ConfigurationProperties(prefix = "gateway.security")
public class GatewaySecurityProperties {

    /**
     * 白名单路径，这些路径不需要JWT认证
     */
    private List<String> whitePaths = List.of(
            "/health",
            "/actuator/**",
            "/gateway/health",
            "/auth-server/login",
            "/auth-server/register",
            "/auth-server/oauth2/**",
            "/auth-server/.well-known/**",
            "/auth-server/jwks",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/favicon.ico"
    );

    /**
     * JWT 配置
     */
    private Jwt jwt = new Jwt();

    @Data
    public static class Jwt {
        /**
         * JWT 发行者 URI
         */
        private String issuerUri = "http://127.0.0.1:8889";
        
        /**
         * JWT 验证失败时是否记录详细日志
         */
        private boolean logFailures = true;
        
        /**
         * 是否启用 JWT 时钟偏移容忍
         */
        private boolean clockSkewEnabled = true;
        
        /**
         * 时钟偏移容忍时间（秒）
         */
        private long clockSkewSeconds = 60;
    }
}
