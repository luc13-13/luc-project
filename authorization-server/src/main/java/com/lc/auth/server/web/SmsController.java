package com.lc.auth.server.web;

import com.lc.auth.server.security.authentication.extension.sms.SmsCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    public Map<String, Object> sendSmsCode(@RequestParam String phone, @RequestParam(defaultValue = "login") String type) {
        log.info("发送短信验证码，手机号: {}, 类型: {}", phone, type);

        Map<String, Object> result = new HashMap<>();

        if (!StringUtils.hasText(phone)) {
            result.put("success", false);
            result.put("message", "手机号不能为空");
            return result;
        }

        // 简单的手机号格式验证
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            result.put("success", false);
            result.put("message", "手机号格式不正确");
            return result;
        }

        try {
            String code = smsCodeService.generateAndStoreCode(phone);

            // 模拟发送短信（实际项目中这里会调用短信服务商API）
            log.info("模拟发送短信到 {}，验证码: {}", phone, code);

            result.put("success", true);
            result.put("message", "验证码发送成功");
            result.put("code", code); // 测试环境返回验证码，生产环境不应该返回

        } catch (Exception e) {
            log.error("发送短信验证码失败", e);
            result.put("success", false);
            result.put("message", "验证码发送失败，请稍后重试");
        }

        return result;
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
