package ${package}.controller;

import com.lc.framework.core.mvc.WebResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * 示例控制器
 * </pre>
 *
 * @author ${author}
 * @date ${date}
 */
@Slf4j
@RestController
@RequestMapping("/hello")
@Tag(name = "示例接口", description = "Hello World 示例接口")
public class HelloController {

    @GetMapping
    @Operation(summary = "Hello World", description = "返回Hello World消息")
    public WebResult<String> hello(@RequestParam(defaultValue = "World") String name) {
        log.info("收到Hello请求，参数：{}", name);
        String message = "Hello, " + name + "! 欢迎使用基于LUC框架的 ${artifactId} 服务！";
        return WebResult.success(message);
    }

    @GetMapping("/info")
    @Operation(summary = "服务信息", description = "获取服务基本信息")
    public WebResult<Object> info() {
        return WebResult.success(new Object() {
            public final String serviceName = "${artifactId}";
            public final String version = "1.0.0";
            public final String framework = "LUC Framework";
            public final String description = "基于LUC框架构建的微服务";
        });
    }
}
