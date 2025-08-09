package com.lc.framework.web.excp;

import com.lc.framework.core.mvc.BizException;
import com.lc.framework.core.mvc.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.lc.framework.core.mvc.StatusConstants.CODE_BIZ_ERROR;

/**
 * @author : Lu Cheng
 * @version : 1.0
 * @desc :
 * @date : 2023/5/20 14:34
 */
@Slf4j
@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public <T> WebResult<T> handlerException(BizException e) {
        log.error("BizException:{}", e.getMessage(), e);
        return WebResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public <T> WebResult<T> handlerException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException:{}", e.getMessage(), e);
        return WebResult.error(CODE_BIZ_ERROR, e.getBody().toString());
    }
}
