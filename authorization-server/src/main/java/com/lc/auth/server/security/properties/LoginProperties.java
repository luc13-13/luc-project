package com.lc.auth.server.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/2 17:43
 * @version : 1.0
 */
@Data
@ConfigurationProperties(prefix = SysSecurityProperties.PREFIX + ".login")
public class LoginProperties {
    /**
     * 登录页面地址，前后端分离项目需要填写绝对路径, 对应着前端页面的地址
     */
    private String loginPage = "/login";

    /**
     * 后端处理登录的接口
     */
    private String loginApi = "/login";

    /**
     * 登陆成功后默认跳转的地址
     */
    private String defaultSuccessUrl = "/home";

    /**
     * 登陆成功后是否总是跳转到默认地址。<br/>true: 总是跳转到defaultSuccessUrl; <br/>false: 跳转到登陆前访问的地址
     */
    private boolean alwaysUseDefaultSuccessUrl = true;

    /**
     * 登出地址
     */
    private String logoutUrl = "/logout";

    /**
     * 登出后跳转地址
     */
    private String logoutSuccessUrl = "/index";

    private String defaultOauth2SuccessUrl = "/oauth2/login/success";
}
