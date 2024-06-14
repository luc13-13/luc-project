package com.lc.framework.security;


import com.lc.framework.security.core.properties.SysSecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/3/20 16:55
 */
@AutoConfiguration
@EnableConfigurationProperties(SysSecurityProperties.class)
public class SecurityAutoConfiguration {

}
