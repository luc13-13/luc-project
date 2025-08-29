package com.lc.framework.core.mvc;


import lombok.Getter;

/**
 * @author : Lu Cheng
 * @version : 1.0
 * @desc :
 * @date : 2023/4/15 13:54
 */
public class BizException extends RuntimeException {
    @Getter
    private final Integer code;
    private final String message;

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public static BizException exp(String message) {
        return new BizException(StatusConstants.CODE_BIZ_ERROR, message);
    }
}
