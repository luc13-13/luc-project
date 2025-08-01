package com.lc.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * <pre>
 * 短信验证码认证过滤器
 * 处理短信验证码登录请求
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Slf4j
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_FILTER_PROCESSES_URL = "/auth/login/phone";
    private static final String PHONE_PARAMETER = "phone";
    private static final String CODE_PARAMETER = "code";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SmsCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher(DEFAULT_FILTER_PROCESSES_URL, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {

        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String phone;
        String code;

        // 支持JSON和表单两种请求格式
        if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
            // JSON格式
            Map<String, String> loginData = objectMapper.readValue(request.getInputStream(), Map.class);
            phone = loginData.get(PHONE_PARAMETER);
            code = loginData.get(CODE_PARAMETER);
        } else {
            // 表单格式
            phone = obtainPhone(request);
            code = obtainCode(request);
        }

        if (!StringUtils.hasText(phone)) {
            throw new AuthenticationServiceException("手机号不能为空");
        }

        if (!StringUtils.hasText(code)) {
            throw new AuthenticationServiceException("验证码不能为空");
        }

        phone = phone.trim();
        code = code.trim();

        log.debug("短信验证码登录请求: phone={}", phone);

        SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(phone, code);
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 获取手机号参数
     */
    protected String obtainPhone(HttpServletRequest request) {
        return request.getParameter(PHONE_PARAMETER);
    }

    /**
     * 获取验证码参数
     */
    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter(CODE_PARAMETER);
    }

    /**
     * 设置认证详情
     */
    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
