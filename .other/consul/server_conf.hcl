# 基础配置
## 数据中心名称，必填
datacenter = "dc1"
## 节点名称
node_name = "consul_leader"
## 数据文件目录，必填
data_dir = "D:\\tools\\consul_backups\\data"
## 以服务端模式运行，必填
server = true
## 集群节点数
bootstrap_expect = 1
# 日志级别
log_level = "INFO"

# ACL配置
acl {
  enabled = true                                       # 开启ACL
  default_policy = "deny"                       # 默认拒绝所有请求
  enable_token_persistence = true      # 持久化
  tokens = {
    master = "858aaefb-0edd-d788-c4bb-63ac4fb233f1"
    agent = "809c1cb4-b1e5-ce46-c904-17ebf6bfd21d"
  }
}

# UI配置
ui_config {
  enabled = true
}


# 网络配置
bind_addr = "127.0.0.1"
advertise_addr = "127.0.0.1"
client_addr = "0.0.0.0"             # 允许远程访问UI、API