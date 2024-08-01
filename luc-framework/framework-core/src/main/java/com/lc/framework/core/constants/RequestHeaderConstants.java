package com.lc.framework.core.constants;

/**
 * @author : Lu Cheng
 * @version : 1.0
 * @desc : 请求头常量，包含请求头中部分参数解释
 * @date : 2023/4/15 13:35
 */
public interface RequestHeaderConstants {

    /**
     * --------------- 权限相关的请求头 ---------------
     */
    String ACCESS_TOKEN = "X-Access-Token";

    String REFRESH_TOKEN = "X-Refresh-Token";

    String Authentication = "Authentication";

    String KNIFE4J_TOKEN_KEY = "knife4j-token-key";

    /**
     * --------------- 请求和session相关的字段名 ---------------
     */
    String REQUEST_ID = "X-Request-Id";

    String JSESSIONID = "JSESSIONID";

    /**
     * --------------- 请求头中Cookie名，及其中用户信息相的关字段名 ---------------
     */
    String cookie = "Cookie";
    String USER_ID = "UserId";

    /**
     * --------------- 请求属性名，区分请求类型 ---------------
     */
    String ATTRIBUTE_IGNORE_FILTER = "visitor";
}
