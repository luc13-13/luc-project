package com.lc.auth.gateway.strategy;

/**
 * <pre>
 * 验证码生成和校验接口， 用于承接开源验证码生成方案
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-09-18 10:13
 */
public interface CaptchaGenerator {
    /**
     * 获取验证码接口
     * @param uuid 获取验证码请求的唯一标识，同请求头中的JSESSIONID
     * @author Lu Cheng
     * @create 2023/9/21
     */
    String generate(String uuid);

    boolean support(String captchaType);
}
