package com.lc.framework.core.excp;

import static com.lc.framework.core.mvc.StatusConstants.*;

/**
 * @author : Lu Cheng
 * @version : 1.0
 * @desc :
 * @date : 2023/4/15 13:50
 */

public enum ArgsExceptionEnums {

    EXP_NOT_LOGIN(ILLEGAL, "未登录！"),
    EXP_NOT_ROLE(ILLEGAL, "不允许访问！"),
    EXP_NOT_PERMITTED(ILLEGAL, "无权限！"),
    EXP_AUTH_LOCKING(ILLEGAL, "账号被锁定！"),
    EXP_ILLEGAL(ILLEGAL,"请检查参数"),
    EXP_OUT_OF_RANGE(OUT_OF_RANGE,"参数范围非法"),
    EXP_NOT_LESS_THAN_ZERO(NOT_LESS_THAN_ZERO,"参数不能小于0"),
    EXP_NOT_GREATER_THAN_ZERO(NOT_GREATER_THAN_ZERO,"参数不能大于0"),
    EXP_NOT_NULL(NOT_NULL,"参数非空"),
    EXP_FORBIDDEN_ARGS(FORBIDDEN_ARGS,"参数非法");


    private final Integer code;
    private final String message;

    ArgsExceptionEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(Integer code) {
        for(ArgsExceptionEnums argsExceptionEnum : values()) {
            if(argsExceptionEnum.code.equals(code)) {
                return argsExceptionEnum.message;
            }
        }
        return null;
    }

    public String getMessage() {
        return this.message;
    }

    public static ArgsExceptionEnums valueOf(Integer code) {
        for(ArgsExceptionEnums argsExceptionEnum : values()) {
            if(argsExceptionEnum.code.equals(code)) {
                return argsExceptionEnum;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public Integer getCode(String message) {

        return null;
    }
}
