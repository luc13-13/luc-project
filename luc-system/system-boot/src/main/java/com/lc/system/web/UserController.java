package com.lc.system.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.system.domain.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *     用户接口
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/2 13:46
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/info")
    public WebResult<UserDTO> userInfo() {
        return WebResult.success();
    }
}
