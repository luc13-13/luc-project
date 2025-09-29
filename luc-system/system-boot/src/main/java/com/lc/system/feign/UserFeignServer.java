package com.lc.system.feign;

import com.lc.framework.core.constants.RequestHeaderConstants;
import com.lc.framework.core.mvc.WebResult;
import com.lc.system.api.SysUserDetailDTO;
import com.lc.system.domain.dto.UserDTO;
import com.lc.system.service.SysUserService;
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

    private final SysUserService sysUserService;

    public UserFeignServer(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/detail")
    public WebResult<SysUserDetailDTO> getUserDetail(@RequestParam("username") String username) {
        SysUserDetailDTO dto = sysUserService.getSysUserDetail(username);
        log.info("获取用户详情: {}", dto);
        return WebResult.success(dto);
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    public WebResult<UserDTO> getUserInfo(HttpServletRequest request) {
        String username = request.getHeader(RequestHeaderConstants.USER_NAME);
        log.info("获取user信息, {}", username);
        return WebResult.success(UserDTO.builder()
                .username(username)
                .permissions(Arrays.asList("system:user:query", "system:menu:query", "system:menu:add", "system:menu:edit", "system:menu:delete"))
                .build());
    }

    @Operation(summary = "获取用户权限")
    @GetMapping("/permissions")
    public WebResult<List<String>> getUserPermissions(@RequestParam("username") String username) {
        log.info("获取用户权限：{}", username);
        return WebResult.success(Arrays.asList("system:user:query", "system:menu:query", "system:menu:add", "system:menu:edit", "system:menu:delete"));
    }
}
