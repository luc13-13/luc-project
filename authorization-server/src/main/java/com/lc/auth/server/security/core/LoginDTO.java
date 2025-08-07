package com.lc.auth.server.security.core;

import lombok.Data;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/6 17:02
 * @version : 1.0
 */
@Data
public class LoginDTO {
    private String username;

    private String password;
}
