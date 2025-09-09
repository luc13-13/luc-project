package com.lc.framework.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/3/20 16:43
 */
@Data
@ConfigurationProperties(prefix = SysSecurityProperties.PREFIX)
public class SysSecurityProperties {
    /**
     * 系统安全相关配置前缀
     */
    public static final String PREFIX = "sys.security";
    /**
     * 认证服务器地址，所有服务保持一致
     */
    private String issuer = "http://127.0.0.1:8809";

    /**
     * 资源服务器的白名单路径
     */
    private List<String> whitePaths;

    private boolean enableRedis = false;

    /**
     * 登陆有效时间
     */
    private Duration tokenTimeToLive = Duration.ofSeconds(3600L);
}
