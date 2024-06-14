package com.lc.auth.gateway.feign.client;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-31 16:09
 */
@FeignClient(name = "uid-center")
public interface UidGeneratorFeignClient {
    @LoadBalanced
    @GetMapping("/uid/nextId")
    long getUid();
}
