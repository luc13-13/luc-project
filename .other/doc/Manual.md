# 系统部署手册

## 一、项目规范

### 1、项目命名规则

（1）脚手架项目：framework-【功能简写】，例如</br>
（2）基础服务：【系统】-system，例如</br>luc-system
（3）认证服务：authorization-【功能】，例如</br>authorization-server
（4）核心业务服务：【业务】-center，例如</br>product-center
（5）小程序服务：mini-app-【小程序名称】，例如</br>mini-app-cookbook

### 2、数据库规范

（1）表规范

| column      | type        | default           | comment                     |
|-------------|-------------|-------------------|-----------------------------|
| id          | bigint      | auto increment    | 自增主键                        |
| dt_created  | timestamp   | current_timestamp | 创建时间                        |
| created_by  | varchar(64) | not null          | 创建人id                       |
| dt_modified | timestamp   | current_timestamp | 修改时间                        |
| modified_by | varchar(64) | not null          | 修改人id                       |
| deleted     | tinyint(1)  | 0                 | 是否被删除, 0否, 1是               |
| status      | tinyint(1)  | 0                 | 是否有效, 0否, 1是(根据业务考虑是否使用该字段) |



## 二、快速启动

## 三、认证与鉴权

Spring Cloud Gateway + Spring Security + OAuth2.0
核心接口：
WebFilter: 所有请求经过WebFilter处理，SpringSecurity使用了代理模式，通过WebFilterChainProxy代理SecurityWebFilterChain
SecurityWebFilterChain：由SpringSecurity提供的安全
![alt text](images/SpringSecurity/reactive/WebFilterChainProxy_and_SecurityWebFilterChain.png)
请求链路：
1：请求进入WebFilterChainProxy，该代理类封装了已创建的SecurityWebFilterChain列表，对请求进行拦截、装饰
2：顺序遍历SecurityWebFilterChain列表，匹配请求路径，找到对应的SecurityWebFilterChain
3：执行SecurityWebFilterChain中的WebFilter列表，对请求进行拦截、装饰


![alt text](images/SpringSecurity/reactive/WebFilterChain.png)

Spring Security 默认的WebFilter实现及执行顺序
![alt text](images/SpringSecurity/reactive/WebFilters.png)

### 用户名密码登陆

```mermaid
sequenceDiagram
    participant User as 用户
    participant Browser as 浏览器
    participant Gateway as 网关
    participant AuthServer as 认证服务器
    participant Security as Spring Security
    participant UserService as TenantUserDetailsService
    participant DB as 数据库
    participant Redis as Redis
    User ->> Browser: 1. 输入用户名密码
    Browser ->> Gateway: 2. POST /login
    Gateway ->> AuthServer: 3. 转发登录请求
    AuthServer ->> Security: 4. 表单认证处理
    Security ->> UserService: 5. loadUserByUsername()
    UserService ->> DB: 6. 查询租户信息
    DB -->> UserService: 7. 返回Tenant实体
    UserService -->> Security: 8. 返回TenantUserDetails
    Security ->> Security: 9. 密码验证(BCrypt)
    alt 认证成功
        Security ->> Redis: 10. 存储认证信息
        Security -->> AuthServer: 11. 认证成功
        AuthServer -->> Gateway: 12. 重定向到授权页面
        Gateway -->> Browser: 13. 返回授权页面
        Browser -->> User: 14. 显示授权确认
        User ->> Browser: 15. 确认授权
        Browser ->> Gateway: 16. POST /oauth2/authorize
        Gateway ->> AuthServer: 17. 处理授权请求
        AuthServer ->> AuthServer: 18. 生成授权码
        AuthServer ->> Redis: 19. 存储授权码
        AuthServer -->> Gateway: 20. 重定向到客户端
        Gateway -->> Browser: 21. 重定向携带授权码
        Browser -->> User: 22. 跳转到客户端应用
    else 认证失败
        Security -->> AuthServer: 11. 认证失败
        AuthServer -->> Gateway: 12. 返回登录页面(错误)
        Gateway -->> Browser: 13. 显示错误信息
        Browser -->> User: 14. 提示重新登录
    end
```

手机号验证码登陆

```mermaid
sequenceDiagram
    participant User as 用户
    participant Client as 客户端应用
    participant AuthServer as 认证服务器
    participant SmsService as 短信服务
    participant SmsProvider as 短信提供商
    participant Redis as Redis缓存
    participant AuthProvider as SmsCodeAuthenticationProvider
    participant TenantService as 租户服务
    participant DB as 数据库
    User ->> Client: 1. 输入手机号
    Client ->> AuthServer: 2. POST /sms/send/login
    AuthServer ->> SmsService: 3. sendLoginCode(phone)
    SmsService ->> Redis: 4. 检查发送频率限制
    alt 频率检查通过
        SmsService ->> SmsService: 5. 生成6位验证码
        SmsService ->> Redis: 6. 存储验证码(5分钟过期)
        SmsService ->> SmsProvider: 7. 发送短信
        SmsProvider -->> SmsService: 8. 发送结果
        SmsService -->> AuthServer: 9. 返回发送成功
        AuthServer -->> Client: 10. 返回成功响应
        Client -->> User: 11. 提示验证码已发送
    else 频率限制
        SmsService -->> AuthServer: 9. 返回频率限制错误
        AuthServer -->> Client: 10. 返回错误响应
        Client -->> User: 11. 提示发送过于频繁
    end

    User ->> Client: 12. 输入验证码
    Client ->> AuthServer: 13. POST /auth/login/phone
    AuthServer ->> AuthProvider: 14. 短信验证码认证
    AuthProvider ->> SmsService: 15. verifyCode(phone, code)
    SmsService ->> Redis: 16. 验证验证码
    alt 验证码正确
        SmsService ->> Redis: 17. 删除验证码
        SmsService -->> AuthProvider: 18. 验证成功
        AuthProvider ->> TenantService: 19. loadUserByPhone(phone)
        TenantService ->> DB: 20. 查询租户信息
        DB -->> TenantService: 21. 返回Tenant实体
        TenantService -->> AuthProvider: 22. 返回TenantUserDetails
        AuthProvider ->> AuthProvider: 23. 检查账号状态
        AuthProvider -->> AuthServer: 24. 认证成功
        AuthServer ->> Redis: 25. 生成并存储访问令牌
        AuthServer -->> Client: 26. 返回访问令牌
        Client -->> User: 27. 登录成功
    else 验证码错误
        SmsService -->> AuthProvider: 18. 验证失败
        AuthProvider -->> AuthServer: 24. 认证失败
        AuthServer -->> Client: 26. 返回错误响应
        Client -->> User: 27. 提示验证码错误
    end
```

第三方账号登陆

```mermaid
sequenceDiagram
    participant User as 用户
    participant Browser as 浏览器
    participant AuthServer as 认证服务器
    participant ThirdParty as 第三方平台(Gitee/微信)
    participant OAuth2Service as CustomOAuth2UserService
    participant ThirdPartyService as TenantThirdPartyService
    participant TenantService as 租户服务
    participant DB as 数据库
    participant Redis as Redis
    User ->> Browser: 1. 点击第三方登录
    Browser ->> AuthServer: 2. GET /oauth2/authorization/gitee
    AuthServer -->> Browser: 3. 重定向到第三方平台
    Browser ->> ThirdParty: 4. 跳转到第三方授权页面
    User ->> ThirdParty: 5. 输入第三方账号密码
    ThirdParty ->> ThirdParty: 6. 验证用户身份
    ThirdParty -->> Browser: 7. 重定向携带授权码
    Browser ->> AuthServer: 8. GET /login/oauth2/code/gitee?code=xxx
    AuthServer ->> ThirdParty: 9. 交换访问令牌
    ThirdParty -->> AuthServer: 10. 返回访问令牌
    AuthServer ->> ThirdParty: 11. 获取用户信息
    ThirdParty -->> AuthServer: 12. 返回用户信息
    AuthServer ->> OAuth2Service: 13. loadUser(userRequest)
    OAuth2Service ->> ThirdPartyService: 14. processThirdPartyLogin()
    ThirdPartyService ->> DB: 15. 查询第三方绑定记录
    DB -->> ThirdPartyService: 16. 返回绑定信息

    alt 已绑定租户
        ThirdPartyService ->> TenantService: 17. findByTenantId()
        TenantService ->> DB: 18. 查询租户信息
        DB -->> TenantService: 19. 返回Tenant实体
        TenantService -->> ThirdPartyService: 20. 返回租户信息
        ThirdPartyService ->> DB: 21. 更新登录信息
        ThirdPartyService -->> OAuth2Service: 22. 返回TenantOAuth2User(已绑定)
        OAuth2Service -->> AuthServer: 23. 返回已绑定用户
        AuthServer ->> Redis: 24. 存储认证信息
        AuthServer -->> Browser: 25. 重定向到成功页面
        Browser ->> AuthServer: 26. GET /oauth2/login/success
        AuthServer -->> Browser: 27. 返回登录成功信息
        Browser -->> User: 28. 显示登录成功

    else 未绑定租户
        ThirdPartyService ->> DB: 17. 创建第三方账号记录
        ThirdPartyService -->> OAuth2Service: 18. 返回TenantOAuth2User(未绑定)
        OAuth2Service -->> AuthServer: 19. 返回未绑定用户
        AuthServer -->> Browser: 20. 重定向到绑定页面
        Browser ->> AuthServer: 21. GET /oauth2/login/success
        AuthServer -->> Browser: 22. 返回绑定引导信息
        Browser -->> User: 23. 显示绑定或注册选项

        alt 用户选择绑定现有账号
            User ->> Browser: 24. 输入用户名密码
            Browser ->> AuthServer: 25. POST /oauth2/bind
            AuthServer ->> TenantService: 26. 验证用户名密码
            TenantService ->> DB: 27. 查询并验证
            DB -->> TenantService: 28. 返回验证结果
            TenantService -->> AuthServer: 29. 验证成功
            AuthServer ->> ThirdPartyService: 30. bindThirdPartyToTenant()
            ThirdPartyService ->> DB: 31. 更新绑定关系
            ThirdPartyService -->> AuthServer: 32. 绑定成功
            AuthServer -->> Browser: 33. 返回绑定成功
            Browser -->> User: 34. 显示绑定成功

        else 用户选择注册新账号
            User ->> Browser: 24. 输入注册信息
            Browser ->> AuthServer: 25. POST /oauth2/register
            AuthServer ->> TenantService: 26. registerTenant()
            TenantService ->> DB: 27. 创建新租户
            DB -->> TenantService: 28. 返回创建结果
            TenantService -->> AuthServer: 29. 注册成功
            AuthServer ->> ThirdPartyService: 30. bindThirdPartyToTenant()
            ThirdPartyService ->> DB: 31. 绑定第三方账号
            ThirdPartyService -->> AuthServer: 32. 绑定成功
            AuthServer -->> Browser: 33. 返回注册成功
            Browser -->> User: 34. 显示注册成功
        end
    end
```

oauth2 code登陆

```mermaid
sequenceDiagram
    participant User as 用户
    participant ClientApp as 客户端应用
    participant Browser as 浏览器
    participant AuthServer as 认证服务器
    participant Security as Spring Security
    participant Redis as Redis
    participant DB as 数据库
    User ->> ClientApp: 1. 访问受保护资源
    ClientApp -->> Browser: 2. 重定向到授权服务器
    Browser ->> AuthServer: 3. GET /oauth2/authorize?response_type=code&client_id=xxx&redirect_uri=xxx&scope=xxx
    AuthServer ->> Security: 4. 检查用户认证状态
    alt 用户未登录
        Security -->> Browser: 5. 重定向到登录页面
        Browser -->> User: 6. 显示登录页面
        User ->> Browser: 7. 输入登录凭证
        Browser ->> AuthServer: 8. POST /login
        AuthServer ->> Security: 9. 执行认证流程
        Security ->> DB: 10. 验证用户凭证
        DB -->> Security: 11. 返回用户信息
        Security ->> Redis: 12. 存储认证会话
        Security -->> AuthServer: 13. 认证成功
    else 用户已登录
        Security -->> AuthServer: 5. 认证状态有效
    end

    AuthServer ->> AuthServer: 14. 验证客户端信息
    AuthServer ->> DB: 15. 查询客户端配置
    DB -->> AuthServer: 16. 返回客户端信息

    alt 需要用户授权确认
        AuthServer -->> Browser: 17. 显示授权确认页面
        Browser -->> User: 18. 显示授权范围
        User ->> Browser: 19. 确认授权
        Browser ->> AuthServer: 20. POST /oauth2/authorize (确认)
    else 客户端已信任
        AuthServer ->> AuthServer: 17. 跳过授权确认
    end

    AuthServer ->> AuthServer: 21. 生成授权码
    AuthServer ->> Redis: 22. 存储授权码(10分钟过期)
    AuthServer -->> Browser: 23. 重定向到客户端回调地址
    Browser ->> ClientApp: 24. GET /callback?code=xxx&state=xxx
    ClientApp ->> AuthServer: 25. POST /oauth2/token (交换访问令牌)
    Note over ClientApp, AuthServer: grant_type=authorization_code<br/>code=xxx<br/>client_id=xxx<br/>client_secret=xxx
    AuthServer ->> AuthServer: 26. 验证客户端身份
    AuthServer ->> Redis: 27. 验证授权码
    Redis -->> AuthServer: 28. 返回授权码信息
    AuthServer ->> Redis: 29. 删除已使用的授权码
    AuthServer ->> AuthServer: 30. 生成访问令牌和刷新令牌
    AuthServer ->> Redis: 31. 存储令牌信息
    AuthServer -->> ClientApp: 32. 返回令牌响应
    Note over AuthServer, ClientApp: {<br/> "access_token": "xxx",<br/> "token_type": "Bearer",<br/> "expires_in": 7200,<br/> "refresh_token": "xxx",<br/> "scope": "read write"<br/>}
    ClientApp ->> AuthServer: 33. GET /userinfo (使用访问令牌)
    Note over ClientApp, AuthServer: Authorization: Bearer xxx
    AuthServer ->> Redis: 34. 验证访问令牌
    Redis -->> AuthServer: 35. 返回令牌信息
    AuthServer ->> DB: 36. 查询用户信息
    DB -->> AuthServer: 37. 返回用户详情
    AuthServer -->> ClientApp: 38. 返回用户信息
    ClientApp -->> User: 39. 返回受保护资源
```

注册流程

```mermaid
sequenceDiagram
    participant User as 用户
    participant Client as 客户端应用
    participant AuthServer as 认证服务器
    participant SmsService as 短信服务
    participant TenantService as 租户服务
    participant Redis as Redis
    participant DB as 数据库
    User ->> Client: 1. 点击注册按钮
    Client -->> User: 2. 显示注册表单
    User ->> Client: 3. 输入手机号
    Client ->> AuthServer: 4. POST /sms/send/register
    AuthServer ->> TenantService: 5. 检查手机号是否已注册
    TenantService ->> DB: 6. 查询手机号
    DB -->> TenantService: 7. 返回查询结果

    alt 手机号已注册
        TenantService -->> AuthServer: 8. 手机号已存在
        AuthServer -->> Client: 9. 返回错误信息
        Client -->> User: 10. 提示手机号已注册
    else 手机号未注册
        TenantService -->> AuthServer: 8. 手机号可用
        AuthServer ->> SmsService: 9. sendRegisterCode()
        SmsService ->> Redis: 10. 检查发送频率
        SmsService ->> SmsService: 11. 生成验证码
        SmsService ->> Redis: 12. 存储验证码
        SmsService ->> SmsService: 13. 发送短信
        SmsService -->> AuthServer: 14. 发送成功
        AuthServer -->> Client: 15. 返回成功响应
        Client -->> User: 16. 提示验证码已发送
    end

    User ->> Client: 17. 输入注册信息(用户名、密码、验证码、邮箱)
    Client ->> AuthServer: 18. POST /auth/register
    AuthServer ->> SmsService: 19. verifyCode(phone, code, "register")
    SmsService ->> Redis: 20. 验证验证码
    alt 验证码错误
        SmsService -->> AuthServer: 21. 验证失败
        AuthServer -->> Client: 22. 返回验证码错误
        Client -->> User: 23. 提示验证码错误
    else 验证码正确
        SmsService ->> Redis: 21. 删除验证码
        SmsService -->> AuthServer: 22. 验证成功
        AuthServer ->> TenantService: 23. registerTenant()
        TenantService ->> TenantService: 24. 参数验证
        TenantService ->> DB: 25. 检查用户名重复
        TenantService ->> DB: 26. 检查手机号重复
        TenantService ->> DB: 27. 检查邮箱重复(如果提供)

        alt 信息重复
            TenantService -->> AuthServer: 28. 返回重复错误
            AuthServer -->> Client: 29. 返回错误信息
            Client -->> User: 30. 提示信息已存在
        else 信息唯一
            TenantService ->> TenantService: 28. 生成租户ID
            TenantService ->> TenantService: 29. 加密密码
            TenantService ->> DB: 30. 创建租户记录
            DB -->> TenantService: 31. 返回创建结果
            TenantService -->> AuthServer: 32. 注册成功
            AuthServer ->> AuthServer: 33. 生成访问令牌
            AuthServer ->> Redis: 34. 存储令牌信息
            AuthServer -->> Client: 35. 返回令牌和用户信息
            Client -->> User: 36. 注册成功，自动登录
        end
    end
```

## 四、账号体系

## 五、luc-framework脚手架核心功能

### 5.1 framework-apidoc

日志依赖，向外暴露/v3/api-doc接口，提供日志信息，支持配置鉴权接口（指向认证服务器）

（1）服务日志配置方式：

```yaml
springdoc:
  info:
    title: "XXX服务 API文档"
    version: "版本号"
    description: "服务描述"
    authorization-url: "获取授权的接口（至支持授权码）"
    token-url: "获取access token和refresh token"
    contract:
      name: "联系人姓名"
      url: "联系人主页"
      email: "联系人邮箱"
    license:
      name: "许可证名称(MIT、Apache等)"
      url: "许可证地址"
```

（2）网关聚合服务API文档方式：

```xml
<!--添加依赖-->
<dependencies>
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webflux-ui</artifactId>
    </dependency>
</dependencies>
```

```yaml
# 增加配置
springdoc:
  swagger-ui:
    use-root-path: true
    oauth:
      client-id: "已注册的客户端id"
      client-secret: "密钥"
      scopes: "权限范围"
```

通过 [RefreshRouteEventListener.java](../../authorization-gateway/src/main/java/com/lc/authorization/gateway/config/RefreshRouteEventListener.java)
监听服务变化，聚合各服务API文档，访问<http://网关服务地址/swagger-ui/index.html>

## 六、其他配置

### 1、Maven

- [ ] 完善说明</br>

完整配置文件见[settings.xml](../mvn/settings.xml)</br>
说明：Maven版本3.6.3，中央仓库[阿里云云效仓库](https://packages.aliyun.com/)</br>
建议将仓库进行规划，目前分为RELEASE、SNAPSHOT</br>

（1）下载 maven 3.6.3, 解压到安装目录 ***/path/to/maven_home*** </br>
（2）设置环境变量 ***MAVEN_HOME=/path/to/maven_home***</br>
（3）修改 ***$MAVEN_HOME/settings.xml***</br>
· 本地仓库 ***localRepository*** 标签</br>
· 镜像 ***mirrors*** 标签</br>
· 服务器 ***servers*** 标签</br>
· 配置文件 ***profiles*** 标签</br>
jdk、仓库、sonar

（4）在项目的pom.xml中设置部署地址与Maven配置文件中一致

### 2、Sonar

#### · SonarCloud

#### · SonarServer

#### · SonarQube For IDE

### 3、Mysql

### 4、Nacos Cluster

### 3、Redis Cluster

### 4、Kafka based on Kraft Cluster

### 3、Docker

### 4、Kubernetes

```css
.circle-number::before {
  content: "•"; /* 或者使用其他字符 */
  color: black; /* 颜色 */
  font-size: 20px; /* 大小 */
  margin-right: 5px; /* 与文本的距离 */
}
```
