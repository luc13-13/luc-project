package com.lc.framework.core.mvc;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * REST响应封装
 * @author : Lu Cheng
 * @version : 1.0
 * @date : 2023/4/15 13:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Schema(name = "WebResult", title = "rest统一返回格式", description = "封装返回对象")
public class WebResult<S> implements Serializable {

    @Schema(name = "timestamp", description = "时间戳", type = "Long")
    private Long timestamp = System.currentTimeMillis();

    @Schema(name = "code", description = "状态码", type = "Integer", example = "200")
    private Integer code;

    @Schema(name = "data", description = "接口响应数据")
    private S data;

    @Schema(name = "msg", description = "消息", type = "String")
    private String msg;

    public static<S> WebResult<S> response(S data, Integer code,  String msg) {
        WebResult<S> webResp = new WebResult<>();
        webResp.setCode(code);
        webResp.setData(data);
        webResp.setMsg(msg);
        return webResp;
    }

    public static <S> WebResult<S> success() {
        return success(null, StatusConstants.SUCCESS, null);
    }

    public static <S> WebResult<S> success(S data) {
        return success(data, StatusConstants.SUCCESS, null);
    }

    public static <S> WebResult<S> success(S data, Integer code, String msg) {
        return response(data, code, msg);
    }

    public static <S> WebResult<S> error(S data) {
        return error(data, StatusConstants.CODE_BIZ_ERROR, null);
    }

    public static <S> WebResult<S> error(String msg) {
        return error(StatusConstants.CODE_BIZ_ERROR, msg);
    }

    public static <S> WebResult<S> error(Integer code, String msg) {
        return error(null, code, msg);
    }

    public static <S> WebResult<S> error(S data, Integer code, String msg) {
        return response(data, code, msg);
    }

    public static <S> boolean isSuccess(WebResult<S> result) {
        return result.getData() != null && StatusConstants.SUCCESS.equals(result.getCode());
    }

}
