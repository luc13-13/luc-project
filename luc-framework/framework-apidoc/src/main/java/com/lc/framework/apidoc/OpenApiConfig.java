package com.lc.framework.apidoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
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
//@AutoConfiguration
//@ConditionalOnExpression("${springdoc.api-docs.enabled:true} and !'${spring.profiles.active}'.equals('pro')")
@RequiredArgsConstructor
@EnableConfigurationProperties(ApiDocInfoProperties.class)
public class OpenApiConfig {


    /**
     * OAuth2 认证 endpoint
     */
//    @Value("${spring.security.oauth2.authorizationserver.token-uri:#{'http://127.0.0.1:8889/oauth2/token'}}")
    private String tokenUrl = "http://127.0.0.1:8889/oauth2/token";

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
        log.info("apidoc自动装配, 扫描路径：{}", properties.getGroupConfigs().stream().map(SpringDocConfigProperties.GroupConfig::getPackagesToScan).collect(Collectors.toList()));
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
            log.info("framework-apidoc开启Authorization");
            openAPI = openAPI// 接口全局添加 Authorization 参数
                    .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION));
            components = new Components()
                    // 设置获取Authorization方式
                    .addSecuritySchemes(HttpHeaders.AUTHORIZATION,
                            new SecurityScheme()
                                    // OAuth2 授权模式
                                    .type(SecurityScheme.Type.OAUTH2)
                                    .name(HttpHeaders.AUTHORIZATION)
                                    .flows(new OAuthFlows()
                                            .clientCredentials(
                                                    new OAuthFlow()
                                                            .tokenUrl(tokenUrl)
                                                            .refreshUrl(tokenUrl)
                                            )
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
