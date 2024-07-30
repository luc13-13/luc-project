package com.lc.authorization.server.feign;

import com.lc.framework.core.mvc.WebResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "luc-system")
public interface InstitutionCenterFeignClient {
    @GetMapping("/institution/info")
    WebResult<String> getUserInfo();

    @GetMapping("/user/detail")
    WebResult<String> getUserDetail(@RequestParam("username") String username);
}
