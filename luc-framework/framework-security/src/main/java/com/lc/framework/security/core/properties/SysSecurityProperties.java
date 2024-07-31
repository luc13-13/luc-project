package com.lc.framework.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

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

    /**
     * 设备吗登录url
     */
    private String deviceActiveUrl;

    /**
     * 资源服务器的白名单路径
     */
    private List<String> whitePaths;

    /**
     * 是否开启feign拦截器
     */
    private boolean enableFeignInterceptor = false;

    /**
     * 开启OAuth2登录，例如引入微信、gitee登录
     */
    private boolean enableOAuth2Client = false;

//    /**
//     * OAuth2客户端设置
//     */
//    private List<ClientRegistration> oauth2Clients;
}
