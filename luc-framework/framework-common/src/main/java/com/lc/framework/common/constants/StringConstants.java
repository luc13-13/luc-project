package com.lc.framework.common.constants;

/**
 * <pre>
 *  统一处理常用字符串，避免代码中出现魔法数字
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-09-04 10:21
 */
public interface StringConstants {
    /**
     * ============ 符号常量 START ============
     */

    String DOT = ".";

    String EMPTY_STRING = "";

    String WHITE_SPACE = " ";

    String WHITE_TABLE = "    ";
    String COLON = ":";

    String ASTERISK = "*";

    String SLASH = "/";

    /**
     * ============ session 相关常量 START ============
     */
    String SESSIONS = "SESSIONS";

    String CURRENT_USER = "CURRENT_USER";

    String JSESSIONID = "JSESSIONID";


    /**
     * ============ redis中key的前缀 ============
     */
    String PREFIX_CAPTCHA = "CAPTCHA";
}
