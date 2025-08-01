package com.lc.auth.service.impl;

import com.lc.auth.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 短信服务实现类
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final StringRedisTemplate redisTemplate;

    private static final String SMS_CODE_PREFIX = "sms:code:";
    private static final String SMS_LIMIT_PREFIX = "sms:limit:";
    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRE_MINUTES = 5;
    private static final int SEND_LIMIT_MINUTES = 1;

    @Override
    public boolean sendLoginCode(String phone) {
        return sendCode(phone, "login");
    }

    @Override
    public boolean sendRegisterCode(String phone) {
        return sendCode(phone, "register");
    }

    @Override
    public boolean verifyCode(String phone, String code, String type) {
        if (!StringUtils.hasText(phone) || !StringUtils.hasText(code) || !StringUtils.hasText(type)) {
            return false;
        }

        String key = SMS_CODE_PREFIX + type + ":" + phone;
        String storedCode = redisTemplate.opsForValue().get(key);

        if (storedCode != null && storedCode.equals(code)) {
            // 验证成功，删除验证码
            redisTemplate.delete(key);
            log.info("短信验证码验证成功: phone={}, type={}", phone, type);
            return true;
        }

        log.warn("短信验证码验证失败: phone={}, type={}, code={}", phone, type, code);
        return false;
    }

    @Override
    public void clearCode(String phone, String type) {
        if (StringUtils.hasText(phone) && StringUtils.hasText(type)) {
            String key = SMS_CODE_PREFIX + type + ":" + phone;
            redisTemplate.delete(key);
            log.debug("清除短信验证码: phone={}, type={}", phone, type);
        }
    }

    /**
     * 发送验证码
     *
     * @param phone 手机号
     * @param type  验证码类型
     * @return 发送结果
     */
    private boolean sendCode(String phone, String type) {
        if (!StringUtils.hasText(phone) || !StringUtils.hasText(type)) {
            return false;
        }

        // 检查发送频率限制
        String limitKey = SMS_LIMIT_PREFIX + phone;
        if (redisTemplate.hasKey(limitKey)) {
            log.warn("短信发送过于频繁: phone={}", phone);
            return false;
        }

        // 生成验证码
        String code = generateCode();

        // 存储验证码
        String codeKey = SMS_CODE_PREFIX + type + ":" + phone;
        redisTemplate.opsForValue().set(codeKey, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 设置发送频率限制
        redisTemplate.opsForValue().set(limitKey, "1", SEND_LIMIT_MINUTES, TimeUnit.MINUTES);

        // 实际发送短信（这里模拟发送）
        boolean sendResult = doSendSms(phone, code, type);

        if (sendResult) {
            log.info("短信验证码发送成功: phone={}, type={}, code={}", phone, type, code);
        } else {
            log.error("短信验证码发送失败: phone={}, type={}", phone, type);
            // 发送失败，清除验证码
            redisTemplate.delete(codeKey);
        }

        return sendResult;
    }

    /**
     * 生成验证码
     *
     * @return 验证码
     */
    private String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    /**
     * 实际发送短信
     * 这里可以集成阿里云短信、腾讯云短信等服务
     *
     * @param phone 手机号
     * @param code  验证码
     * @param type  验证码类型
     * @return 发送结果
     */
    private boolean doSendSms(String phone, String code, String type) {
        // TODO: 集成实际的短信服务提供商
        // 这里模拟发送成功
        log.info("模拟发送短信: phone={}, code={}, type={}", phone, code, type);
        return true;
    }
}
