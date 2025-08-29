package com.lc.framework.core.mvc;

/**
 * rest请求响应状态码和描述
 * @author : Lu Cheng
 * @version : 1.0
 * @date : 2023/4/15 13:39
 */
public interface StatusConstants {

    Integer SUCCESS = 200;

    String MSG_SUCCESS = "api.success";

    Integer CODE_UNAUTHORIZED = 401;

    String MSG_UNAUTHORIZED = "未授权！";

    Integer CODE_LOGIN_FAILURE = 40101;

    String MSG_LOGIN_FAILURE = "登录失败！";

    Integer CODE_FORBIDDEN = 403;

    String MSG_FORBIDDEN = "禁止访问！";

    Integer CODE_BIZ_ERROR = 500;

    String MSG_BIZ_ERROR = "系统内部异常！";
}
