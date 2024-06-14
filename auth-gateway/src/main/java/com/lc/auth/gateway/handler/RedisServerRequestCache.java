package com.lc.auth.gateway.handler;

import com.lc.auth.gateway.utils.WebFluxUtils;
import com.lc.framework.redis.starter.utils.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.util.matcher.*;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;

import static com.lc.framework.core.mvc.RequestHeaderConstants.JSESSIONID;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/8 14:39
 */
@Slf4j
@Component
public class RedisServerRequestCache implements ServerRequestCache {

    @Autowired
    private RedisHelper redisHelper;

    private final ServerWebExchangeMatcher saveRequestMatcher = createDefaultRequestMatcher();
    @Override
    public Mono<Void> saveRequest(ServerWebExchange exchange) {
        return this.saveRequestMatcher.matches(exchange).filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                .map((m) -> exchange.getRequest())
                .doOnNext(request -> {
                    String redirectUri = pathInApplication(request);
                    String jsessionid = WebFluxUtils.getHeaderValue(request, JSESSIONID);
                    redisHelper.set(jsessionid, redirectUri);

                    log.info("jsessionid: {}, saved redirect uri: {}", jsessionid, redirectUri);
                }).then();
    }

    @Override
    public Mono<URI> getRedirectUri(ServerWebExchange exchange) {
        return Mono.justOrEmpty(WebFluxUtils.getHeaderValue(exchange.getRequest(), JSESSIONID)).map(this::createRedirectUri);
    }

    @Override
    public Mono<ServerHttpRequest> removeMatchingRequest(ServerWebExchange exchange) {

        return Mono.just(exchange.getRequest()).map(ServerHttpRequest::getHeaders).doOnNext(headers -> redisHelper.expired(headers.getFirst(JSESSIONID)))
                .thenReturn(exchange.getRequest());
    }

    private URI createRedirectUri(String uri) {
        log.info("获取重定向地址：{}", uri);
            return URI.create(uri);
    }

    private static String pathInApplication(ServerHttpRequest request) {
        String path = request.getPath().pathWithinApplication().value();
        String query = request.getURI().getRawQuery();
        return path + ((query != null) ? "?" + query : "");
    }

    private static ServerWebExchangeMatcher createDefaultRequestMatcher() {
        ServerWebExchangeMatcher get = ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/**");
        ServerWebExchangeMatcher notFavicon = new NegatedServerWebExchangeMatcher(
                ServerWebExchangeMatchers.pathMatchers("/favicon.*"));
        MediaTypeServerWebExchangeMatcher html = new MediaTypeServerWebExchangeMatcher(MediaType.TEXT_HTML);
        html.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        return new AndServerWebExchangeMatcher(get, notFavicon, html);
    }
}
