---
type: "manual"
---

# 项目开发手册

## 项目背景

基于Spring Authorization Server的统一认证授权服务平台，由认证授权中心auth-server和鉴权网关auth-gateway组成

## 技术框架

java 17
spring boot 3.2.11
spring cloud 2023.0.1
mysql 8
mybatis-plus 3.5.5
springdoc-openapi 2.8.9

## 开发规范

遵循maven最小化配置，使用父项目进行版本、依赖管理。父项目maven配置文件pom.xml()[../pom.xml]

## 基础功能

## 项目结构

### 1、项目命名规则

（1）脚手架项目——framework-【功能简写】，例如</br>
（2）基础服务——【系统】-system，例如</br>
（3）认证服务——auth-【功能】，例如</br>
（4）核心业务服务——【服务】-center，例如</br>
（5）小程序服务——mini-app-【小程序名称】，例如</br>

### 2、快速启动

### 3、认证与鉴权

(1) 用户通过auth-gateway统一访问系统
(2) 请求头中携带x-access-token视为已登陆用户，auth-gateway和auth-server通过x-access-token作为key，从redis中获取security context和jwt token
(3) auth-server支持OAuth2.0的认证流程，同时拓展短信登陆、gitee登陆、微信登陆
(4) 要求下游业务服务不集成spring security