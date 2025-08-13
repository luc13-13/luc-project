package com.lc.auth.server.web;

import com.lc.framework.core.mvc.WebResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/13 16:38
 * @version : 1.0
 */
@RestController
@RequestMapping
public class UserController {

    @GetMapping("codes")
    public WebResult<List<String>> codes() {
        return WebResult.success();
    }

    @GetMapping("/user/info")
    public WebResult<Map<String, Object>> userInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", 1);
        result.put("realName", "Admin");
        result.put("roles", List.of("admin"));
        result.put("homePath", "/index");
        return WebResult.success(result);
    }
}
