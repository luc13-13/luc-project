package com.lc.authorization.server.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.time.Duration;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-10-17 09:44
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "sys.token")
public class SysTokenProperties {
    /**密钥*/
    private String secret;

    /**签发JWT的过期时间*/
    private Duration expire;

    /**JWT在redis中的最长时间，用于免密登录与刷新token*/
    private Duration maxDuration;

    /**JWT本身的token头名称*/
    private String tokenHeader;
}
