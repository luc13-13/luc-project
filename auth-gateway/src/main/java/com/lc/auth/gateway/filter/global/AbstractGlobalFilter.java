package com.lc.auth.gateway.filter.global;

import com.lc.auth.gateway.config.properties.LucGatewayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.lc.framework.core.mvc.RequestHeaderConstants.ATTRIBUTE_IGNORE_FILTER;


/**
 * <pre>
 *  全局过滤器的模板类， 对全局过滤器的过滤方法进行模板化开发，所有子类的全局过滤器只需要实现
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-31 16:14
 */
@Slf4j
public abstract class AbstractGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    protected LucGatewayProperties lucGatewayProperties;

    protected final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * <pre>
     * 模板方法， 将所有全局过滤器的通用校验逻辑进行抽离， 各个子类全局过滤器只需要实现
     * 步骤(1): 判断是否可以放行
     *   默认的方法是：判断url是否在白名单内， 为避免重复进行白名单路径匹配，首次匹配后为exchange设置属性ATTRIBUTE_IGNORE_FILTER为true， 执行步骤(1)时先判断exchange是否有该属性值， 如果有则无需进行后续的路径匹配
     *   对于无需进行白名单校验的子类, 如{@link RequestHeaderGlobalFilter}，则重写{@link AbstractGlobalFilter#isIgnore}方法
     * 步骤(2): 执行过滤逻辑
     * </pre>
     * @author Lu Cheng
     * @create 2023/9/4
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1、判断是否可以放行
        if (isIgnore(exchange)) {
            return chain.filter(exchange);
        }
        // 2、执行子类filter的过滤逻辑
        return doFilter(exchange, chain);
    }

    public abstract Mono<Void> doFilter(ServerWebExchange exchange, GatewayFilterChain chain);

    public boolean isIgnore(ServerWebExchange exchange) {
        Object isIgnore = exchange.getAttribute(ATTRIBUTE_IGNORE_FILTER);
        if(Objects.isNull(isIgnore)) {
            return skip(exchange);
        } else {
            return Boolean.parseBoolean(String.valueOf(isIgnore));
        }
    }
    /**
     * 获取请求头中的参数
     * @param request http请求
     * @param headerName 目标请求头
     * @return 返回空字符串说明request中没有目标请求头， 否则返回目标请求头的值
     */
    protected String getHeaderValue(ServerHttpRequest request, String headerName){
        HttpHeaders headers = request.getHeaders();
        // 请求头为空则返回空字符串
        if(headers.isEmpty()) {
            return "";
        }
        String headerValue = headers.getFirst(headerName);
        log.info("获取header{}:{}",headerName,headerValue);
        return headerValue;
        // 如果请求头中没有headerName, 则去请求参数中查找
    }

    // protected方法只能在同一个包下被其他类非子类调用， 因此改为public方法
    public static String getRequestUrl(ServerHttpRequest request) {
        String url = request.getPath().value();
        log.info("path.value: {}",url);
        log.info("uri.path: {}",request.getURI().getPath());
        return url;
    }

    /**
     * 判断是否被跳过
     * @param exchange 过滤链路中封装这request
     * @return true表示跳过该filter， false表示不跳过
     */
    protected boolean skip(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String url = AbstractGlobalFilter.getRequestUrl(request);
        log.info("判断是否要跳过: {}", url);
        if (!CollectionUtils.isEmpty(lucGatewayProperties.getWhiteUrl())) {
            for(String whiteUrl : lucGatewayProperties.getWhiteUrl()) {
                if (whiteUrl.equals(url) || antPathMatcher.match(whiteUrl, url)) {
                    // 在exchange中设置属性， 防止每个后续filter都进行路径解析判断是否跳过
                    exchange.getAttributes().put(ATTRIBUTE_IGNORE_FILTER, true);
                    log.info("白名单放行：{}， 设置exchange属性: {}", url, exchange.getAttribute(ATTRIBUTE_IGNORE_FILTER));
                    return true;
                } else {
                    exchange.getAttributes().put(ATTRIBUTE_IGNORE_FILTER, false);
                }
            }
        }
        return false;
    }
}
