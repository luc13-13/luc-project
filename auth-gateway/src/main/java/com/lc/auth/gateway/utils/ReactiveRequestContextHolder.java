package com.lc.auth.gateway.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * @author : Lu Cheng
 * @date : 2022/11/1 21:03
 * @version : 1.0
 */
@Slf4j
public class ReactiveRequestContextHolder {
    // 异步方法内需要将主线程的request进行put
    /**
     * public void method(){
     *     Future<String> res = CompletableFuture.supplyAsync(() -> {
     *         // 这里 request 是从主线程获取到的，在子线程中放入后，FeignCookieInterceptor才可以获取到该子线程需要的request
     *         ReactiveRequestContextHolder.put(request);
     *         return feign.api();
     *     })
     * }
     */
    public static ThreadLocal<ServerHttpRequest> LOCAL_REQUEST = new ThreadLocal<>();
    public ReactiveRequestContextHolder() {
    }

    public static void put(ServerHttpRequest request) {
        log.debug("向thread local添加request:{}, 当前线程: {}", request, Thread.currentThread().getId());
        LOCAL_REQUEST.set(request);
    }

    /**
     * get之后立即删除ThreadLocal中当前变量，避免OOM（因为ThreadLocalMap中的Entry为弱引用，被垃圾回收后会导致value失去引用）
     * @author Lu Cheng
     * @date 2023/7/25
     */
    public static ServerHttpRequest get() {
        ServerHttpRequest request = LOCAL_REQUEST.get();
        log.debug("从thread local获取request:{}, 当前线程:{}",request, Thread.currentThread().getId());
        LOCAL_REQUEST.remove();
        return request;
    }
}
