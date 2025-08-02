package com.lc.auth.server.security;

import com.lc.auth.server.security.properties.LoginProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * <pre>
 *     项目启动后打印日志
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/2 18:30
 * @version : 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class SecurityApplicationRunner implements ApplicationRunner {

    private final LoginProperties loginProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Login Page: {}", loginProperties.loginPage());
    }
}
