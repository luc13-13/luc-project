package com.lc.framework.core.mvc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author : Lu Cheng
 * @version : 1.0
 * @desc :
 * @date : 2023/4/15 13:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Schema(name = "WebResult", title = "rest统一返回格式", description = "封装返回对象")
public class WebResult<S> implements Serializable {

//    @Schema(name = "timestamp", description = "时间戳", type = "Long")
    private Long timestamp = System.currentTimeMillis();

//    @Schema(name = "code", description = "状态码", type = "Integer", example = "200")
    private Integer code;

//    @Schema(name = "data", description = "接口响应数据", subTypes = Object.class)
    private S data;

//    @Schema(name = "msg", description = "接口响应数据", type = "String")
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

    public static <S> WebResult<S> success(S data) {
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

    public static <S> WebResult<S> error(S data,  Status status) {
        return response(data, status.getCode(), status.getDesc());
    }

    public static <S> WebResult<S> bizError(String msg) {
        return response(null, StatusConstants.CODE_BIZ_ERROR, msg);
    }

    public static <S> boolean isSuccess(WebResult<S> result) {
        return result.getData() != null && StatusConstants.SUCCESS.equals(result.getCode());
    }

}
