package com.lc.auth.gateway.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

/**
 * 验证码配置， 开启的验证码类型参考{@link com.lc.auth.gateway.enums.CaptchaTypeEnum}
 * <pre>
 *     Example:
 *     luc:
 *       gateway:
 *         captcha:
 *           url: /captcha
 *           enabled-types:
 *             - mixed
 *             - default
 *             - math
 * </pre>
 * @author : Lu Cheng
 * @version : 1.0
 * @date : 2023/9/1 17:30
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "luc.gateway.captcha")
public class CaptchaProperties {
    private String url;

    /**
     * @see com.lc.auth.gateway.enums.CaptchaTypeEnum
     */
    private List<String> enabledTypes;
}
