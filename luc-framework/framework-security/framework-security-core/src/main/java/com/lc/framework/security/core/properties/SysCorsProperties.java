package com.lc.framework.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/7 15:07
 * @version : 1.0
 */
@Data
@ConfigurationProperties(prefix = SysCorsProperties.PREFIX)
public class SysCorsProperties {
    public static final String PREFIX = SysSecurityProperties.PREFIX + ".cors";

    private boolean enabled = false;

    private List<String> allowedOriginPatterns;

    private List<String> allowedOrigins;

    private List<String> allowedMethods;

    private List<String> allowedHeaders;

    private boolean allowCredentials = false;
}
