package com.lc.framework.security.auth.server.authentication.extension.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 短信验证码服务
 */
@Slf4j
public class SmsCodeService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String SMS_CODE_PREFIX = "sms:code:";
    private static final int CODE_EXPIRE_MINUTES = 5; // 验证码5分钟过期
    private static final int CODE_LENGTH = 6; // 验证码长度
    
    public SmsCodeService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    /**
     * 生成并存储验证码
     */
    public String generateAndStoreCode(String phone) {
        String code = generateCode();
        String key = SMS_CODE_PREFIX + phone;
        
        // 存储验证码到Redis，5分钟过期
        redisTemplate.opsForValue().set(key, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        
        log.info("为手机号 {} 生成验证码: {}", phone, code);
        return code;
    }
    
    /**
     * 验证验证码
     */
    public boolean verifyCode(String phone, String inputCode) {
        String key = SMS_CODE_PREFIX + phone;
        Object storedCode = redisTemplate.opsForValue().get(key);
        
        if (storedCode == null) {
            log.warn("手机号 {} 的验证码已过期或不存在", phone);
            return false;
        }
        
        boolean isValid = storedCode.toString().equals(inputCode);
        
        if (isValid) {
            // 验证成功后删除验证码
            redisTemplate.delete(key);
            log.info("手机号 {} 验证码验证成功", phone);
        } else {
            log.warn("手机号 {} 验证码验证失败，输入: {}, 期望: {}", phone, inputCode, storedCode);
        }
        
        return isValid;
    }
    
    /**
     * 生成随机验证码
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
     * 检查验证码是否存在
     */
    public boolean codeExists(String phone) {
        String key = SMS_CODE_PREFIX + phone;
        return redisTemplate.hasKey(key);
    }
}
