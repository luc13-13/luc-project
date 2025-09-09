package com.lc.framework.security.core.constants;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/5 09:17
 * @version : 1.0
 */
public final class OAuth2ParameterConstants {

    public static final String LOGIN_TYPE = "login_type";

    public static final String SMS_CODE = "sms_code";

    public static final String SPRING_SECURITY_FORM_PHONE_KEY = "phone";
    /**
     * 认证信息在redis中的key
     */
    public static final String SECURITY_CONTEXT_PREFIX = "spring:security:context:";
    /**
     * 返回给前端的请求头
     */
    public static final String AUTH_KEY = "luc_auth_token";

    /**
     * 请求头中token的前缀
     */
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";

}
