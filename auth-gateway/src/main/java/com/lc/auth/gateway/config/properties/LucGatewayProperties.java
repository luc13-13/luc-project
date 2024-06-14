package com.lc.auth.gateway.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import java.util.List;

/**
 * @desc 网关系统配置
 * @author : Lu Cheng
 * @date 2022/10/26 21:07
 * @version : 1.0
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "luc.gateway")
public class LucGatewayProperties {
    /**
     * 可以跳过验证的请求url, 例如/token/**, /login, /captcha, /v3/api-code
     *
     */
    private List<String> whiteUrl;

    /**可以跳过验证的用户名*/
    private List<String> whiteResource;
}
