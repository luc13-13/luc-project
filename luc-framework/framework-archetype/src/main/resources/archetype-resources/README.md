# ${artifactId}

基于 LUC Framework 构建的 Spring Boot 微服务项目。

## 项目信息

- **项目名称**: ${artifactId}
- **包名**: ${package}
- **版本**: ${version}
- **框架**: LUC Framework
- **Java版本**: 17
- **Spring Boot版本**: 3.2.11

## 功能特性

- ✅ 基于 Spring Boot 3.x
- ✅ 集成 LUC Framework 核心组件
- ✅ 支持 OAuth2 认证授权
- ✅ 集成 SpringDoc OpenAPI 3.0
- ✅ 支持 Redis 缓存
- ✅ 支持 MySQL 数据库
- ✅ 统一异常处理
- ✅ 统一响应格式
- ✅ CORS 跨域支持

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6.3+
- MySQL 8.0+
- Redis 6.0+

### 2. 配置数据库

1. 创建数据库：
```sql
CREATE DATABASE ${artifactId} CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改 `application.yml` 中的数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/${artifactId}?useSSL=false&useUnicode=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8
    username: your_username
    password: your_password
```

### 3. 配置 Redis

修改 `application.yml` 中的 Redis 配置：
```yaml
spring:
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password: your_redis_password
```

### 4. 启动应用

```bash
mvn spring-boot:run
```

### 5. 访问应用

- **应用地址**: http://localhost:8080
- **API文档**: http://localhost:8080/swagger-ui.html
- **健康检查**: http://localhost:8080/actuator/health

## API 接口

### Hello World 接口

```bash
# 基本调用
curl http://localhost:8080/hello

# 带参数调用
curl http://localhost:8080/hello?name=LUC

# 获取服务信息
curl http://localhost:8080/hello/info
```

## 项目结构

```
${artifactId}/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ${package}/
│   │   │       ├── ${classNamePrefix}Application.java     # 启动类
│   │   │       ├── controller/                            # 控制器
│   │   │       │   └── HelloController.java
│   │   │       └── config/                                # 配置类
│   │   │           └── ApplicationConfig.java
│   │   └── resources/
│   │       └── application.yml                            # 配置文件
│   └── test/
│       └── java/
│           └── ${package}/
│               └── ${classNamePrefix}ApplicationTests.java # 测试类
├── pom.xml                                                # Maven配置
└── README.md                                              # 项目说明
```

## 开发指南

### 1. 添加新的控制器

在 `${package}.controller` 包下创建新的控制器类：

```java
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {
    
    @GetMapping
    @Operation(summary = "获取用户列表")
    public WebResult<List<User>> getUsers() {
        // 实现逻辑
        return WebResult.success(users);
    }
}
```

### 2. 添加数据库实体

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String email;
    
    // getters and setters
}
```

### 3. 使用 LUC Framework 组件

```java
// 使用统一响应格式
return WebResult.success(data);
return WebResult.error("错误信息");

// 使用 Redis 缓存
@Autowired
private RedisHelper redisHelper;

// 使用数据权限
@DataScope(type = DataScopeType.USER)
public List<User> getUserList() {
    // 自动添加数据权限过滤
}
```

## 部署说明

### 1. 打包应用

```bash
mvn clean package
```

### 2. 运行 JAR 包

```bash
java -jar target/${artifactId}-${version}.jar
```

### 3. Docker 部署

```dockerfile
FROM openjdk:17-jre-slim
COPY target/${artifactId}-${version}.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 技术支持

如有问题，请联系 LUC Framework 开发团队。
