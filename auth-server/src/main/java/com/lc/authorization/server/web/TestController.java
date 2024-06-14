package com.lc.authorization.server.web;

import com.lc.framework.core.mvc.WebResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-10-19 09:46
 */
@RestController
@RequestMapping("")
public class TestController {

//    @PostMapping("/login")
//    public String login(@RequestBody LoginRequestDTO loginRequestDTO) {
//
//        return "login";
//    }

//    @GetMapping("/login")
//    public String login(Model model, HttpSession session) {
//
//        return "login";
//    }

    @GetMapping("/user")
    public WebResult<String> userInfo(){
        return WebResult.successData("user info");
    }

    @GetMapping("/test")
    public WebResult<String> test() {
        return WebResult.successData("hello");
    }

    @GetMapping
    public WebResult<String> index() {
        return WebResult.successData("welcome home!");
    }

    @GetMapping("/error")
    public WebResult<String> error() {
        return WebResult.error("error happened...", 500, "error happened...");
    }

    @GetMapping("/captcha")
    public WebResult<String> captcha() {
        return WebResult.successData("abcd");
    }
}
