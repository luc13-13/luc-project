package ${package};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <pre>
 * ${artifactId} 应用启动类
 * </pre>
 *
 * @author ${author}
 * @date ${date}
 */
@SpringBootApplication
@EnableFeignClients
public class ${classNamePrefix}Application {

    public static void main(String[] args) {
        SpringApplication.run(${classNamePrefix}Application.class, args);
        System.out.println("${artifactId} 服务启动成功！");
    }
}
