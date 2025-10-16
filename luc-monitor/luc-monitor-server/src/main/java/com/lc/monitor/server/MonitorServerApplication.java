package com.lc.monitor.server;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/10/16 22:23
 * @version : 1.0
 */
@EnableDiscoveryClient
@EnableAdminServer
@SpringBootApplication
public class MonitorServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonitorServerApplication.class, args);
    }
}
