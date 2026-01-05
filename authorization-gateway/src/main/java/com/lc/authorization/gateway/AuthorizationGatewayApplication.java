package com.lc.authorization.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.data.redis.autoconfigure.DataRedisReactiveAutoConfiguration;

@SpringBootApplication(exclude = {DataRedisReactiveAutoConfiguration.class})
public class AuthorizationGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationGatewayApplication.class, args);
    }

}
