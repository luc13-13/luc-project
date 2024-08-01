package com.lc.auth.server.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录请求参数
 * @author lucheng
 * @date 2022/4/30 14:10
 * @version 1.0
 */
@Data
@Schema(name = "LoginRequest", title = "封装登录请求参数", description = "封装登录请求参数")
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO implements Serializable {

    @Schema(name = "username", title = "用户名")
    private String username;

    @Schema(name = "password", title = "密码" )
    private String password;

    @Schema(name = "rememberMe", title = "是否勾选“记住我”, true勾选， false未勾选" )
    private String rememberMe;

    @Schema(name = "code", title = "验证码" )
    private String code;

    @Schema(name = "sessionId", title = "请求头中的JSESSIONID")
    private String sessionId;

    @Schema(name = "loginType", title = "登陆类型， 1用户名密码， 2手机号验证码， 3邮箱验证码， 4第三方账号扫码" )
    private String loginType;

    @Schema(name = "device", title = "登陆设备的识别码，浏览器类型（Chrome、Edge、FireFox等）" )
    private String device;

    @Schema(name = "grantType", title = "授权类型， password用户名密码， sms手机号验证码， email邮箱验证码， federation第三方账号登录， qr扫码登录" )
    private String grantType;
}
