# luc-auth

## 项目描述
基于 Spring Authorization Server构建的认证授权中心服务，为平台提供租户登陆、租户注册、租户授权、客户端授权、客户端注册功能，遵循OAuth2规范与要求。

## 技术框架

- **项目名称**: luc-auth
- **包名**: com.lc.auth
- **版本**: 1.0.0
- **框架**: LUC Framework
- **JDK**: 21
- **Spring Boot**: 3.5.4
- **Spring Cloud**: 2025.0.0
- **Springdoc OpenAPI**: 2.8.9
- **Spring Authorization Server**: 1.5.1
- 
## 功能实现
**1、自定义租户实现**
自定义OAuth2User和UserDetails实现，每个租户ID唯一、手机号唯一，每个租户ID可关联一个邮箱、多个第三方账号（例如gitee、wechat、github等）。

**2、基于Redis的认证与授权管理**
租户认证后，认证信息和授权信息存储在redis中，前端只传递X-Access-Token，后端根据该请求头取值，从redis获取认证信息和授权信息。

**3、拓展认证方式**
在OAuth2的基础上，增加用户名密码登陆、短信验证码登陆、第三方账号登陆。
第三方账号登陆成功后，如果没有绑定平台租户，引导用户关联或注册平台租户。


## 要求
- 符合spring boot项目规范，分包明确，最小化模块开发
- 代码注释明确
- 善于利用spring boot自动装配机制

