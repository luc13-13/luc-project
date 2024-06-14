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
 * @date 2023-10-17 09:43
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "sys.security")
public class SysSecurityProperties {

    // 前后端分离项目需要填写绝对路径, 对应着前端页面的地址
    private String loginPage;

    // 后端登录接口
    private String loginApi;

    private String deviceActiveUrl;

    private String sessionPrefix;

    private Duration sessionTimeout;

    private Duration cookieMaxAge;

    private String cachePrefix;

    private String authorizationCacheName;

    private String authenticationCacheName;
}
