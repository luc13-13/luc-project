package com.lc.auth.server.web;

import cn.hutool.core.util.RandomUtil;
import com.lc.framework.core.mvc.WebResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/8/1 14:34
 */
@Tag(name = "OAuth2客户端接口")
@Slf4j
@RestController
@RequestMapping("/client")
public class RegisterClientController {

    @PostMapping("/register")
    public WebResult<RegisteredClient> register(String clientName) {
//        RegisteredClient registeredClient = RegisteredClient
//                .withId(RandomUtil.randomString(64))
//                .clientName(clientName)
//                .scopes()
//                .clientSecret()
        return WebResult.success();
    }
}
