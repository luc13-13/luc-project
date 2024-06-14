package com.lc.framework.core.mvc;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

import static com.lc.framework.core.mvc.StatusConstants.*;


/**
 * @author : Lu Cheng
 * @version : 1.0
 * @desc :
 * @date : 2023/4/15 13:38
 */
@Schema(name = "WebResult", title = "rest统一返回格式", description = "封装返回对象")
public class WebResult<S> implements Serializable {
    private Status status;
    private Long timestamp = System.currentTimeMillis();
    private S data;

    public static<S> WebResult<S> response(Status state, S data) {
        WebResult<S> webResp = new WebResult();
        webResp.setStatus(state);
        webResp.setData(data);
        return webResp;
    }

    public static <S> WebResult<S> success(S data, Integer code, String message) {
        return response(Status.generate(code, message), data);
    }

    public static <S> WebResult<S> success() {
        return success(null, SUCCESS, (String) null);
    }

    public static <S> WebResult<S> successData(S data) {
        return success(data, SUCCESS, (String) null);
    }

    public static <S> WebResult<S> error(S data, Integer code, String message) {
        return response(Status.generate(code, message), data);
    }

    public static <S> WebResult<S> bizErrorData(S data) {
        return error(data, BIZ_ERROR, null);
    }

    public static <S> WebResult<S> sysErrorData(S data) {
        return error(data, ERROR, null);
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public S getData() {
        return data;
    }

    public void setData(S data) {
        this.data = data;
    }

    public static Integer getSUCCESS() {
        return SUCCESS;
    }

    public static Integer getERROR() {
        return ERROR;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
