package com.lc.framework.security;

import com.lc.framework.security.core.properties.SysCorsProperties;
import com.lc.framework.security.core.properties.SysSecurityProperties;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/7/31 10:15
 */
@EnableConfigurationProperties({SysCorsProperties.class, SysSecurityProperties.class})
public class ResourceServerAutoConfiguration {

    @Autowired
    private SysSecurityProperties sysSecurityProperties;

    /**
     * servlet服务的feign拦截器
     */
    @Bean
    @ConditionalOnWebApplication
    public ServletFeignInterceptor servletFeignInterceptor() {
        return new ServletFeignInterceptor();
    }

    /**
     * webflux服务的feign拦截器
     */
    @Bean
    @ConditionalOnNotWebApplication
    public WebFluxFeignInterceptor webFluxFeignInterceptor() {
        return new WebFluxFeignInterceptor();
    }

    public static class ServletFeignInterceptor implements RequestInterceptor {

        @Override
        public void apply(RequestTemplate template) {

        }
    }

    public static class WebFluxFeignInterceptor implements RequestInterceptor {

        @Override
        public void apply(RequestTemplate template) {

        }
    }
}

