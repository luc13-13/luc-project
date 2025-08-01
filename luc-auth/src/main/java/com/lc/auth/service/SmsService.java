package com.lc.auth.service;

/**
 * <pre>
 * 短信服务接口
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
public interface SmsService {

    /**
     * 发送登录验证码
     *
     * @param phone 手机号
     * @return 发送结果
     */
    boolean sendLoginCode(String phone);

    /**
     * 发送注册验证码
     *
     * @param phone 手机号
     * @return 发送结果
     */
    boolean sendRegisterCode(String phone);

    /**
     * 验证验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @param type  验证码类型：login、register
     * @return 验证结果
     */
    boolean verifyCode(String phone, String code, String type);

    /**
     * 清除验证码
     *
     * @param phone 手机号
     * @param type  验证码类型
     */
    void clearCode(String phone, String type);
}
