package com.lc.auth.server.security.encoder;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/2 18:04
 * @version : 1.0
 */
public class EncoderConfiguration {
    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
