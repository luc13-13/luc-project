package com.lc.authorization.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025/7/22 15:33
 */
@Slf4j
@Component
public class RefreshRouteEventListener {

    private final RouteDefinitionLocator routeDefinitionLocator;

    private final ReactiveDiscoveryClient reactiveDiscoveryClient;

    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    public RefreshRouteEventListener(RouteDefinitionLocator routeDefinitionLocator, ReactiveDiscoveryClient reactiveDiscoveryClient, SwaggerUiConfigProperties swaggerUiConfigProperties) {
        this.routeDefinitionLocator = routeDefinitionLocator;
        this.reactiveDiscoveryClient = reactiveDiscoveryClient;
        this.swaggerUiConfigProperties = swaggerUiConfigProperties;
    }


    @EventListener(RefreshRoutesEvent.class)
    public void onApplicationEvent() {
        routeDefinitionLocator.getRouteDefinitions()
                .filter(definition -> "lb".equals(definition.getUri().getScheme()))
                .map(RouteDefinition::getId)
                .collect(Collectors.toSet())
                .subscribe(routeDefinitions -> reactiveDiscoveryClient.getServices()
                        .filter(StringUtils::hasText)
                        .filter(routeDefinitions::contains)
                        .map(service -> new AbstractSwaggerUiConfigProperties.SwaggerUrl(service, service + DEFAULT_API_DOCS_URL, service))
                        .collect(Collectors.toSet())
                        .subscribe(urls -> {
                            log.info("refresh swagger services：{}", urls);
                            swaggerUiConfigProperties.setUrls(urls);
                        }));
    }
}
