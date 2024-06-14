package com.lc.framework.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/3/20 16:43
 */
@Data
@ConfigurationProperties(prefix = "sys.security")
public class SysSecurityProperties {
    /**
     * 登录页面地址，前后端分离项目需要填写绝对路径, 对应着前端页面的地址
      */
    private String loginPage;

    /**
     * 后端处理登录的接口
      */
    private String loginApi;

    private String deviceActiveUrl;

    /**
     * 系统session前缀
     */
    private String sessionPrefix;

    /**
     * 系统session过期时间
     */
    private Duration sessionTimeout;

    /**
     * 系统创建的cookie生命周期
     */
    private Duration cookieMaxAge;

    /**
     * token前缀
     */
    private String cachePrefix;

    /**
     * 权限缓存名称
     */
    private String authorizationCacheName;

    /**
     * 认证缓存名称
     */
    private String authenticationCacheName;

    private boolean enableFeignInterceptor = false;
}
