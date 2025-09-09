package com.lc.auth.server.web;

import com.lc.framework.security.auth.server.authentication.extension.sms.SmsCodeService;
import com.lc.framework.core.mvc.WebResult;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     短信接口
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/4 09:35
 * @version : 1.0
 */
@Slf4j
@RestController
@RequestMapping("/sms")
public class SmsController {

    private final SmsCodeService smsCodeService;

    public SmsController(SmsCodeService smsCodeService) {
        this.smsCodeService = smsCodeService;
    }

    /**
     * 获取短信验证码
     */
    @PostMapping("/code")
    public WebResult<String> sendSmsCode(@RequestParam("phone")
                                             @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确") String phone) {
//        @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
        log.info("发送短信验证码，手机号: {}", phone);
        if (!StringUtils.hasText(phone)) {
            return WebResult.error("手机号不能为空");
        }
        try {
            String code = smsCodeService.generateAndStoreCode(phone);

            // 模拟发送短信（实际项目中这里会调用短信服务商API）
            log.info("模拟发送短信到 {}，验证码: {}", phone, code);
            return WebResult.success(code);

        } catch (Exception e) {
            log.error("发送短信验证码失败", e);
            return WebResult.error("验证码发送失败，请稍后重试");
        }
    }

    /**
     * 验证短信验证码
     */
    @PostMapping("/verify")
    public Map<String, Object> verifySmsCode(@RequestParam String phone, @RequestParam String code) {
        log.info("验证短信验证码，手机号: {}, 验证码: {}", phone, code);

        Map<String, Object> result = new HashMap<>();

        if (!StringUtils.hasText(phone) || !StringUtils.hasText(code)) {
            result.put("success", false);
            result.put("message", "手机号和验证码不能为空");
            return result;
        }

        boolean isValid = smsCodeService.verifyCode(phone, code);

        if (isValid) {
            result.put("success", true);
            result.put("message", "验证码验证成功");
        } else {
            result.put("success", false);
            result.put("message", "验证码错误或已过期");
        }

        return result;
    }
}
