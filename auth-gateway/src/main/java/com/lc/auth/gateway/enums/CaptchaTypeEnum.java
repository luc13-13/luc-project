package com.lc.auth.gateway.enums;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-09-21 16:53
 */
public enum CaptchaTypeEnum {
    DEFAULT("default", "随机字母验证码生成器"),
    MIXED_CHARS("mixed", "字母数字的组合验证码"),
    MATH("math", "数学公式验证码");

    private final String code;
    private final String desc;
    CaptchaTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    @Override
    public String toString() {
        return code;
    }
}
