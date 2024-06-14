package com.lc.auth.gateway.factory;

import com.lc.auth.gateway.strategy.CaptchaGenerator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * 验证码工厂，根据验证码类型提供不同的验证码生成方式
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-09-18 10:11
 */
public class CaptchaGeneratorFactory {
    private static final Map<String, CaptchaGenerator> generators = new ConcurrentHashMap<>();

    public static CaptchaGenerator get(String captchaType) {
        return generators.get(captchaType);
    }

    public static void register(String captchaType, CaptchaGenerator generator) {
        generators.put(captchaType, generator);
    }
}
