CREATE DATABASE IF NOT EXISTS product_center DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
use product_center;

-- 产品信息表，包含平台所有产品配置
drop table if exists product_info;
-- ============================================================
-- 1. 产品信息表（四层结构 + 计费属性）
-- ============================================================
CREATE TABLE product_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id VARCHAR(32) NOT NULL COMMENT '租户ID',
    
    -- 四层产品结构
    product_code VARCHAR(32) NOT NULL COMMENT '产品编码: CVM/CBS/CLB',
    sub_product_code VARCHAR(64) NOT NULL COMMENT '规格族编码: S5_GENERAL/C6_COMPUTE',
    billing_item_code VARCHAR(64) NOT NULL COMMENT '计费项编码: CPU/MEMORY/STORAGE',
    sub_billing_item_code VARCHAR(64) NOT NULL COMMENT '计费规格编码: INTEL_4C/HYGON_4C',
    
    -- 名称
    product_name VARCHAR(128) COMMENT '产品名称',
    sub_product_name VARCHAR(128) COMMENT '规格族名称',
    billing_item_name VARCHAR(128) COMMENT '计费项名称',
    sub_billing_item_name VARCHAR(128) COMMENT '计费规格名称',
    
    -- 规格属性
    spec_value DECIMAL(10,2) COMMENT '规格值: 4, 8, 100',
    spec_unit VARCHAR(16) COMMENT '规格单位: 核, GB, Mbps',
    
    -- 计费属性
    base_price DECIMAL(12,4) COMMENT '基准单价',
    price_factor DECIMAL(5,2) DEFAULT 1.00 COMMENT '价格系数',
    metering_unit VARCHAR(32) COMMENT '计量单位（账单展示）: 核·小时, GB·月',
    
    -- 状态与排序
    `status` VARCHAR(16) DEFAULT 'ACTIVE' COMMENT 'DRAFT/ACTIVE/INACTIVE',
    sort_order INT DEFAULT 0 COMMENT '排序',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除(0:未删除 1:已删除)',
    -- 审计字段
    created_by VARCHAR(64) COMMENT '创建人',
    dt_created DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    modified_by VARCHAR(64) COMMENT '更新人',
    dt_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE KEY uk_product_spec (tenant_id, product_code, sub_product_code, 
                                 billing_item_code, sub_billing_item_code, deleted),
    INDEX idx_tenant_product (tenant_id, product_code, status),
    INDEX idx_tenant_sub_product (tenant_id, product_code, sub_product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品信息表';


-- ============================================================
-- 2. 产品SKU表（可售卖单元）
-- ============================================================
drop table if exists product_sku;
CREATE TABLE product_sku (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id VARCHAR(32) NOT NULL COMMENT '租户ID',
    
    -- SKU基本信息
    sku_code VARCHAR(128) NOT NULL COMMENT 'SKU编码: CVM-S5-4C8G',
    sku_name VARCHAR(256) COMMENT 'SKU名称: 通用型S5 4核8G',
    
    -- 关联产品（冗余，便于查询）
    product_code VARCHAR(32) NOT NULL COMMENT '所属产品',
    sub_product_code VARCHAR(64) NOT NULL COMMENT '所属规格族',
    
    -- SKU类型
    sku_type VARCHAR(32) DEFAULT 'INSTANCE' COMMENT 'INSTANCE/ADDON/BUNDLE',
    
    -- 售卖控制
    saleable TINYINT DEFAULT 1 COMMENT '是否可售: 1是 0否',
    visible TINYINT DEFAULT 1 COMMENT '是否可见: 1是 0否',
    
    -- 配额限制
    quota_limit INT COMMENT '配额限制，NULL表示无限制',
    
    -- 状态
    `status` VARCHAR(16) DEFAULT 'ACTIVE' COMMENT 'DRAFT/ACTIVE/INACTIVE',
    publish_time DATETIME COMMENT '上架时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除(0:未删除 1:已删除)',
    -- 审计字段
    created_by VARCHAR(64),
    dt_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_by VARCHAR(64),
    dt_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_tenant_sku (tenant_id, sku_code, deleted),
    INDEX idx_tenant_product (tenant_id, product_code, sub_product_code, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品SKU表';


-- ============================================================
-- 3. SKU计费项组合表（SKU由哪些计费项组成）
-- ============================================================
drop table if exists sku_item_combination;
CREATE TABLE sku_item_combination (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id VARCHAR(32) NOT NULL COMMENT '租户ID',
    
    -- 关联SKU
    sku_code VARCHAR(128) NOT NULL COMMENT 'SKU编码',
    
    -- 关联计费项（product_info的四层编码）
    product_code VARCHAR(32) NOT NULL,
    sub_product_code VARCHAR(64) NOT NULL,
    billing_item_code VARCHAR(64) NOT NULL,
    sub_billing_item_code VARCHAR(64) NOT NULL,
    
    -- 数量
    quantity DECIMAL(10,2) DEFAULT 1 COMMENT '数量/份数',
    
    -- 是否参与定价计算
    pricing_included TINYINT DEFAULT 1 COMMENT '是否计入SKU定价: 1是 0否',
    
    -- 审计字段
    created_by VARCHAR(64),
    dt_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_by VARCHAR(64),
    dt_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_sku_billing_item (tenant_id, sku_code, billing_item_code, sub_billing_item_code),
    INDEX idx_sku (tenant_id, sku_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SKU计费项组合表';


-- ============================================================
-- 4. SKU定价表（支持按量/包月/包年）
-- ============================================================
drop table if exists sku_pricing;
CREATE TABLE sku_pricing (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id VARCHAR(32) NOT NULL COMMENT '租户ID',
    
    -- 关联SKU
    sku_code VARCHAR(128) NOT NULL COMMENT 'SKU编码',
    
    -- 定价模式
    pricing_model VARCHAR(32) NOT NULL COMMENT '定价模式: PAY_AS_GO/PREPAID/SUBSCRIPTION',
    
    -- 计费周期
    billing_period VARCHAR(16) COMMENT '计费周期: HOURLY/DAILY/MONTHLY/QUARTERLY/YEARLY',
    period_count INT DEFAULT 1 COMMENT '周期数量',
    
    -- 价格
    original_price DECIMAL(12,2) COMMENT '原价',
    sale_price DECIMAL(12,2) NOT NULL COMMENT '售价',
    currency VARCHAR(8) DEFAULT 'CNY' COMMENT '币种',
    
    -- 折扣
    discount_rate DECIMAL(4,2) COMMENT '折扣率: 0.85表示85折',
    
    -- 有效期
    effective_time DATETIME COMMENT '生效时间',
    expiry_time DATETIME COMMENT '失效时间',
    
    -- 优先级
    priority INT DEFAULT 0 COMMENT '优先级，数值越大优先级越高',
    
    -- 状态
    `status` VARCHAR(16) DEFAULT 'ACTIVE' COMMENT 'ACTIVE/INACTIVE',
    
    -- 审计字段
    created_by VARCHAR(64),
    dt_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_by VARCHAR(64),
    dt_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_sku_pricing (tenant_id, sku_code, pricing_model, status),
    INDEX idx_sku_period (tenant_id, sku_code, billing_period)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SKU定价表';


-- ============================================================
-- 5. 计量维度映射表（对接第三方计量平台）
-- ============================================================
drop table if exists metering_dimension_mapping;
CREATE TABLE metering_dimension_mapping (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id VARCHAR(32) NOT NULL COMMENT '租户ID',
    
    -- 计量平台信息
    platform_code VARCHAR(32) NOT NULL COMMENT '计量平台: PROMETHEUS/CLOUDWATCH/CUSTOM',
    platform_name VARCHAR(64) COMMENT '平台名称',
    metering_dimension VARCHAR(128) NOT NULL COMMENT '平台上报的维度key',
    
    -- 关联product_info的四层编码
    product_code VARCHAR(32) NOT NULL,
    sub_product_code VARCHAR(64) NOT NULL,
    billing_item_code VARCHAR(64) NOT NULL,
    sub_billing_item_code VARCHAR(64) NOT NULL,
    
    -- 值转换
    value_factor DECIMAL(16,10) DEFAULT 1.0 COMMENT '值转换系数（如bytes转GB）',
    value_formula VARCHAR(256) COMMENT '复杂转换公式',
    
    -- 状态
    `status` VARCHAR(16) DEFAULT 'ACTIVE',
    
    -- 审计字段
    created_by VARCHAR(64),
    dt_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_by VARCHAR(64),
    dt_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_platform_dimension (tenant_id, platform_code, metering_dimension),
    INDEX idx_tenant_platform (tenant_id, platform_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='计量维度映射表';


-- ============================================================
-- 初始化数据：product_info（四层产品目录）
-- ============================================================

-- 清空现有数据（可选）
-- TRUNCATE TABLE product_info;

-- ============================================================
-- 一、IaaS 产品
-- ============================================================

-- ------------------------------------------------------------
-- 1. CVM 云服务器
-- ------------------------------------------------------------
INSERT INTO product_info 
(tenant_id, product_code, sub_product_code, billing_item_code, sub_billing_item_code,
 product_name, sub_product_name, billing_item_name, sub_billing_item_name,
 spec_value, spec_unit, base_price, price_factor, metering_unit, status, sort_order, created_by)
VALUES
-- 通用型S5 - CPU
('DEFAULT', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL_2C', '云服务器', '通用型S5', 'CPU', 'Intel 2核', 2, '核', 0.40, 1.00, '核·小时', 'ACTIVE', 1, 'system'),
('DEFAULT', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL_4C', '云服务器', '通用型S5', 'CPU', 'Intel 4核', 4, '核', 0.40, 1.00, '核·小时', 'ACTIVE', 2, 'system'),
('DEFAULT', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL_8C', '云服务器', '通用型S5', 'CPU', 'Intel 8核', 8, '核', 0.40, 1.00, '核·小时', 'ACTIVE', 3, 'system'),
('DEFAULT', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL_16C', '云服务器', '通用型S5', 'CPU', 'Intel 16核', 16, '核', 0.40, 1.00, '核·小时', 'ACTIVE', 4, 'system'),

-- 通用型S5 - 内存
('DEFAULT', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4_4G', '云服务器', '通用型S5', '内存', 'DDR4 4GB', 4, 'GB', 0.20, 1.00, 'GB·小时', 'ACTIVE', 1, 'system'),
('DEFAULT', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4_8G', '云服务器', '通用型S5', '内存', 'DDR4 8GB', 8, 'GB', 0.20, 1.00, 'GB·小时', 'ACTIVE', 2, 'system'),
('DEFAULT', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4_16G', '云服务器', '通用型S5', '内存', 'DDR4 16GB', 16, 'GB', 0.20, 1.00, 'GB·小时', 'ACTIVE', 3, 'system'),
('DEFAULT', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4_32G', '云服务器', '通用型S5', '内存', 'DDR4 32GB', 32, 'GB', 0.20, 1.00, 'GB·小时', 'ACTIVE', 4, 'system'),

-- 通用型S5 - 系统盘
('DEFAULT', 'CVM', 'S5_GENERAL', 'SYSTEM_DISK', 'SSD_40G', '云服务器', '通用型S5', '系统盘', 'SSD 40GB', 40, 'GB', 0.0010, 1.00, 'GB·小时', 'ACTIVE', 1, 'system'),
('DEFAULT', 'CVM', 'S5_GENERAL', 'SYSTEM_DISK', 'SSD_80G', '云服务器', '通用型S5', '系统盘', 'SSD 80GB', 80, 'GB', 0.0010, 1.00, 'GB·小时', 'ACTIVE', 2, 'system'),

-- 计算型C6 - CPU（海光/鲲鹏）
('DEFAULT', 'CVM', 'C6_COMPUTE', 'CPU', 'HYGON_4C', '云服务器', '计算型C6', 'CPU', '海光 4核', 4, '核', 0.40, 0.85, '核·小时', 'ACTIVE', 1, 'system'),
('DEFAULT', 'CVM', 'C6_COMPUTE', 'CPU', 'HYGON_8C', '云服务器', '计算型C6', 'CPU', '海光 8核', 8, '核', 0.40, 0.85, '核·小时', 'ACTIVE', 2, 'system'),
('DEFAULT', 'CVM', 'C6_COMPUTE', 'CPU', 'KUNPENG_4C', '云服务器', '计算型C6', 'CPU', '鲲鹏 4核', 4, '核', 0.40, 0.90, '核·小时', 'ACTIVE', 3, 'system'),
('DEFAULT', 'CVM', 'C6_COMPUTE', 'CPU', 'KUNPENG_8C', '云服务器', '计算型C6', 'CPU', '鲲鹏 8核', 8, '核', 0.40, 0.90, '核·小时', 'ACTIVE', 4, 'system'),

-- 计算型C6 - 内存
('DEFAULT', 'CVM', 'C6_COMPUTE', 'MEMORY', 'DDR4_8G', '云服务器', '计算型C6', '内存', 'DDR4 8GB', 8, 'GB', 0.20, 0.85, 'GB·小时', 'ACTIVE', 1, 'system'),
('DEFAULT', 'CVM', 'C6_COMPUTE', 'MEMORY', 'DDR4_16G', '云服务器', '计算型C6', '内存', 'DDR4 16GB', 16, 'GB', 0.20, 0.85, 'GB·小时', 'ACTIVE', 2, 'system'),

-- ------------------------------------------------------------
-- 2. CBS 云硬盘
-- ------------------------------------------------------------
-- 高效云盘
('DEFAULT', 'CBS', 'CLOUD_PREMIUM', 'STORAGE', 'HDD_100G', '云硬盘', '高效云盘', '存储容量', 'HDD 100GB', 100, 'GB', 0.0004, 1.00, 'GB·小时', 'ACTIVE', 1, 'system'),
('DEFAULT', 'CBS', 'CLOUD_PREMIUM', 'STORAGE', 'HDD_200G', '云硬盘', '高效云盘', '存储容量', 'HDD 200GB', 200, 'GB', 0.0004, 1.00, 'GB·小时', 'ACTIVE', 2, 'system'),
('DEFAULT', 'CBS', 'CLOUD_PREMIUM', 'STORAGE', 'HDD_500G', '云硬盘', '高效云盘', '存储容量', 'HDD 500GB', 500, 'GB', 0.0004, 1.00, 'GB·小时', 'ACTIVE', 3, 'system'),

-- SSD云盘
('DEFAULT', 'CBS', 'CLOUD_SSD', 'STORAGE', 'SSD_100G', '云硬盘', 'SSD云盘', '存储容量', 'SSD 100GB', 100, 'GB', 0.0010, 1.00, 'GB·小时', 'ACTIVE', 1, 'system'),
('DEFAULT', 'CBS', 'CLOUD_SSD', 'STORAGE', 'SSD_200G', '云硬盘', 'SSD云盘', '存储容量', 'SSD 200GB', 200, 'GB', 0.0010, 1.00, 'GB·小时', 'ACTIVE', 2, 'system'),
('DEFAULT', 'CBS', 'CLOUD_SSD', 'STORAGE', 'SSD_500G', '云硬盘', 'SSD云盘', '存储容量', 'SSD 500GB', 500, 'GB', 0.0010, 1.00, 'GB·小时', 'ACTIVE', 3, 'system'),
('DEFAULT', 'CBS', 'CLOUD_SSD', 'STORAGE', 'SSD_1T', '云硬盘', 'SSD云盘', '存储容量', 'SSD 1TB', 1024, 'GB', 0.0010, 0.95, 'GB·小时', 'ACTIVE', 4, 'system'),

-- ------------------------------------------------------------
-- 3. CLB 负载均衡
-- ------------------------------------------------------------
-- 公网负载均衡
('DEFAULT', 'CLB', 'PUBLIC_CLB', 'INSTANCE', 'CLB_STANDARD', '负载均衡', '公网负载均衡', '实例费', '标准型', 1, '个', 0.0200, 1.00, '个·小时', 'ACTIVE', 1, 'system'),
('DEFAULT', 'CLB', 'PUBLIC_CLB', 'BANDWIDTH', 'BW_SHARED', '负载均衡', '公网负载均衡', '带宽费', '共享带宽', 1, 'Mbps', 0.0600, 1.00, 'Mbps·小时', 'ACTIVE', 2, 'system'),
('DEFAULT', 'CLB', 'PUBLIC_CLB', 'LCU', 'LCU_STANDARD', '负载均衡', '公网负载均衡', '容量费', '标准LCU', 1, 'LCU', 0.0080, 1.00, 'LCU·小时', 'ACTIVE', 3, 'system'),

-- 内网负载均衡
('DEFAULT', 'CLB', 'PRIVATE_CLB', 'INSTANCE', 'CLB_INTERNAL', '负载均衡', '内网负载均衡', '实例费', '内网型', 1, '个', 0.0100, 1.00, '个·小时', 'ACTIVE', 1, 'system'),

-- ------------------------------------------------------------
-- 4. EIP 弹性公网IP
-- ------------------------------------------------------------
-- 按带宽计费
('DEFAULT', 'EIP', 'BGP', 'BANDWIDTH', 'BW_1M', '弹性公网IP', 'BGP线路', '带宽', '1Mbps', 1, 'Mbps', 0.0300, 1.00, 'Mbps·小时', 'ACTIVE', 1, 'system'),
('DEFAULT', 'EIP', 'BGP', 'BANDWIDTH', 'BW_5M', '弹性公网IP', 'BGP线路', '带宽', '5Mbps', 5, 'Mbps', 0.0300, 1.00, 'Mbps·小时', 'ACTIVE', 2, 'system'),
('DEFAULT', 'EIP', 'BGP', 'BANDWIDTH', 'BW_10M', '弹性公网IP', 'BGP线路', '带宽', '10Mbps', 10, 'Mbps', 0.0300, 0.95, 'Mbps·小时', 'ACTIVE', 3, 'system'),
('DEFAULT', 'EIP', 'BGP', 'BANDWIDTH', 'BW_20M', '弹性公网IP', 'BGP线路', '带宽', '20Mbps', 20, 'Mbps', 0.0300, 0.90, 'Mbps·小时', 'ACTIVE', 4, 'system'),

-- 按流量计费
('DEFAULT', 'EIP', 'BGP', 'TRAFFIC', 'TRAFFIC_OUT', '弹性公网IP', 'BGP线路', '流量', '出流量', 1, 'GB', 0.8000, 1.00, 'GB', 'ACTIVE', 5, 'system'),

-- 闲置IP费用
('DEFAULT', 'EIP', 'BGP', 'IDLE', 'IP_IDLE', '弹性公网IP', 'BGP线路', '闲置费', 'IP闲置', 1, '个', 0.0100, 1.00, '个·小时', 'ACTIVE', 6, 'system'),

-- ------------------------------------------------------------
-- 5. NAT 网关
-- ------------------------------------------------------------
-- 小型
('DEFAULT', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_SMALL', 'NAT网关', 'NAT网关', '实例费', '小型', 1, '个', 0.0500, 1.00, '个·小时', 'ACTIVE', 1, 'system'),
-- 中型
('DEFAULT', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_MEDIUM', 'NAT网关', 'NAT网关', '实例费', '中型', 1, '个', 0.1000, 1.00, '个·小时', 'ACTIVE', 2, 'system'),
-- 大型
('DEFAULT', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_LARGE', 'NAT网关', 'NAT网关', '实例费', '大型', 1, '个', 0.2000, 1.00, '个·小时', 'ACTIVE', 3, 'system'),

-- NAT流量费
('DEFAULT', 'NAT', 'NAT_GATEWAY', 'TRAFFIC', 'NAT_TRAFFIC', 'NAT网关', 'NAT网关', '流量费', '数据处理', 1, 'GB', 0.0200, 1.00, 'GB', 'ACTIVE', 4, 'system'),


-- ============================================================
-- 二、SaaS 订阅产品
-- ============================================================

-- ------------------------------------------------------------
-- 1. APP_SUBSCRIPTION 应用订阅
-- ------------------------------------------------------------
-- 基础版
('DEFAULT', 'APP_SUBSCRIPTION', 'BASIC', 'SUBSCRIPTION', 'MONTHLY', '应用订阅', '基础版', '订阅费', '月度订阅', 1, '月', 99.00, 1.00, '月', 'ACTIVE', 1, 'system'),
('DEFAULT', 'APP_SUBSCRIPTION', 'BASIC', 'SUBSCRIPTION', 'QUARTERLY', '应用订阅', '基础版', '订阅费', '季度订阅', 3, '月', 89.00, 1.00, '月', 'ACTIVE', 2, 'system'),
('DEFAULT', 'APP_SUBSCRIPTION', 'BASIC', 'SUBSCRIPTION', 'YEARLY', '应用订阅', '基础版', '订阅费', '年度订阅', 12, '月', 79.00, 1.00, '月', 'ACTIVE', 3, 'system'),

-- 专业版
('DEFAULT', 'APP_SUBSCRIPTION', 'PROFESSIONAL', 'SUBSCRIPTION', 'MONTHLY', '应用订阅', '专业版', '订阅费', '月度订阅', 1, '月', 299.00, 1.00, '月', 'ACTIVE', 1, 'system'),
('DEFAULT', 'APP_SUBSCRIPTION', 'PROFESSIONAL', 'SUBSCRIPTION', 'QUARTERLY', '应用订阅', '专业版', '订阅费', '季度订阅', 3, '月', 269.00, 1.00, '月', 'ACTIVE', 2, 'system'),
('DEFAULT', 'APP_SUBSCRIPTION', 'PROFESSIONAL', 'SUBSCRIPTION', 'YEARLY', '应用订阅', '专业版', '订阅费', '年度订阅', 12, '月', 239.00, 1.00, '月', 'ACTIVE', 3, 'system'),

-- 企业版
('DEFAULT', 'APP_SUBSCRIPTION', 'ENTERPRISE', 'SUBSCRIPTION', 'MONTHLY', '应用订阅', '企业版', '订阅费', '月度订阅', 1, '月', 999.00, 1.00, '月', 'ACTIVE', 1, 'system'),
('DEFAULT', 'APP_SUBSCRIPTION', 'ENTERPRISE', 'SUBSCRIPTION', 'YEARLY', '应用订阅', '企业版', '订阅费', '年度订阅', 12, '月', 799.00, 1.00, '月', 'ACTIVE', 2, 'system'),

-- ------------------------------------------------------------
-- 2. APP_ACCOUNT 应用账号（按用户数收费）
-- ------------------------------------------------------------
-- 基础账号
('DEFAULT', 'APP_ACCOUNT', 'STANDARD', 'USER_LICENSE', 'USER_1', '应用账号', '标准账号', '用户许可', '单用户', 1, '用户', 30.00, 1.00, '用户·月', 'ACTIVE', 1, 'system'),
('DEFAULT', 'APP_ACCOUNT', 'STANDARD', 'USER_LICENSE', 'USER_5', '应用账号', '标准账号', '用户许可', '5用户包', 5, '用户', 25.00, 1.00, '用户·月', 'ACTIVE', 2, 'system'),
('DEFAULT', 'APP_ACCOUNT', 'STANDARD', 'USER_LICENSE', 'USER_10', '应用账号', '标准账号', '用户许可', '10用户包', 10, '用户', 20.00, 1.00, '用户·月', 'ACTIVE', 3, 'system'),
('DEFAULT', 'APP_ACCOUNT', 'STANDARD', 'USER_LICENSE', 'USER_50', '应用账号', '标准账号', '用户许可', '50用户包', 50, '用户', 15.00, 1.00, '用户·月', 'ACTIVE', 4, 'system'),

-- 高级账号
('DEFAULT', 'APP_ACCOUNT', 'PREMIUM', 'USER_LICENSE', 'USER_1', '应用账号', '高级账号', '用户许可', '单用户', 1, '用户', 60.00, 1.00, '用户·月', 'ACTIVE', 1, 'system'),
('DEFAULT', 'APP_ACCOUNT', 'PREMIUM', 'USER_LICENSE', 'USER_10', '应用账号', '高级账号', '用户许可', '10用户包', 10, '用户', 50.00, 1.00, '用户·月', 'ACTIVE', 2, 'system'),

-- ------------------------------------------------------------
-- 3. APP_STORAGE 应用存储空间
-- ------------------------------------------------------------
('DEFAULT', 'APP_STORAGE', 'CLOUD_STORAGE', 'STORAGE', 'STORAGE_10G', '应用存储', '云存储', '存储空间', '10GB', 10, 'GB', 5.00, 1.00, 'GB·月', 'ACTIVE', 1, 'system'),
('DEFAULT', 'APP_STORAGE', 'CLOUD_STORAGE', 'STORAGE', 'STORAGE_50G', '应用存储', '云存储', '存储空间', '50GB', 50, 'GB', 4.00, 1.00, 'GB·月', 'ACTIVE', 2, 'system'),
('DEFAULT', 'APP_STORAGE', 'CLOUD_STORAGE', 'STORAGE', 'STORAGE_100G', '应用存储', '云存储', '存储空间', '100GB', 100, 'GB', 3.00, 1.00, 'GB·月', 'ACTIVE', 3, 'system'),
('DEFAULT', 'APP_STORAGE', 'CLOUD_STORAGE', 'STORAGE', 'STORAGE_500G', '应用存储', '云存储', '存储空间', '500GB', 500, 'GB', 2.00, 1.00, 'GB·月', 'ACTIVE', 4, 'system'),

-- ------------------------------------------------------------
-- 4. APP_API 应用API调用
-- ------------------------------------------------------------
('DEFAULT', 'APP_API', 'API_PACKAGE', 'API_CALLS', 'API_10K', '应用API', 'API调用包', 'API调用', '1万次', 10000, '次', 0.0100, 1.00, '次', 'ACTIVE', 1, 'system'),
('DEFAULT', 'APP_API', 'API_PACKAGE', 'API_CALLS', 'API_100K', '应用API', 'API调用包', 'API调用', '10万次', 100000, '次', 0.0080, 1.00, '次', 'ACTIVE', 2, 'system'),
('DEFAULT', 'APP_API', 'API_PACKAGE', 'API_CALLS', 'API_1M', '应用API', 'API调用包', 'API调用', '100万次', 1000000, '次', 0.0050, 1.00, '次', 'ACTIVE', 3, 'system');


-- ============================================================
-- 初始化数据：product_sku（SKU示例）
-- ============================================================

INSERT INTO product_sku 
(tenant_id, sku_code, sku_name, product_code, sub_product_code, sku_type, saleable, visible, status, created_by)
VALUES
-- CVM SKU
('DEFAULT', 'CVM-S5-2C4G', '通用型S5 2核4G', 'CVM', 'S5_GENERAL', 'INSTANCE', 1, 1, 'ACTIVE', 'system'),
('DEFAULT', 'CVM-S5-4C8G', '通用型S5 4核8G', 'CVM', 'S5_GENERAL', 'INSTANCE', 1, 1, 'ACTIVE', 'system'),
('DEFAULT', 'CVM-S5-8C16G', '通用型S5 8核16G', 'CVM', 'S5_GENERAL', 'INSTANCE', 1, 1, 'ACTIVE', 'system'),
('DEFAULT', 'CVM-C6-HYGON-4C8G', '计算型C6 海光4核8G', 'CVM', 'C6_COMPUTE', 'INSTANCE', 1, 1, 'ACTIVE', 'system'),
('DEFAULT', 'CVM-C6-KUNPENG-8C16G', '计算型C6 鲲鹏8核16G', 'CVM', 'C6_COMPUTE', 'INSTANCE', 1, 1, 'ACTIVE', 'system'),

-- CBS SKU
('DEFAULT', 'CBS-SSD-100G', 'SSD云盘 100GB', 'CBS', 'CLOUD_SSD', 'ADDON', 1, 1, 'ACTIVE', 'system'),
('DEFAULT', 'CBS-SSD-500G', 'SSD云盘 500GB', 'CBS', 'CLOUD_SSD', 'ADDON', 1, 1, 'ACTIVE', 'system'),
('DEFAULT', 'CBS-HDD-500G', '高效云盘 500GB', 'CBS', 'CLOUD_PREMIUM', 'ADDON', 1, 1, 'ACTIVE', 'system'),

-- CLB SKU
('DEFAULT', 'CLB-PUBLIC-STANDARD', '公网负载均衡-标准型', 'CLB', 'PUBLIC_CLB', 'INSTANCE', 1, 1, 'ACTIVE', 'system'),
('DEFAULT', 'CLB-PRIVATE', '内网负载均衡', 'CLB', 'PRIVATE_CLB', 'INSTANCE', 1, 1, 'ACTIVE', 'system'),

-- EIP SKU
('DEFAULT', 'EIP-BGP-5M', 'BGP弹性IP 5Mbps', 'EIP', 'BGP', 'INSTANCE', 1, 1, 'ACTIVE', 'system'),
('DEFAULT', 'EIP-BGP-10M', 'BGP弹性IP 10Mbps', 'EIP', 'BGP', 'INSTANCE', 1, 1, 'ACTIVE', 'system'),

-- NAT SKU
('DEFAULT', 'NAT-SMALL', 'NAT网关-小型', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 1, 1, 'ACTIVE', 'system'),
('DEFAULT', 'NAT-MEDIUM', 'NAT网关-中型', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 1, 1, 'ACTIVE', 'system'),

-- SaaS SKU
('DEFAULT', 'APP-BASIC-MONTHLY', '应用基础版-月付', 'APP_SUBSCRIPTION', 'BASIC', 'SUBSCRIPTION', 1, 1, 'ACTIVE', 'system'),
('DEFAULT', 'APP-BASIC-YEARLY', '应用基础版-年付', 'APP_SUBSCRIPTION', 'BASIC', 'SUBSCRIPTION', 1, 1, 'ACTIVE', 'system'),
('DEFAULT', 'APP-PRO-MONTHLY', '应用专业版-月付', 'APP_SUBSCRIPTION', 'PROFESSIONAL', 'SUBSCRIPTION', 1, 1, 'ACTIVE', 'system'),
('DEFAULT', 'APP-PRO-YEARLY', '应用专业版-年付', 'APP_SUBSCRIPTION', 'PROFESSIONAL', 'SUBSCRIPTION', 1, 1, 'ACTIVE', 'system'),
('DEFAULT', 'APP-ENT-YEARLY', '应用企业版-年付', 'APP_SUBSCRIPTION', 'ENTERPRISE', 'SUBSCRIPTION', 1, 1, 'ACTIVE', 'system'),

-- 账号包SKU
('DEFAULT', 'APP-ACCOUNT-5', '标准账号 5用户包', 'APP_ACCOUNT', 'STANDARD', 'ADDON', 1, 1, 'ACTIVE', 'system'),
('DEFAULT', 'APP-ACCOUNT-10', '标准账号 10用户包', 'APP_ACCOUNT', 'STANDARD', 'ADDON', 1, 1, 'ACTIVE', 'system'),
('DEFAULT', 'APP-ACCOUNT-50', '标准账号 50用户包', 'APP_ACCOUNT', 'STANDARD', 'ADDON', 1, 1, 'ACTIVE', 'system');


-- ============================================================
-- 初始化数据：sku_item_combination（SKU计费项组合）
-- ============================================================

INSERT INTO sku_item_combination 
(tenant_id, sku_code, product_code, sub_product_code, billing_item_code, sub_billing_item_code, quantity, pricing_included, created_by)
VALUES
-- CVM-S5-2C4G 的组合
('DEFAULT', 'CVM-S5-2C4G', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL_2C', 1, 1, 'system'),
('DEFAULT', 'CVM-S5-2C4G', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4_4G', 1, 1, 'system'),
('DEFAULT', 'CVM-S5-2C4G', 'CVM', 'S5_GENERAL', 'SYSTEM_DISK', 'SSD_40G', 1, 1, 'system'),

-- CVM-S5-4C8G 的组合
('DEFAULT', 'CVM-S5-4C8G', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL_4C', 1, 1, 'system'),
('DEFAULT', 'CVM-S5-4C8G', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4_8G', 1, 1, 'system'),
('DEFAULT', 'CVM-S5-4C8G', 'CVM', 'S5_GENERAL', 'SYSTEM_DISK', 'SSD_40G', 1, 1, 'system'),

-- CVM-S5-8C16G 的组合
('DEFAULT', 'CVM-S5-8C16G', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL_8C', 1, 1, 'system'),
('DEFAULT', 'CVM-S5-8C16G', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4_16G', 1, 1, 'system'),
('DEFAULT', 'CVM-S5-8C16G', 'CVM', 'S5_GENERAL', 'SYSTEM_DISK', 'SSD_80G', 1, 1, 'system'),

-- CVM-C6-HYGON-4C8G 的组合
('DEFAULT', 'CVM-C6-HYGON-4C8G', 'CVM', 'C6_COMPUTE', 'CPU', 'HYGON_4C', 1, 1, 'system'),
('DEFAULT', 'CVM-C6-HYGON-4C8G', 'CVM', 'C6_COMPUTE', 'MEMORY', 'DDR4_8G', 1, 1, 'system'),

-- CBS SKU组合
('DEFAULT', 'CBS-SSD-100G', 'CBS', 'CLOUD_SSD', 'STORAGE', 'SSD_100G', 1, 1, 'system'),
('DEFAULT', 'CBS-SSD-500G', 'CBS', 'CLOUD_SSD', 'STORAGE', 'SSD_500G', 1, 1, 'system'),
('DEFAULT', 'CBS-HDD-500G', 'CBS', 'CLOUD_PREMIUM', 'STORAGE', 'HDD_500G', 1, 1, 'system'),

-- CLB SKU组合
('DEFAULT', 'CLB-PUBLIC-STANDARD', 'CLB', 'PUBLIC_CLB', 'INSTANCE', 'CLB_STANDARD', 1, 1, 'system'),
('DEFAULT', 'CLB-PUBLIC-STANDARD', 'CLB', 'PUBLIC_CLB', 'LCU', 'LCU_STANDARD', 1, 1, 'system'),
('DEFAULT', 'CLB-PRIVATE', 'CLB', 'PRIVATE_CLB', 'INSTANCE', 'CLB_INTERNAL', 1, 1, 'system'),

-- EIP SKU组合
('DEFAULT', 'EIP-BGP-5M', 'EIP', 'BGP', 'BANDWIDTH', 'BW_5M', 1, 1, 'system'),
('DEFAULT', 'EIP-BGP-10M', 'EIP', 'BGP', 'BANDWIDTH', 'BW_10M', 1, 1, 'system'),

-- NAT SKU组合
('DEFAULT', 'NAT-SMALL', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_SMALL', 1, 1, 'system'),
('DEFAULT', 'NAT-MEDIUM', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_MEDIUM', 1, 1, 'system'),

-- SaaS订阅组合
('DEFAULT', 'APP-BASIC-MONTHLY', 'APP_SUBSCRIPTION', 'BASIC', 'SUBSCRIPTION', 'MONTHLY', 1, 1, 'system'),
('DEFAULT', 'APP-BASIC-YEARLY', 'APP_SUBSCRIPTION', 'BASIC', 'SUBSCRIPTION', 'YEARLY', 12, 1, 'system'),
('DEFAULT', 'APP-PRO-MONTHLY', 'APP_SUBSCRIPTION', 'PROFESSIONAL', 'SUBSCRIPTION', 'MONTHLY', 1, 1, 'system'),
('DEFAULT', 'APP-PRO-YEARLY', 'APP_SUBSCRIPTION', 'PROFESSIONAL', 'SUBSCRIPTION', 'YEARLY', 12, 1, 'system'),
('DEFAULT', 'APP-ENT-YEARLY', 'APP_SUBSCRIPTION', 'ENTERPRISE', 'SUBSCRIPTION', 'YEARLY', 12, 1, 'system'),

-- 账号包组合
('DEFAULT', 'APP-ACCOUNT-5', 'APP_ACCOUNT', 'STANDARD', 'USER_LICENSE', 'USER_5', 1, 1, 'system'),
('DEFAULT', 'APP-ACCOUNT-10', 'APP_ACCOUNT', 'STANDARD', 'USER_LICENSE', 'USER_10', 1, 1, 'system'),
('DEFAULT', 'APP-ACCOUNT-50', 'APP_ACCOUNT', 'STANDARD', 'USER_LICENSE', 'USER_50', 1, 1, 'system');


-- ============================================================
-- 初始化数据：sku_pricing（SKU定价）
-- ============================================================

INSERT INTO sku_pricing 
(tenant_id, sku_code, pricing_model, billing_period, period_count, original_price, sale_price, currency, discount_rate, status, created_by)
VALUES
-- CVM 按量付费（小时价）
('DEFAULT', 'CVM-S5-2C4G', 'PAY_AS_GO', 'HOURLY', 1, 1.68, 1.68, 'CNY', 1.00, 'ACTIVE', 'system'),
('DEFAULT', 'CVM-S5-4C8G', 'PAY_AS_GO', 'HOURLY', 1, 2.88, 2.88, 'CNY', 1.00, 'ACTIVE', 'system'),
('DEFAULT', 'CVM-S5-8C16G', 'PAY_AS_GO', 'HOURLY', 1, 5.12, 5.12, 'CNY', 1.00, 'ACTIVE', 'system'),
('DEFAULT', 'CVM-C6-HYGON-4C8G', 'PAY_AS_GO', 'HOURLY', 1, 2.45, 2.45, 'CNY', 1.00, 'ACTIVE', 'system'),

-- CVM 包月
('DEFAULT', 'CVM-S5-2C4G', 'PREPAID', 'MONTHLY', 1, 1200, 999, 'CNY', 0.83, 'ACTIVE', 'system'),
('DEFAULT', 'CVM-S5-4C8G', 'PREPAID', 'MONTHLY', 1, 2100, 1699, 'CNY', 0.81, 'ACTIVE', 'system'),
('DEFAULT', 'CVM-S5-8C16G', 'PREPAID', 'MONTHLY', 1, 3700, 2999, 'CNY', 0.81, 'ACTIVE', 'system'),

-- CVM 包年
('DEFAULT', 'CVM-S5-2C4G', 'PREPAID', 'YEARLY', 1, 14400, 9588, 'CNY', 0.67, 'ACTIVE', 'system'),
('DEFAULT', 'CVM-S5-4C8G', 'PREPAID', 'YEARLY', 1, 25200, 16788, 'CNY', 0.67, 'ACTIVE', 'system'),
('DEFAULT', 'CVM-S5-8C16G', 'PREPAID', 'YEARLY', 1, 44400, 29588, 'CNY', 0.67, 'ACTIVE', 'system'),

-- CBS 按量
('DEFAULT', 'CBS-SSD-100G', 'PAY_AS_GO', 'HOURLY', 1, 0.10, 0.10, 'CNY', 1.00, 'ACTIVE', 'system'),
('DEFAULT', 'CBS-SSD-500G', 'PAY_AS_GO', 'HOURLY', 1, 0.50, 0.50, 'CNY', 1.00, 'ACTIVE', 'system'),

-- CBS 包月
('DEFAULT', 'CBS-SSD-100G', 'PREPAID', 'MONTHLY', 1, 80, 68, 'CNY', 0.85, 'ACTIVE', 'system'),
('DEFAULT', 'CBS-SSD-500G', 'PREPAID', 'MONTHLY', 1, 400, 320, 'CNY', 0.80, 'ACTIVE', 'system'),

-- CLB
('DEFAULT', 'CLB-PUBLIC-STANDARD', 'PAY_AS_GO', 'HOURLY', 1, 0.028, 0.028, 'CNY', 1.00, 'ACTIVE', 'system'),
('DEFAULT', 'CLB-PRIVATE', 'PAY_AS_GO', 'HOURLY', 1, 0.01, 0.01, 'CNY', 1.00, 'ACTIVE', 'system'),

-- EIP
('DEFAULT', 'EIP-BGP-5M', 'PAY_AS_GO', 'HOURLY', 1, 0.15, 0.15, 'CNY', 1.00, 'ACTIVE', 'system'),
('DEFAULT', 'EIP-BGP-5M', 'PREPAID', 'MONTHLY', 1, 115, 99, 'CNY', 0.86, 'ACTIVE', 'system'),
('DEFAULT', 'EIP-BGP-10M', 'PAY_AS_GO', 'HOURLY', 1, 0.285, 0.285, 'CNY', 1.00, 'ACTIVE', 'system'),
('DEFAULT', 'EIP-BGP-10M', 'PREPAID', 'MONTHLY', 1, 220, 189, 'CNY', 0.86, 'ACTIVE', 'system'),

-- NAT
('DEFAULT', 'NAT-SMALL', 'PAY_AS_GO', 'HOURLY', 1, 0.05, 0.05, 'CNY', 1.00, 'ACTIVE', 'system'),
('DEFAULT', 'NAT-MEDIUM', 'PAY_AS_GO', 'HOURLY', 1, 0.10, 0.10, 'CNY', 1.00, 'ACTIVE', 'system'),
('DEFAULT', 'NAT-SMALL', 'PREPAID', 'MONTHLY', 1, 40, 36, 'CNY', 0.90, 'ACTIVE', 'system'),
('DEFAULT', 'NAT-MEDIUM', 'PREPAID', 'MONTHLY', 1, 80, 72, 'CNY', 0.90, 'ACTIVE', 'system'),

-- SaaS 订阅定价
('DEFAULT', 'APP-BASIC-MONTHLY', 'SUBSCRIPTION', 'MONTHLY', 1, 99, 99, 'CNY', 1.00, 'ACTIVE', 'system'),
('DEFAULT', 'APP-BASIC-YEARLY', 'SUBSCRIPTION', 'YEARLY', 1, 1188, 948, 'CNY', 0.80, 'ACTIVE', 'system'),
('DEFAULT', 'APP-PRO-MONTHLY', 'SUBSCRIPTION', 'MONTHLY', 1, 299, 299, 'CNY', 1.00, 'ACTIVE', 'system'),
('DEFAULT', 'APP-PRO-YEARLY', 'SUBSCRIPTION', 'YEARLY', 1, 3588, 2868, 'CNY', 0.80, 'ACTIVE', 'system'),
('DEFAULT', 'APP-ENT-YEARLY', 'SUBSCRIPTION', 'YEARLY', 1, 11988, 9588, 'CNY', 0.80, 'ACTIVE', 'system'),

-- 账号包定价
('DEFAULT', 'APP-ACCOUNT-5', 'SUBSCRIPTION', 'MONTHLY', 1, 150, 125, 'CNY', 0.83, 'ACTIVE', 'system'),
('DEFAULT', 'APP-ACCOUNT-10', 'SUBSCRIPTION', 'MONTHLY', 1, 300, 200, 'CNY', 0.67, 'ACTIVE', 'system'),
('DEFAULT', 'APP-ACCOUNT-50', 'SUBSCRIPTION', 'MONTHLY', 1, 1500, 750, 'CNY', 0.50, 'ACTIVE', 'system');
