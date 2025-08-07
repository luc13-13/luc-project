package com.lc.auth.server.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * CSRF Token 控制器
 * 为前后端分离项目提供 CSRF token
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class CsrfController {
    
    /**
     * 获取 CSRF Token
     * 前端在发起需要 CSRF 保护的请求前，先调用此接口获取 token
     */
    @GetMapping("/csrf-token")
    public Map<String, Object> getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        
        if (csrfToken != null) {
            Map<String, String> tokenInfo = new HashMap<>();
            tokenInfo.put("token", csrfToken.getToken());
            tokenInfo.put("headerName", csrfToken.getHeaderName());
            tokenInfo.put("parameterName", csrfToken.getParameterName());
            
            result.put("csrf", tokenInfo);
            log.debug("返回 CSRF token: {}", csrfToken.getToken());
        } else {
            result.put("success", false);
            result.put("message", "CSRF token 不可用");
            log.warn("CSRF token 不可用");
        }
        
        return result;
    }
}
