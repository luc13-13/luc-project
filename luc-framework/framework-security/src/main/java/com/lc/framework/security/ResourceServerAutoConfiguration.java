package com.lc.framework.security;

import com.lc.framework.security.core.properties.SysCorsProperties;
import com.lc.framework.security.core.properties.SysSecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/7/31 10:15
 */
@AutoConfiguration
@EnableConfigurationProperties({SysCorsProperties.class, SysSecurityProperties.class})
public class ResourceServerAutoConfiguration {
}
