package com.lc.auth.web;

import com.lc.auth.domain.entity.Tenant;
import com.lc.auth.domain.security.TenantUserDetails;
import com.lc.auth.service.SmsService;
import com.lc.auth.service.TenantService;
import com.lc.framework.core.mvc.WebResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 认证控制器
 * 提供用户名密码登录、短信验证码登录、租户注册等功能
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "认证管理", description = "租户认证相关接口")
public class AuthController {

    private final TenantService tenantService;
    private final SmsService smsService;
    private final StringRedisTemplate redisTemplate;

    @PostMapping("/login/username")
    @Operation(summary = "用户名密码登录", description = "使用用户名和密码进行登录")
    public WebResult<String> loginByUsername(
            @Parameter(description = "用户名", required = true)
            @RequestParam @NotBlank(message = "用户名不能为空") String username,
            @Parameter(description = "密码", required = true)
            @RequestParam @NotBlank(message = "密码不能为空") String password) {

        log.info("用户名密码登录: username={}", username);

        try {
            // 查找租户
            Tenant tenant = tenantService.findByUsername(username);
            if (tenant == null) {
                return WebResult.error(404, "用户不存在");
            }

            // 验证密码
            if (!tenantService.validatePassword(tenant, password)) {
                return WebResult.error(401, "密码错误");
            }

            // 检查账号状态
            if (!Integer.valueOf(1).equals(tenant.getStatus())) {
                return WebResult.error(403, "账号已被禁用或锁定");
            }

            // 生成访问令牌
            String accessToken = generateAccessToken(tenant);

            // 更新登录信息
            tenantService.updateLastLoginInfo(tenant.getTenantId(), getClientIp());

            log.info("用户名密码登录成功: tenantId={}", tenant.getTenantId());
            return WebResult.successData(accessToken);

        } catch (Exception e) {
            log.error("用户名密码登录失败", e);
            return WebResult.error(500, "登录失败: " + e.getMessage());
        }
    }

    @PostMapping("/login/phone")
    @Operation(summary = "手机号验证码登录", description = "使用手机号和验证码进行登录")
    public WebResult<String> loginByPhone(
            @Parameter(description = "手机号", required = true)
            @RequestParam @NotBlank(message = "手机号不能为空") String phone,
            @Parameter(description = "验证码", required = true)
            @RequestParam @NotBlank(message = "验证码不能为空") String code) {

        log.info("手机号验证码登录: phone={}", phone);

        try {
            // 验证验证码
            if (!smsService.verifyCode(phone, code, "login")) {
                return WebResult.error(400, "验证码错误或已过期");
            }

            // 查找租户
            Tenant tenant = tenantService.findByPhone(phone);
            if (tenant == null) {
                return WebResult.error(404, "手机号未注册");
            }

            // 检查账号状态
            if (!Integer.valueOf(1).equals(tenant.getStatus())) {
                return WebResult.error(403, "账号已被禁用或锁定");
            }

            // 生成访问令牌
            String accessToken = generateAccessToken(tenant);

            // 更新登录信息
            tenantService.updateLastLoginInfo(tenant.getTenantId(), getClientIp());

            log.info("手机号验证码登录成功: tenantId={}", tenant.getTenantId());
            return WebResult.successData(accessToken);

        } catch (Exception e) {
            log.error("手机号验证码登录失败", e);
            return WebResult.error(500, "登录失败: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    @Operation(summary = "租户注册", description = "注册新的租户账号")
    public WebResult<String> register(
            @Parameter(description = "用户名", required = true)
            @RequestParam @NotBlank(message = "用户名不能为空") String username,
            @Parameter(description = "密码", required = true)
            @RequestParam @NotBlank(message = "密码不能为空") String password,
            @Parameter(description = "手机号", required = true)
            @RequestParam @NotBlank(message = "手机号不能为空") String phone,
            @Parameter(description = "短信验证码", required = true)
            @RequestParam @NotBlank(message = "验证码不能为空") String code,
            @Parameter(description = "邮箱")
            @RequestParam(required = false) String email) {

        log.info("租户注册: username={}, phone={}, email={}", username, phone, email);

        try {
            // 验证验证码
            if (!smsService.verifyCode(phone, code, "register")) {
                return WebResult.error(400, "验证码错误或已过期");
            }

            // 注册租户
            Tenant tenant = tenantService.registerTenant(username, password, phone, email);

            // 生成访问令牌
            String accessToken = generateAccessToken(tenant);

            log.info("租户注册成功: tenantId={}", tenant.getTenantId());
            return WebResult.successData(accessToken);

        } catch (IllegalArgumentException e) {
            log.warn("租户注册失败: {}", e.getMessage());
            return WebResult.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("租户注册失败", e);
            return WebResult.error(500, "注册失败: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录", description = "清除用户登录状态")
    public WebResult<String> logout(
            @Parameter(description = "访问令牌", required = true)
            @RequestHeader("X-Access-Token") String accessToken) {

        log.info("用户退出登录");

        try {
            // 清除Redis中的令牌
            redisTemplate.delete("access_token:" + accessToken);

            // 清除Security上下文
            SecurityContextHolder.clearContext();

            return WebResult.successData("退出登录成功");

        } catch (Exception e) {
            log.error("退出登录失败", e);
            return WebResult.error(500, "退出登录失败");
        }
    }

    @GetMapping("/userinfo")
    @Operation(summary = "获取用户信息", description = "获取当前登录用户的信息")
    public WebResult<Object> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof TenantUserDetails userDetails) {
            return WebResult.successData(new Object() {
                public final String tenantId = userDetails.getTenantId();
                public final String username = userDetails.getUsername();
                public final String phone = userDetails.getPhone();
                public final String email = userDetails.getEmail();
                public final String realName = userDetails.getRealName();
                public final String avatar = userDetails.getAvatar();
            });
        }
        return WebResult.error(401, "未登录");
    }

    /**
     * 生成访问令牌
     *
     * @param tenant 租户信息
     * @return 访问令牌
     */
    private String generateAccessToken(Tenant tenant) {
        String accessToken = "AT_" + System.currentTimeMillis() + "_" + tenant.getTenantId();
        
        // 将令牌存储到Redis，有效期2小时
        redisTemplate.opsForValue().set("access_token:" + accessToken, tenant.getTenantId(), 2, TimeUnit.HOURS);
        
        // 设置Security上下文
        TenantUserDetails userDetails = new TenantUserDetails(tenant);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        return accessToken;
    }

    /**
     * 获取客户端IP
     *
     * @return 客户端IP
     */
    private String getClientIp() {
        // TODO: 从HttpServletRequest中获取真实IP
        return "127.0.0.1";
    }
}
