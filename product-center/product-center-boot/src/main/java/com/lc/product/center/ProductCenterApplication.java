package com.lc.product.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <pre>
 * product-center-boot 应用启动类
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ProductCenterApplication {

    public static void main(String[] args) { SpringApplication.run(ProductCenterApplication.class, args); }
}
