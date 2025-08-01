-- LUC认证中心数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS luc_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE luc_auth;

-- 租户表
CREATE TABLE IF NOT EXISTS sys_tenant (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    tenant_id VARCHAR(64) NOT NULL COMMENT '租户ID（唯一标识）',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
    phone VARCHAR(20) NOT NULL COMMENT '手机号（唯一）',
    email VARCHAR(100) COMMENT '邮箱',
    real_name VARCHAR(50) COMMENT '真实姓名',
    avatar VARCHAR(500) COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    birthday DATETIME COMMENT '生日',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用，2-锁定',
    phone_verified TINYINT NOT NULL DEFAULT 0 COMMENT '是否已验证手机号：0-未验证，1-已验证',
    email_verified TINYINT NOT NULL DEFAULT 0 COMMENT '是否已验证邮箱：0-未验证，1-已验证',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    login_count BIGINT NOT NULL DEFAULT 0 COMMENT '登录次数',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建者',
    update_by VARCHAR(50) COMMENT '更新者',
    remark VARCHAR(500) COMMENT '备注',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_tenant_id (tenant_id),
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_phone (phone),
    KEY idx_email (email),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户表';

-- 租户第三方账号绑定表
CREATE TABLE IF NOT EXISTS sys_tenant_third_party (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    tenant_id VARCHAR(64) COMMENT '租户ID',
    provider VARCHAR(50) NOT NULL COMMENT '第三方平台类型：gitee、wechat、github、qq等',
    provider_user_id VARCHAR(100) NOT NULL COMMENT '第三方平台用户唯一标识',
    provider_username VARCHAR(100) COMMENT '第三方平台用户名',
    provider_nickname VARCHAR(100) COMMENT '第三方平台用户昵称',
    provider_avatar VARCHAR(500) COMMENT '第三方平台用户头像',
    provider_email VARCHAR(100) COMMENT '第三方平台用户邮箱',
    provider_raw_info TEXT COMMENT '第三方平台返回的原始用户信息（JSON格式）',
    bind_status TINYINT NOT NULL DEFAULT 0 COMMENT '绑定状态：0-未绑定，1-已绑定，2-已解绑',
    bind_time DATETIME COMMENT '绑定时间',
    unbind_time DATETIME COMMENT '解绑时间',
    last_login_time DATETIME COMMENT '最后登录时间',
    login_count BIGINT NOT NULL DEFAULT 0 COMMENT '登录次数',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_provider_user (provider, provider_user_id),
    KEY idx_tenant_id (tenant_id),
    KEY idx_provider (provider),
    KEY idx_bind_status (bind_status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户第三方账号绑定表';

-- OAuth2客户端表
CREATE TABLE IF NOT EXISTS oauth2_registered_client (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    client_id VARCHAR(100) NOT NULL COMMENT '客户端ID',
    client_id_issued_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '客户端ID发布时间',
    client_secret VARCHAR(200) COMMENT '客户端密钥',
    client_secret_expires_at DATETIME COMMENT '客户端密钥过期时间',
    client_name VARCHAR(200) NOT NULL COMMENT '客户端名称',
    client_authentication_methods TEXT NOT NULL COMMENT '客户端认证方法（JSON格式）',
    authorization_grant_types TEXT NOT NULL COMMENT '授权类型（JSON格式）',
    redirect_uris TEXT COMMENT '重定向URI（JSON格式）',
    post_logout_redirect_uris TEXT COMMENT '后重定向URI（JSON格式）',
    scopes TEXT NOT NULL COMMENT '授权范围（JSON格式）',
    client_settings TEXT NOT NULL COMMENT '客户端设置（JSON格式）',
    token_settings TEXT NOT NULL COMMENT 'Token设置（JSON格式）',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建者',
    update_by VARCHAR(50) COMMENT '更新者',
    remark VARCHAR(500) COMMENT '备注',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_client_id (client_id),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OAuth2客户端表';

-- OAuth2授权表
CREATE TABLE IF NOT EXISTS oauth2_authorization (
    id VARCHAR(100) NOT NULL COMMENT '主键ID',
    registered_client_id VARCHAR(100) NOT NULL COMMENT '注册客户端ID',
    principal_name VARCHAR(200) NOT NULL COMMENT '主体名称',
    authorization_grant_type VARCHAR(100) NOT NULL COMMENT '授权类型',
    authorized_scopes TEXT COMMENT '授权范围',
    attributes TEXT COMMENT '属性',
    state VARCHAR(500) COMMENT '状态',
    authorization_code_value TEXT COMMENT '授权码值',
    authorization_code_issued_at DATETIME COMMENT '授权码发布时间',
    authorization_code_expires_at DATETIME COMMENT '授权码过期时间',
    authorization_code_metadata TEXT COMMENT '授权码元数据',
    access_token_value TEXT COMMENT '访问令牌值',
    access_token_issued_at DATETIME COMMENT '访问令牌发布时间',
    access_token_expires_at DATETIME COMMENT '访问令牌过期时间',
    access_token_metadata TEXT COMMENT '访问令牌元数据',
    access_token_type VARCHAR(100) COMMENT '访问令牌类型',
    access_token_scopes TEXT COMMENT '访问令牌范围',
    oidc_id_token_value TEXT COMMENT 'OIDC ID令牌值',
    oidc_id_token_issued_at DATETIME COMMENT 'OIDC ID令牌发布时间',
    oidc_id_token_expires_at DATETIME COMMENT 'OIDC ID令牌过期时间',
    oidc_id_token_metadata TEXT COMMENT 'OIDC ID令牌元数据',
    refresh_token_value TEXT COMMENT '刷新令牌值',
    refresh_token_issued_at DATETIME COMMENT '刷新令牌发布时间',
    refresh_token_expires_at DATETIME COMMENT '刷新令牌过期时间',
    refresh_token_metadata TEXT COMMENT '刷新令牌元数据',
    user_code_value TEXT COMMENT '用户码值',
    user_code_issued_at DATETIME COMMENT '用户码发布时间',
    user_code_expires_at DATETIME COMMENT '用户码过期时间',
    user_code_metadata TEXT COMMENT '用户码元数据',
    device_code_value TEXT COMMENT '设备码值',
    device_code_issued_at DATETIME COMMENT '设备码发布时间',
    device_code_expires_at DATETIME COMMENT '设备码过期时间',
    device_code_metadata TEXT COMMENT '设备码元数据',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OAuth2授权表';

-- OAuth2授权同意表
CREATE TABLE IF NOT EXISTS oauth2_authorization_consent (
    registered_client_id VARCHAR(100) NOT NULL COMMENT '注册客户端ID',
    principal_name VARCHAR(200) NOT NULL COMMENT '主体名称',
    authorities TEXT NOT NULL COMMENT '权限',
    PRIMARY KEY (registered_client_id, principal_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OAuth2授权同意表';

-- 插入初始数据
INSERT INTO sys_tenant (tenant_id, username, password, phone, email, real_name, status, phone_verified, email_verified) VALUES
('T001', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOBEDQJIrPqZqQ.Xqpm', '13800138000', 'admin@luc.com', '系统管理员', 1, 1, 1),
('T002', 'test', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOBEDQJIrPqZqQ.Xqpm', '13800138001', 'test@luc.com', '测试用户', 1, 1, 1)
ON DUPLICATE KEY UPDATE username = VALUES(username);
