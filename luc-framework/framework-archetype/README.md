# LUC Framework Maven Archetype 使用指南

## 概述

基于 framework-archetype 项目创建的 Maven archetype，用于快速生成基于 LUC Framework 的 Spring Boot 项目模板。

## Archetype 结构

```
framework-archetype/
├── pom.xml                                                    # Archetype 项目配置
└── src/main/resources/
    ├── META-INF/maven/
    │   └── archetype-metadata.xml                             # Archetype 元数据配置
    └── archetype-resources/                                   # 模板资源
        ├── pom.xml                                            # 生成项目的 pom.xml 模板
        ├── README.md                                          # 生成项目的说明文档
        └── src/
            ├── main/
            │   ├── java/
            │   │   └── __packageInPathFormat__/               # 包路径占位符
            │   │       ├── __classNamePrefix__Application.java # 启动类模板
            │   │       ├── controller/
            │   │       │   └── HelloController.java           # 示例控制器
            │   │       └── config/
            │   │           └── ApplicationConfig.java         # 配置类模板
            │   └── resources/
            │       └── application.yml                        # 配置文件模板
            └── test/
                └── java/
                    └── __packageInPathFormat__/
                        └── __classNamePrefix__ApplicationTests.java # 测试类模板
```

## 构建 Archetype

### 1. 进入 framework-archetype 目录

```bash
cd luc-framework/framework-archetype
```

### 2. 构建 archetype

```bash
mvn clean install
```

### 3. 验证构建结果

构建成功后，archetype 会被安装到本地 Maven 仓库：
```
~/.m2/repository/com/lc/framework-archetype/1.0.0/
```

## 使用 Archetype 生成项目

### 方式一：使用 Maven 命令行

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.lc \
  -DarchetypeArtifactId=framework-archetype \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.example \
  -DartifactId=my-service \
  -Dversion=1.0.0 \
  -Dpackage=com.example.service \
  -DclassNamePrefix=MyService \
  -Dauthor="Your Name" \
  -Ddate="2024" \
  -DinteractiveMode=false
```

### 方式二：交互式生成

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.lc \
  -DarchetypeArtifactId=framework-archetype \
  -DarchetypeVersion=1.0.0
```

然后按提示输入各项参数。

### 参数说明

| 参数 | 说明 | 默认值 | 示例 |
|------|------|--------|------|
| `groupId` | 项目组ID | `com.lc` | `com.example` |
| `artifactId` | 项目ID | `luc-demo` | `my-service` |
| `version` | 项目版本 | `1.0.0` | `1.0.0` |
| `package` | 包名 | `com.lc.demo` | `com.example.service` |
| `classNamePrefix` | 类名前缀 | `Demo` | `MyService` |
| `author` | 作者 | `LUC Framework` | `Your Name` |
| `date` | 日期 | `2024` | `2024` |

## 生成的项目结构

使用 archetype 生成的项目将包含：

### 1. 完整的 Maven 配置
- Spring Boot 3.2.11
- LUC Framework 依赖
- 必要的插件配置

### 2. 标准的项目结构
- 启动类：`{classNamePrefix}Application.java`
- 示例控制器：`HelloController.java`
- 配置类：`ApplicationConfig.java`
- 测试类：`{classNamePrefix}ApplicationTests.java`

### 3. 完整的配置文件
- 数据库配置（MySQL）
- Redis 配置
- SpringDoc OpenAPI 配置
- 安全配置
- 日志配置

### 4. 开箱即用的功能
- RESTful API 接口
- Swagger 文档
- 统一异常处理
- 统一响应格式
- CORS 支持

## 快速验证

生成项目后，可以快速验证：

### 1. 进入生成的项目目录
```bash
cd my-service
```

### 2. 启动项目
```bash
mvn spring-boot:run
```

### 3. 访问测试接口
```bash
# 测试 Hello 接口
curl http://localhost:8080/hello

# 访问 Swagger 文档
open http://localhost:8080/swagger-ui.html
```

## 自定义 Archetype

### 1. 修改模板文件

可以根据需要修改 `src/main/resources/archetype-resources/` 下的模板文件：

- **添加新的依赖**：修改 `pom.xml`
- **添加新的配置**：修改 `application.yml`
- **添加新的类**：在相应目录下创建新的模板文件
- **修改项目结构**：调整目录结构

### 2. 更新元数据配置

修改 `archetype-metadata.xml` 来：
- 添加新的必需属性
- 配置文件过滤规则
- 设置包结构

### 3. 重新构建

```bash
mvn clean install
```

## 发布到远程仓库

### 1. 配置 Maven settings.xml

```xml
<servers>
    <server>
        <id>your-repo</id>
        <username>your-username</username>
        <password>your-password</password>
    </server>
</servers>
```

### 2. 配置 pom.xml 分发管理

```xml
<distributionManagement>
    <repository>
        <id>your-repo</id>
        <url>http://your-repo-url/repository/maven-releases/</url>
    </repository>
</distributionManagement>
```

### 3. 发布

```bash
mvn clean deploy
```

## 最佳实践

### 1. 版本管理
- 使用语义化版本号
- 及时更新 archetype 版本
- 保持与 LUC Framework 版本同步

### 2. 模板维护
- 定期更新依赖版本
- 添加新的最佳实践
- 保持模板的简洁性

### 3. 文档更新
- 及时更新使用说明
- 提供完整的示例
- 说明配置项的作用

## 常见问题

### 1. 构建失败
- 检查 Maven 版本（建议 3.6.3+）
- 确认 JDK 版本（需要 17+）
- 检查网络连接

### 2. 生成项目启动失败
- 检查数据库配置
- 确认 Redis 连接
- 查看日志错误信息

### 3. 依赖冲突
- 检查 LUC Framework 版本
- 更新 Spring Boot 版本
- 排除冲突的传递依赖

## 总结

通过这个 Maven archetype，您可以：

1. **快速创建**基于 LUC Framework 的项目
2. **标准化**项目结构和配置
3. **提高效率**，减少重复工作
4. **保持一致性**，确保团队使用统一的项目模板

这个 archetype 包含了 LUC Framework 的核心功能和最佳实践，是快速开发微服务的理想起点。
