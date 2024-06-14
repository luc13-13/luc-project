package com.lc.auth.gateway.filter.global;

import com.lc.auth.gateway.enums.RequiredHeaderEnum;
import com.lc.auth.gateway.factory.HeaderGeneratorFactory;
import com.lc.auth.gateway.strategy.HeaderGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *  请求头过滤器，判断请求头中是否包含必要的属性：{@link RequiredHeaderEnum}
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-31 16:14
 */
@Slf4j
@Component
public class RequestHeaderGlobalFilter extends AbstractGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public boolean isIgnore(ServerWebExchange exchange) {
        return false;
    }

    @Override
    public Mono<Void> doFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest().mutate().headers(httpHeaders -> {
            HeaderGenerator headerGenerator;
            List<String> accessControlAllowHeaders = new ArrayList<>();
            for(RequiredHeaderEnum requiredHeader : RequiredHeaderEnum.values()) {
                accessControlAllowHeaders.add(requiredHeader.getCode());
                headerGenerator = HeaderGeneratorFactory.getByType(requiredHeader);
                // generator实现类自行判断是否进行请求头填充
                if (headerGenerator != null) {
                    headerGenerator.generate(exchange, httpHeaders);
                }
            }
//            log.info("设置AccessControlAllowHeaders：{}", accessControlAllowHeaders);
            httpHeaders.setAccessControlAllowHeaders(accessControlAllowHeaders);
        }).build();
        return chain.filter(exchange.mutate().request(request).build());
    }
}
