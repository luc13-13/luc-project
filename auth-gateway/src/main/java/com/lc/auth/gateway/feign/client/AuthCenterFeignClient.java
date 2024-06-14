package com.lc.auth.gateway.feign.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * spring boot 2.7.x 之后，网关调用feignClient服务必须使用异步调用
 */
@FeignClient(name = "auth-center")
public interface AuthCenterFeignClient {
    /**
     * 使用 feignClient的GetMapping方法时，一定要绑定参数，否则feignClient构建参数时将按照顺序赋予默认参数名
     * @param url 请求luj
     * @return "success" or other
     */
    @GetMapping("/feign/checkPerm")
    String checkPermission(@RequestParam(value = "url") String url);
}
