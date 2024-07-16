package com.lc.framework.core.mvc;

/**
 * @author : Lu Cheng
 * @version : 1.0
 * @desc : rest请求响应标识
 * @date : 2023/4/15 13:39
 */
public interface StatusConstants {

    Integer SUCCESS = 200;

    String SUCCESS_MSG = "请求成功";

    Integer CODE_UNAUTHORIZED = 401;

    String MSG_UNAUTHORIZED = "未授权！";

    Integer CODE_LOGIN_FAILURE = 40101;

    String MSG_LOGIN_FAILURE = "登录失败！";

    Integer CODE_FORBIDDEN = 403;

    String MSG_FORBIDDEN = "禁止访问！";

    Integer CODE_BIZ_ERROR = 500;

    String MSG_BIZ_ERROR = "系统内部异常！";

    Status LOGIN_FAILURE = Status.of(CODE_LOGIN_FAILURE, MSG_LOGIN_FAILURE);

    Status UNAUTHORIZED = Status.of(CODE_UNAUTHORIZED, MSG_UNAUTHORIZED);

    Status FORBIDDEN = Status.of(CODE_FORBIDDEN, MSG_FORBIDDEN);

    Status BIZ_ERROR = Status.of(CODE_BIZ_ERROR, MSG_BIZ_ERROR);
}
