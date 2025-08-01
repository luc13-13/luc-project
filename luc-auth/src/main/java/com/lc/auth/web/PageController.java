package com.lc.auth.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <pre>
 * 页面控制器
 * 处理页面跳转和模板渲染
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Slf4j
@Controller
@RequestMapping
public class PageController {

    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        log.debug("访问登录页面, error={}", error);
        if (error != null) {
            model.addAttribute("error", "登录失败，请检查用户名和密码");
        }
        return "login";
    }

    /**
     * 首页 - 重定向到API文档
     */
    @GetMapping("/")
    public String indexPage() {
        log.debug("访问首页，重定向到API文档");
        return "redirect:/doc.html";
    }

    /**
     * 首页 - JSON格式响应
     */
    @GetMapping("/home")
    @ResponseBody
    public String home() {
        log.debug("访问home页面");
        return """
            {
                "service": "LUC认证中心",
                "version": "1.0.0",
                "message": "认证中心服务运行正常",
                "links": {
                    "login": "/login",
                    "api_doc": "/doc.html",
                    "oauth2_authorize": "/oauth2/authorize"
                }
            }
            """;
    }

    /**
     * 注册页面
     */
    @GetMapping("/register")
    public String registerPage() {
        log.debug("访问注册页面");
        return "register";
    }
}
