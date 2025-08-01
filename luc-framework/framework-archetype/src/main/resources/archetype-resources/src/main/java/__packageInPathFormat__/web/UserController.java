package ${package}.web;

import ${package}.domain.User;
import ${package}.service.UserService;
import com.lc.framework.core.mvc.WebResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * <pre>
 * 用户管理控制器
 * </pre>
 *
 * @author ${author}
 * @date ${date}
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "获取用户列表", description = "获取所有启用状态的用户列表")
    public WebResult<List<User>> getUsers() {
        log.info("获取用户列表");
        List<User> users = userService.findActiveUsers();
        return WebResult.success(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取用户", description = "根据用户ID获取用户详细信息")
    public WebResult<User> getUserById(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long id) {
        log.info("根据ID获取用户: {}", id);
        User user = userService.getById(id);
        if (user == null) {
            return WebResult.error("用户不存在");
        }
        return WebResult.success(user);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名获取用户", description = "根据用户名获取用户信息")
    public WebResult<User> getUserByUsername(
            @Parameter(description = "用户名", required = true)
            @PathVariable String username) {
        log.info("根据用户名获取用户: {}", username);
        User user = userService.findByUsername(username);
        if (user == null) {
            return WebResult.error("用户不存在");
        }
        return WebResult.success(user);
    }

    @PostMapping
    @Operation(summary = "创建用户", description = "创建新用户")
    public WebResult<String> createUser(
            @Parameter(description = "用户信息", required = true)
            @Valid @RequestBody User user) {
        log.info("创建用户: {}", user.getUsername());
        try {
            boolean success = userService.createUser(user);
            if (success) {
                return WebResult.success("用户创建成功");
            } else {
                return WebResult.error("用户创建失败");
            }
        } catch (Exception e) {
            log.error("创建用户失败", e);
            return WebResult.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户", description = "更新用户信息")
    public WebResult<String> updateUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "用户信息", required = true)
            @Valid @RequestBody User user) {
        log.info("更新用户: {}", id);
        user.setId(id);
        boolean success = userService.updateUser(user);
        if (success) {
            return WebResult.success("用户更新成功");
        } else {
            return WebResult.error("用户更新失败");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "删除指定用户")
    public WebResult<String> deleteUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long id) {
        log.info("删除用户: {}", id);
        boolean success = userService.deleteUser(id);
        if (success) {
            return WebResult.success("用户删除成功");
        } else {
            return WebResult.error("用户删除失败");
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新用户状态", description = "启用或禁用用户")
    public WebResult<String> updateUserStatus(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "状态：0-禁用，1-启用", required = true)
            @RequestParam Integer status) {
        log.info("更新用户状态: userId={}, status={}", id, status);
        boolean success = userService.updateUserStatus(id, status);
        if (success) {
            return WebResult.success("用户状态更新成功");
        } else {
            return WebResult.error("用户状态更新失败");
        }
    }
}
