package com.lc.auth.server;

import com.lc.framework.security.annotation.EnableResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-10-19 09:26
 */
@SpringBootApplication(exclude = {SessionAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
@EnableResourceServer
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
