# 系统部署手册

## 一、项目结构
### 1、项目命名规则

（1）脚手架项目——framework-【功能简写】，例如</br>
（2）基础服务——【系统】-system，例如</br>
（3）认证服务——auth-【功能】，例如</br>
（4）核心业务服务——【服务】-center，例如</br>
（5）小程序服务——mini-app-【小程序名称】，例如</br>

## 二、快速启动

## 三、认证与鉴权

## 四、账号体系

## 五、脚手架核心功能

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
