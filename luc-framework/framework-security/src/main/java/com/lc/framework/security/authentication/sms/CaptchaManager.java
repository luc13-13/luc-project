package com.lc.framework.security.authentication.sms;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/6/11 17:24
 */
public interface CaptchaManager {
    boolean checkCaptcha(String mobile, String captcha);
}
