package com.lc.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <pre>
 * luc-auth 应用启动类
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.lc.auth.mapper")
public class LucAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(LucAuthApplication.class, args);
        System.out.println("=================================================");
        System.out.println("LUC认证中心服务启动成功！");
        System.out.println("访问地址: http://localhost:8889");
        System.out.println("API文档: http://localhost:8889/doc.html");
        System.out.println("登录页面: http://localhost:8889/login");
        System.out.println("=================================================");
    }
}
