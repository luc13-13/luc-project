package com.lc.auth.server.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * 首页
     */
    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        log.info("访问首页");

        if (authentication != null && authentication.isAuthenticated()) {
            // 用户已登录，显示用户信息
            String username = authentication.getName();
            model.addAttribute("isAuthenticated", true);
            model.addAttribute("username", username);

            // 如果是 OAuth2 用户，获取更多信息
            if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
                String name = oauth2User.getAttribute("name");
                String avatarUrl = oauth2User.getAttribute("avatar_url");
                model.addAttribute("displayName", name != null ? name : username);
                model.addAttribute("userAvatar", avatarUrl);
                model.addAttribute("loginType", "oauth2");
            } else {
                model.addAttribute("displayName", username);
                model.addAttribute("loginType", "form");
            }
        } else {
            // 用户未登录
            model.addAttribute("isAuthenticated", false);
        }

        return "index";
    }

    /**
     * 首页 - 兼容 /home 路径
     */
    @GetMapping("/home")
    public String homeAlias(Authentication authentication, Model model) {
        return home(authentication, model);
    }

    @RequestMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
//        log.debug("访问登录页面, error={}", error);
        if (error != null) {
            model.addAttribute("error", "登录失败，请检查用户名和密码");
        }
        return  "login";
    }

    /**
     * OAuth2 第三方登录成功后的处理页面
     */
    @GetMapping("/oauth2/login/success")
    public String oauth2LoginSuccess(Authentication authentication, Model model) {
        log.info("OAuth2 登录成功，用户信息: {}", authentication.getName());

        if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            // 获取第三方用户信息
            String name = oauth2User.getAttribute("name");
            String login = oauth2User.getAttribute("login");
            String avatarUrl = oauth2User.getAttribute("avatar_url");
            String email = oauth2User.getAttribute("email");

            log.info("第三方用户信息 - 姓名: {}, 登录名: {}, 头像: {}, 邮箱: {}", name, login, avatarUrl, email);

            // 将用户信息传递给页面
            model.addAttribute("userName", name != null ? name : login);
            model.addAttribute("userLogin", login);
            model.addAttribute("userAvatar", avatarUrl);
            model.addAttribute("userEmail", email);
            model.addAttribute("provider", "gitee"); // 暂时硬编码，后续可以动态获取
        }

        return "oauth2-success";
    }
}
