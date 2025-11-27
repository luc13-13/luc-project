package com.lc.system;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author lucheng
 */
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@OpenAPIDefinition(servers = @Server(url = "http://localhost:8809/luc-system"))
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }

}
