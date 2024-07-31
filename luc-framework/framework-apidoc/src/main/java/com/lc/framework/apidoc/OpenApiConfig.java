package com.lc.framework.apidoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2024/6/15 20:05
 * @version : 1.0
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(ApiDocInfoProperties.class)
public class OpenApiConfig {
    /**
     * OAuth2 认证 endpoint
     */
    @Value("${spring.security.oauth2.authorizationserver.token-uri:#{null}}")
    private String tokenUrl;

    /**
     * API 文档信息属性
     */
    private final ApiDocInfoProperties apiDocInfoProperties;


    /**
     * OpenAPI 配置（元信息、安全协议）
     */
    @Bean
    public OpenAPI apiInfo() {
        OpenAPI openAPI = new OpenAPI();
        Info info = new Info().title(apiDocInfoProperties.getTitle())
                .version(apiDocInfoProperties.getVersion())
                .description(apiDocInfoProperties.getDescription());// 接口文档信息(不重要)

        if (Objects.nonNull(apiDocInfoProperties.getContact())) {
            info = info.contact(new Contact()
                    .name(apiDocInfoProperties.getContact().getName())
                    .url(apiDocInfoProperties.getContact().getUrl())
                    .email(apiDocInfoProperties.getContact().getEmail()));
        }
        if (Objects.nonNull(apiDocInfoProperties.getLicense())) {
            info = info.license(new License().name(apiDocInfoProperties.getLicense().getName())
                    .url(apiDocInfoProperties.getLicense().getUrl()));
        }
        Components components = null;
        if (StringUtils.hasText(tokenUrl)) {
            openAPI = openAPI// 接口全局添加 Authorization 参数
                    .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION));
            components = new Components()
                    .addSecuritySchemes(HttpHeaders.AUTHORIZATION,
                            new SecurityScheme()
                                    // OAuth2 授权模式
                                    .type(SecurityScheme.Type.OAUTH2)
                                    .name(HttpHeaders.AUTHORIZATION)
                                    .flows(new OAuthFlows()
                                            .password(
                                                    new OAuthFlow()
                                                            .tokenUrl(tokenUrl)
                                                            .refreshUrl(tokenUrl))
                                    )
                                    // 安全模式使用Bearer令牌（即JWT）
                                    .in(SecurityScheme.In.HEADER)
                                    .scheme("Bearer")
                                    .bearerFormat("JWT")
                    );
        }
        return openAPI.info(info).components(components);

    }
}
