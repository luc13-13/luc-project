package com.lc.system.feign;

import com.lc.framework.core.mvc.WebResult;
import com.lc.system.domain.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@Tag(name = "Feign接口——用户信息相关接口")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserFeignServer {

    @Operation(summary = "获取用户详情")
    @GetMapping("/detail")
    public WebResult<String> getUserDetail() {
        log.info("获取用户详情");
        return WebResult.success();
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    public WebResult<UserDTO> getUserInfo(HttpServletRequest request) {
        String username = request.getHeader("X-Username");
        log.info("获取user信息, {}", username);
        return WebResult.success(UserDTO.builder()
                .username(username)
                .permissions(Arrays.asList("system:user:query", "system:menu:query", "system:menu:add", "system:menu:edit"))
                .build());
    }

    @Operation(summary = "获取用户权限")
    @GetMapping("/permissions")
    public WebResult<List<String>> getUserPermissions(@RequestParam("username") String username) {
        log.info("获取用户权限：{}", username);
        return WebResult.success(Arrays.asList("system:user:query", "system:menu:query", "system:menu:add", "system:menu:edit"));
    }
}
