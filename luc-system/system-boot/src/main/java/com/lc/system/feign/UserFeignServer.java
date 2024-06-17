package com.lc.system.feign;

import com.lc.framework.core.mvc.WebResult;
import com.lc.system.domain.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/28 15:50
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserFeignServer {

    @GetMapping("/detail")
    public WebResult<String> getUserDetail(@RequestParam("username") String username) {
        log.info("获取用户详情");
        return WebResult.success();
    }

    @GetMapping("/info")
    public WebResult<UserDTO> getUserInfo(String username) {
        log.info("获取user信息, {}", username);
        return WebResult.successData(UserDTO.builder()
                .username(username)
                .permissions(Arrays.asList("system:user:query", "system:menu:query", "system:menu:add", "system:menu:edit"))
                .build());
    }

    @GetMapping("/permissions")
    public WebResult<List<String>> getUserPermissions(String username) {
        log.info("获取用户权限：{}", username);
        return WebResult.successData(Arrays.asList("system:user:query", "system:menu:query", "system:menu:add", "system:menu:edit"));
    }
}
