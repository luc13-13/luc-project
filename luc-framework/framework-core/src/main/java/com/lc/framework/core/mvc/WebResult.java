package com.lc.framework.core.mvc;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;


/**
 * @author : Lu Cheng
 * @version : 1.0
 * @desc :
 * @date : 2023/4/15 13:38
 */
@Schema(name = "WebResult", title = "rest统一返回格式", description = "封装返回对象")
public class WebResult<S> implements Serializable {
    private Long timestamp = System.currentTimeMillis();
    private Integer code;
    private S data;
    private String msg;
    public static<S> WebResult<S> response(S data, Integer code,  String msg) {
        WebResult<S> webResp = new WebResult<>();
        webResp.setCode(code);
        webResp.setData(data);
        webResp.setMsg(msg);
        return webResp;
    }

    public static <S> WebResult<S> success(S data, Integer code, String msg) {
        return response(data, code, msg);
    }

    public static <S> WebResult<S> success() {
        return success(null, StatusConstants.SUCCESS, StatusConstants.SUCCESS_MSG);
    }

    public static <S> WebResult<S> successData(S data) {
        return success(data, StatusConstants.SUCCESS, StatusConstants.SUCCESS_MSG);
    }

    public static <S> WebResult<S> error(S data, Integer code, String msg) {
        return response(data, code, msg);
    }

    public static <S> WebResult<S> error(Integer code, String msg) {
        return response(null, code, msg);
    }

    public static <S> WebResult<S> error(Status status) {
        return response(null, status.getCode(), status.getDesc());
    }

    public static <S> WebResult<S> bizErrorData(S data) {
        return error(data, StatusConstants.BIZ_ERROR, StatusConstants.BIZ_ERROR_MSG);
    }

    public static <S> WebResult<S> sysErrorData(S data) {
        return error(data, StatusConstants.ERROR, StatusConstants.BIZ_ERROR_MSG);
    }

    public static <S> boolean isSuccess(WebResult<S> result) {
        return result.getData() != null && StatusConstants.SUCCESS.equals(result.getCode());
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public S getData() {
        return data;
    }

    public void setData(S data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

}
