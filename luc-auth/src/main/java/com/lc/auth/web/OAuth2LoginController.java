package com.lc.auth.web;

import com.lc.auth.domain.entity.Tenant;
import com.lc.auth.domain.security.TenantOAuth2User;
import com.lc.auth.service.TenantService;
import com.lc.auth.service.TenantThirdPartyService;
import com.lc.framework.core.mvc.WebResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <pre>
 * 第三方登录控制器
 * 处理第三方账号登录、绑定、解绑等功能
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Slf4j
@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
@Validated
@Tag(name = "第三方登录", description = "第三方账号登录相关接口")
public class OAuth2LoginController {

    private final TenantThirdPartyService thirdPartyService;
    private final TenantService tenantService;

    @GetMapping("/login/success")
    @Operation(summary = "第三方登录成功回调", description = "处理第三方登录成功后的回调")
    public WebResult<Object> loginSuccess(@AuthenticationPrincipal OAuth2User oAuth2User) {
        log.info("第三方登录成功回调: user={}", oAuth2User.getName());

        try {
            if (oAuth2User instanceof TenantOAuth2User tenantOAuth2User) {
                if (tenantOAuth2User.isBoundToTenant()) {
                    // 已绑定租户，返回用户信息
                    return WebResult.successData(new Object() {
                        public final String status = "bound";
                        public final String tenantId = tenantOAuth2User.getTenantId();
                        public final String username = tenantOAuth2User.getName();
                        public final String provider = tenantOAuth2User.getProvider();
                        public final String message = "登录成功";
                    });
                } else {
                    // 未绑定租户，需要引导用户绑定或注册
                    return WebResult.successData(new Object() {
                        public final String status = "unbound";
                        public final String provider = tenantOAuth2User.getProvider();
                        public final String providerUserId = tenantOAuth2User.getProviderUserId();
                        public final String providerNickname = tenantOAuth2User.getProviderNickname();
                        public final String providerAvatar = tenantOAuth2User.getProviderAvatar();
                        public final String providerEmail = tenantOAuth2User.getProviderEmail();
                        public final String message = "第三方账号未绑定，请选择绑定现有账号或注册新账号";
                    });
                }
            }

            return WebResult.error(500, "第三方登录处理失败");

        } catch (Exception e) {
            log.error("第三方登录成功回调处理失败", e);
            return WebResult.error(500, "登录处理失败: " + e.getMessage());
        }
    }

    @PostMapping("/bind")
    @Operation(summary = "绑定第三方账号到现有租户", description = "将第三方账号绑定到现有的租户账号")
    public WebResult<String> bindToExistingTenant(
            @Parameter(description = "用户名", required = true)
            @RequestParam @NotBlank(message = "用户名不能为空") String username,
            @Parameter(description = "密码", required = true)
            @RequestParam @NotBlank(message = "密码不能为空") String password,
            @Parameter(description = "第三方平台类型", required = true)
            @RequestParam @NotBlank(message = "第三方平台类型不能为空") String provider,
            @AuthenticationPrincipal OAuth2User oAuth2User) {

        log.info("绑定第三方账号到现有租户: username={}, provider={}", username, provider);

        try {
            // 验证租户账号
            Tenant tenant = tenantService.findByUsername(username);
            if (tenant == null) {
                return WebResult.error(404, "用户不存在");
            }

            if (!tenantService.validatePassword(tenant, password)) {
                return WebResult.error(401, "密码错误");
            }

            // 绑定第三方账号
            boolean success = thirdPartyService.bindThirdPartyToTenant(tenant.getTenantId(), provider, oAuth2User);
            if (success) {
                log.info("第三方账号绑定成功: tenantId={}, provider={}", tenant.getTenantId(), provider);
                return WebResult.successData("绑定成功");
            } else {
                return WebResult.error(500, "绑定失败");
            }

        } catch (IllegalArgumentException e) {
            log.warn("第三方账号绑定失败: {}", e.getMessage());
            return WebResult.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("第三方账号绑定失败", e);
            return WebResult.error(500, "绑定失败: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    @Operation(summary = "第三方账号注册新租户", description = "使用第三方账号信息注册新的租户账号")
    public WebResult<String> registerWithThirdParty(
            @Parameter(description = "用户名", required = true)
            @RequestParam @NotBlank(message = "用户名不能为空") String username,
            @Parameter(description = "密码", required = true)
            @RequestParam @NotBlank(message = "密码不能为空") String password,
            @Parameter(description = "手机号", required = true)
            @RequestParam @NotBlank(message = "手机号不能为空") String phone,
            @Parameter(description = "短信验证码", required = true)
            @RequestParam @NotBlank(message = "验证码不能为空") String code,
            @Parameter(description = "第三方平台类型", required = true)
            @RequestParam @NotBlank(message = "第三方平台类型不能为空") String provider,
            @AuthenticationPrincipal OAuth2User oAuth2User) {

        log.info("第三方账号注册新租户: username={}, phone={}, provider={}", username, phone, provider);

        try {
            // 注册租户（会验证短信验证码）
            String email = null;
            if (oAuth2User instanceof TenantOAuth2User tenantOAuth2User) {
                email = tenantOAuth2User.getProviderEmail();
            }
            
            Tenant tenant = tenantService.registerTenant(username, password, phone, email);

            // 绑定第三方账号
            boolean bindSuccess = thirdPartyService.bindThirdPartyToTenant(tenant.getTenantId(), provider, oAuth2User);
            if (!bindSuccess) {
                log.warn("租户注册成功但第三方账号绑定失败: tenantId={}, provider={}", tenant.getTenantId(), provider);
            }

            log.info("第三方账号注册新租户成功: tenantId={}, provider={}", tenant.getTenantId(), provider);
            return WebResult.successData("注册成功");

        } catch (IllegalArgumentException e) {
            log.warn("第三方账号注册失败: {}", e.getMessage());
            return WebResult.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("第三方账号注册失败", e);
            return WebResult.error(500, "注册失败: " + e.getMessage());
        }
    }

    @PostMapping("/unbind")
    @Operation(summary = "解绑第三方账号", description = "解绑租户的第三方账号")
    public WebResult<String> unbindThirdParty(
            @Parameter(description = "租户ID", required = true)
            @RequestParam @NotBlank(message = "租户ID不能为空") String tenantId,
            @Parameter(description = "第三方平台类型", required = true)
            @RequestParam @NotBlank(message = "第三方平台类型不能为空") String provider) {

        log.info("解绑第三方账号: tenantId={}, provider={}", tenantId, provider);

        try {
            boolean success = thirdPartyService.unbindThirdParty(tenantId, provider);
            if (success) {
                log.info("第三方账号解绑成功: tenantId={}, provider={}", tenantId, provider);
                return WebResult.successData("解绑成功");
            } else {
                return WebResult.error(404, "解绑失败，未找到绑定记录");
            }

        } catch (Exception e) {
            log.error("第三方账号解绑失败", e);
            return WebResult.error(500, "解绑失败: " + e.getMessage());
        }
    }

    @GetMapping("/bindings/{tenantId}")
    @Operation(summary = "查询租户绑定的第三方账号", description = "查询指定租户绑定的所有第三方账号")
    public WebResult<Object> getThirdPartyBindings(
            @Parameter(description = "租户ID", required = true)
            @PathVariable @NotBlank(message = "租户ID不能为空") String tenantId) {

        log.info("查询租户绑定的第三方账号: tenantId={}", tenantId);

        try {
            var bindings = thirdPartyService.findByTenantId(tenantId);
            return WebResult.successData(bindings.stream().map(binding -> new Object() {
                public final String provider = binding.getProvider();
                public final String providerUsername = binding.getProviderUsername();
                public final String providerNickname = binding.getProviderNickname();
                public final String providerAvatar = binding.getProviderAvatar();
                public final String bindTime = binding.getBindTime().toString();
            }).toList());

        } catch (Exception e) {
            log.error("查询租户绑定的第三方账号失败", e);
            return WebResult.error(500, "查询失败: " + e.getMessage());
        }
    }
}
