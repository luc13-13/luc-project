package com.lc.auth.server.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/2 18:17
 * @version : 1.0
 */
@Slf4j
@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
//        log.debug("访问登录页面, error={}", error);
        if (error != null) {
            model.addAttribute("error", "登录失败，请检查用户名和密码");
        }
        return  "login";
    }
}
