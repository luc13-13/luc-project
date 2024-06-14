package com.lc.framework.core.mvc;

/**
 * @author : Lu Cheng
 * @version : 1.0
 * @desc : rest请求响应标识
 * @date : 2023/4/15 13:39
 */
public interface StatusConstants {

    Integer SUCCESS = 200;
    Integer ERROR = 400;

    Integer BIZ_ERROR = 500;

    Integer PERMISSION_DENIED = 501;

    Integer AUTH_OUT_OF_DATE = 502;
    Integer ILLEGAL = 5001;

    Integer OUT_OF_RANGE = 5002;

    Integer NOT_LESS_THAN_ZERO = 5003;

    Integer NOT_GREATER_THAN_ZERO = 5004;
    Integer NOT_FOUND_PATH = 404;

    Integer FORBIDDEN_ARGS = 4001;
    Integer NOT_NULL = 4002;
}
