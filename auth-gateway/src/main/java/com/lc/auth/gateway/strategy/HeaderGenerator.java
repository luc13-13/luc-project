package com.lc.auth.gateway.strategy;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

/**
 * <pre>
 * 非阻塞式的请求头填充策略
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-09-04 10:10
 */
public interface HeaderGenerator extends InitializingBean {
    void generate(ServerWebExchange exchange, HttpHeaders httpHeaders);
}
