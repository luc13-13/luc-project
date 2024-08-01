package com.lc.framework.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/7/29 9:53
 */
@Data
@ConfigurationProperties(prefix = "sys.cors")
public class SysCorsProperties {

    private boolean enabled = false;

    private List<String> allowedOriginPatterns;

    private List<String> allowedOrigins;

    private List<String> allowedMethods;

    private List<String> allowedHeaders;

    private boolean allowCredentials = false;
}
