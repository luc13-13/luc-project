package com.lc.framework.core.excp;


/**
 * @author : lucheng
 * @date : 2021/11/23 13:51
 * @version : 1.0
 */
public class ArgumentException extends RuntimeException{
    private Integer code;
    private String message;

    public ArgumentException(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public ArgumentException(Integer code) {
        super();
        this.code = code;
        this.message = ArgsExceptionEnums.getMessage(code);
    }

    public ArgumentException() {
        super();
        this.code = 5001;
        this.message = ArgsExceptionEnums.getMessage(5001);
    }

    public ArgumentException(String msg) {
        super();
        this.code = 500;
        this.message = msg;
    }

}
