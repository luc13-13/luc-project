package com.lc.auth.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * 测试控制器
 * 用于验证路径匹配和安全配置是否正常
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/public")
    public String publicEndpoint() {
        log.info("访问公开测试接口");
        return "公开接口访问成功 - " + System.currentTimeMillis();
    }

    @GetMapping("/ping")
    public String ping() {
        log.info("Ping测试");
        return "pong";
    }

    @GetMapping("/status")
    public String status() {
        log.info("状态检查");
        return "服务运行正常";
    }
}
