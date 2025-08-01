package com.lc.auth.web;

import com.lc.framework.core.mvc.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <pre>
 * 错误处理控制器
 * 处理系统错误页面
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Slf4j
@Controller
public class MyErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public WebResult<Object> handleError(HttpServletRequest request, HttpServletResponse response) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String errorMessage = (String) request.getAttribute("jakarta.servlet.error.message");
        String requestUri = (String) request.getAttribute("jakarta.servlet.error.request_uri");
        
        log.warn("访问错误: statusCode={}, message={}, uri={}", statusCode, errorMessage, requestUri);
        
        if (statusCode == null) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        
        String message = switch (statusCode) {
            case 404 -> "页面不存在";
            case 403 -> "访问被拒绝，请先登录";
            case 401 -> "未授权访问";
            case 500 -> "服务器内部错误";
            default -> errorMessage != null ? errorMessage : "未知错误";
        };
        
        return WebResult.error(statusCode, message + " (请访问 /login 登录或 /doc.html 查看API文档)");
    }
}
