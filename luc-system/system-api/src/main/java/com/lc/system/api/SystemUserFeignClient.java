package com.lc.system.api;

import com.lc.framework.core.mvc.WebResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025/3/5 11:05
 */
@FeignClient(value = "luc-system")
public interface SystemUserFeignClient {

    @GetMapping("/institution/info")
    WebResult<String> getUserInfo();

    @GetMapping("/user/detail")
    WebResult<SysUserDetailDTO> getUserDetail(@RequestParam("username") String username);

}
