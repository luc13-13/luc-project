package com.lc.framework.apidoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.stream.Collectors;

import static com.lc.framework.core.constants.RequestHeaderConstants.ACCESS_TOKEN;
import static com.lc.framework.core.constants.RequestHeaderConstants.KNIFE4J_TOKEN_KEY;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2024/6/15 20:05
 * @version : 1.0
 */
@Slf4j
@ConditionalOnExpression("${springdoc.api-docs.enabled:true} and !'${spring.profiles.active}'.equals('pro')")
@RequiredArgsConstructor
@EnableConfigurationProperties(ApiDocInfoProperties.class)
public class OpenApiConfig {

    /**
     * API 文档信息属性
     */
    private final ApiDocInfoProperties apiDocInfoProperties;


    /**
     * 1、增加请求头X-Access-Token
     * 2、为每个请求增加Authorization
     */
    @Bean
    public GlobalOperationCustomizer globalOperationCustomizer() {
        return (operation, handlerMethod) -> operation
                .addParametersItem(new HeaderParameter().required(true).name(ACCESS_TOKEN).schema(new StringSchema()._default(KNIFE4J_TOKEN_KEY)));
    }

    /**
     * debug时自动填充Authorization
     */
    @Bean
    public GlobalOpenApiCustomizer globalOpenApiCustomizer() {
        return openApi -> {
            if (openApi.getPaths() != null) {
                openApi.getPaths().forEach((s, pathItem) -> pathItem
                        .readOperations()
                        .forEach(operation -> operation
                                .security(openApi.getSecurity()))
                );
            }
        };
    }


    /**
     * OpenAPI 配置（元信息、安全协议）
     */
    @Bean
    public OpenAPI apiInfo(SpringDocConfigProperties properties) {
        log.info("apidoc自动装配, 扫描路径：{}, tokenUrl: {}", properties.getGroupConfigs().stream().map(SpringDocConfigProperties.GroupConfig::getPackagesToScan).collect(Collectors.toList()), apiDocInfoProperties.getTokenUrl());

        OpenAPI openAPI = new OpenAPI();
        Info info = new Info().title(apiDocInfoProperties.getTitle())
                .version(apiDocInfoProperties.getVersion())
                .description(apiDocInfoProperties.getDescription());

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
        if (StringUtils.hasText(apiDocInfoProperties.getTokenUrl())) {
            log.info("framework-apidoc开启Authorization");
            openAPI = openAPI.addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION));
            components = new Components()
                    // 设置获取Authorization方式
                    .addSecuritySchemes(HttpHeaders.AUTHORIZATION,
                            new SecurityScheme()
                                    // OAuth2 授权模式
                                    .type(SecurityScheme.Type.OAUTH2)
                                    // SecuritySchema名称: Authorization
                                    .name(HttpHeaders.AUTHORIZATION)
                                    // SecuritySchema携带方式: 请求头
                                    .in(SecurityScheme.In.HEADER)
                                    // SecuritySchema类型: Bearer
                                    .scheme("Bearer")
                                    // Bearer格式: JWT
                                    .bearerFormat("JWT")
                                    // SecuritySchema获取流程: OAuth方式，获取授权码->授权码获取token
                                    .flows(new OAuthFlows()
                                            .authorizationCode(new OAuthFlow()
                                                    .authorizationUrl(apiDocInfoProperties.getAuthorizationUrl())
                                                    .tokenUrl(apiDocInfoProperties.getTokenUrl())
                                                    .refreshUrl(apiDocInfoProperties.getTokenUrl())
                                            )
                                    )
                    )
                    .addParameters("myHeader1", new Parameter().in("header").schema(new StringSchema()).name("myHeader1")).addHeaders("myHeader2", new Header().description("myHeader2 header").schema(new StringSchema()));
        }
        return openAPI.info(info).components(components);
    }
}
