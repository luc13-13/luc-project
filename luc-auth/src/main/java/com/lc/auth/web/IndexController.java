package com.lc.auth.web;

import com.lc.framework.core.mvc.WebResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <pre>
 * 首页控制器
 * 提供系统基本信息和状态
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Slf4j
@RestController
@Tag(name = "系统信息", description = "系统基本信息接口")
public class IndexController {

    @GetMapping("/api")
    @Operation(summary = "系统信息", description = "获取认证中心基本信息")
    public WebResult<Object> systemInfo() {
        log.debug("获取系统信息");
        
        return WebResult.successData(new Object() {
            public final String service = "LUC认证中心";
            public final String version = "1.0.0";
            public final String description = "基于Spring Authorization Server构建的认证授权中心服务";
            public final String timestamp = LocalDateTime.now().toString();
            public final Object endpoints = new Object() {
                public final String login = "/login";
                public final String apiDoc = "/doc.html";
                public final String authorize = "/oauth2/authorize";
                public final String token = "/oauth2/token";
                public final String userinfo = "/userinfo";
            };
            public final Object features = new String[]{
                "用户名密码登录",
                "手机号验证码登录", 
                "第三方账号登录",
                "OAuth2/OIDC协议支持",
                "多租户架构",
                "Redis会话管理"
            };
        });
    }

    @GetMapping("/welcome")
    @Operation(summary = "欢迎页面", description = "认证中心欢迎信息")
    public WebResult<Object> welcome() {
        log.debug("访问欢迎页面");
        
        return WebResult.successData(new Object() {
            public final String message = "欢迎使用LUC认证中心";
            public final String guide = "请访问 /login 进行登录，或访问 /doc.html 查看API文档";
            public final Object quickStart = new Object() {
                public final String step1 = "访问 /login 登录系统";
                public final String step2 = "使用测试账号: admin/123456";
                public final String step3 = "查看 /doc.html 了解API接口";
                public final String step4 = "集成OAuth2客户端应用";
            };
        });
    }
}
