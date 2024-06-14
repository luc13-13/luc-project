package com.lc.framework.core.mvc.handler;

import com.lc.framework.core.excp.BizException;
import com.lc.framework.core.mvc.Status;
import com.lc.framework.core.mvc.WebResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : Lu Cheng
 * @version : 1.0
 * @desc :
 * @date : 2023/5/20 14:34
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public WebResult handlerException(BizException e) {
        e.printStackTrace();
        return WebResult.response(Status.generate(e.getCode(), e.getMessage()), e.getMessage());
    }
}
