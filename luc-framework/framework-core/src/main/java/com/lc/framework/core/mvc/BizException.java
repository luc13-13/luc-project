package com.lc.framework.core.mvc;


/**
 * @author : Lu Cheng
 * @version : 1.0
 * @desc :
 * @date : 2023/4/15 13:54
 */
public class BizException extends RuntimeException {
    private final Integer code;
    private final String message;

    public BizException(String message) {
        super(message);
        this.code = StatusConstants.CODE_BIZ_ERROR;
        this.message = message;
    }

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public static BizException exp(String message) {
        return new BizException(message);
    }
}
