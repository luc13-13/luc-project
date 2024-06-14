package com.lc.auth.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-31 16:14
 */

@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients
public class AuthGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthGatewayApplication.class, args);
    }

}
