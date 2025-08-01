package com.lc.auth.web;

import com.lc.auth.service.SmsService;
import com.lc.auth.service.TenantService;
import com.lc.framework.core.mvc.WebResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <pre>
 * 短信验证码控制器
 * 提供发送登录验证码、注册验证码等功能
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Slf4j
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
@Validated
@Tag(name = "短信验证码", description = "短信验证码相关接口")
public class SmsController {

    private final SmsService smsService;
    private final TenantService tenantService;

    @PostMapping("/send/login")
    @Operation(summary = "发送登录验证码", description = "向指定手机号发送登录验证码")
    public WebResult<String> sendLoginCode(
            @Parameter(description = "手机号", required = true)
            @RequestParam @NotBlank(message = "手机号不能为空")
            @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确") String phone) {

        log.info("发送登录验证码: phone={}", phone);

        try {
            // 检查手机号是否已注册
            if (!tenantService.existsByPhone(phone)) {
                return WebResult.error(404, "手机号未注册，请先注册");
            }

            // 发送验证码
            boolean success = smsService.sendLoginCode(phone);
            if (success) {
                return WebResult.successData("验证码发送成功");
            } else {
                return WebResult.error(500, "验证码发送失败，请稍后重试");
            }

        } catch (Exception e) {
            log.error("发送登录验证码失败", e);
            return WebResult.error(500, "验证码发送失败: " + e.getMessage());
        }
    }

    @PostMapping("/send/register")
    @Operation(summary = "发送注册验证码", description = "向指定手机号发送注册验证码")
    public WebResult<String> sendRegisterCode(
            @Parameter(description = "手机号", required = true)
            @RequestParam @NotBlank(message = "手机号不能为空")
            @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确") String phone) {

        log.info("发送注册验证码: phone={}", phone);

        try {
            // 检查手机号是否已注册
            if (tenantService.existsByPhone(phone)) {
                return WebResult.error(409, "手机号已注册，请直接登录");
            }

            // 发送验证码
            boolean success = smsService.sendRegisterCode(phone);
            if (success) {
                return WebResult.successData("验证码发送成功");
            } else {
                return WebResult.error(500, "验证码发送失败，请稍后重试");
            }

        } catch (Exception e) {
            log.error("发送注册验证码失败", e);
            return WebResult.error(500, "验证码发送失败: " + e.getMessage());
        }
    }

    @PostMapping("/verify")
    @Operation(summary = "验证验证码", description = "验证手机验证码是否正确")
    public WebResult<String> verifyCode(
            @Parameter(description = "手机号", required = true)
            @RequestParam @NotBlank(message = "手机号不能为空")
            @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确") String phone,
            @Parameter(description = "验证码", required = true)
            @RequestParam @NotBlank(message = "验证码不能为空")
            @Pattern(regexp = "^\\d{6}$", message = "验证码格式不正确") String code,
            @Parameter(description = "验证码类型", required = true)
            @RequestParam @NotBlank(message = "验证码类型不能为空")
            @Pattern(regexp = "^(login|register)$", message = "验证码类型只能是login或register") String type) {

        log.info("验证验证码: phone={}, type={}", phone, type);

        try {
            boolean valid = smsService.verifyCode(phone, code, type);
            if (valid) {
                return WebResult.successData("验证码验证成功");
            } else {
                return WebResult.error(400, "验证码错误或已过期");
            }

        } catch (Exception e) {
            log.error("验证验证码失败", e);
            return WebResult.error(500, "验证失败: " + e.getMessage());
        }
    }
}
