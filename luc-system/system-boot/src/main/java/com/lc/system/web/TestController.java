package com.lc.system.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lu Cheng
 */
@RestController
@RequestMapping("/test")
@Tag(name = "测试接口",description = "用于测试网关路由功能")
@AllArgsConstructor
public class TestController {



    @GetMapping("/hello")
    public String test1(){
        return "hello test-luc1";
    }

    @GetMapping("/scope")
    public String testDataScope() {
        return "scope";
    }

    @GetMapping("")
    public String index(){
        return "welcome index";
    }
}
