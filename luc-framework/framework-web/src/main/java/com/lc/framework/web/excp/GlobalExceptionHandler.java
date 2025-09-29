package com.lc.framework.web.excp;

import com.lc.framework.core.mvc.BizException;
import com.lc.framework.core.mvc.WebResult;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static com.lc.framework.core.constants.StringConstants.SEMICOLON;
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

    @ExceptionHandler(value = Exception.class)
    public WebResult<String> handException(Exception e) {
        return WebResult.error(CODE_BIZ_ERROR, "服务内部错误");
    }

    @ExceptionHandler(BizException.class)
    public <T> WebResult<T> handlerBizException(BizException e) {
        log.error("BizException:{}", e.getMessage(), e);
        return WebResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public <T> WebResult<T> handlerValidationException(ValidationException e) {
        log.error("ValidationException:{}", e.getMessage(), e);
        return WebResult.error(CODE_BIZ_ERROR, e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public WebResult<String> handlerArgumentException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(SEMICOLON));
        log.error("MethodArgumentNotValidException:{}", e.getMessage(), e);
        return WebResult.error(CODE_BIZ_ERROR, message);
    }
}
