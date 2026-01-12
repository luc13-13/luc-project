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

-- 1. 清空现有数据
TRUNCATE TABLE sku_pricing;
TRUNCATE TABLE sku_item_combination;
TRUNCATE TABLE product_sku;
TRUNCATE TABLE product_info;

-- 2. 插入优化后的 product_info 数据（单位计费项，specValue=1）
INSERT INTO product_info (
    tenant_id, product_code, sub_product_code, billing_item_code, sub_billing_item_code,
    product_name, sub_product_name, billing_item_name, sub_billing_item_name,
    spec_value, spec_unit, base_price, price_factor, metering_unit, status, sort_order, deleted, created_by, dt_created
) VALUES
-- ==================== 云服务器 CVM ====================
-- 通用型S5
('DEFAULT', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL', '云服务器', '通用型S5', 'CPU', 'Intel处理器', 1, '核', 0.4000, 1.00, '核·小时', 'ACTIVE', 1, 0, 'system', NOW()),
('DEFAULT', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4', '云服务器', '通用型S5', '内存', 'DDR4内存', 1, 'GB', 0.2000, 1.00, 'GB·小时', 'ACTIVE', 2, 0, 'system', NOW()),
('DEFAULT', 'CVM', 'S5_GENERAL', 'SYSTEM_DISK', 'SSD', '云服务器', '通用型S5', '系统盘', 'SSD系统盘', 1, 'GB', 0.0010, 1.00, 'GB·小时', 'ACTIVE', 3, 0, 'system', NOW()),

-- 计算型C6
('DEFAULT', 'CVM', 'C6_COMPUTE', 'CPU', 'HYGON', '云服务器', '计算型C6', 'CPU', '海光处理器', 1, '核', 0.4000, 0.85, '核·小时', 'ACTIVE', 1, 0, 'system', NOW()),
('DEFAULT', 'CVM', 'C6_COMPUTE', 'CPU', 'KUNPENG', '云服务器', '计算型C6', 'CPU', '鲲鹏处理器', 1, '核', 0.4000, 0.90, '核·小时', 'ACTIVE', 2, 0, 'system', NOW()),
('DEFAULT', 'CVM', 'C6_COMPUTE', 'MEMORY', 'DDR4', '云服务器', '计算型C6', '内存', 'DDR4内存', 1, 'GB', 0.2000, 0.85, 'GB·小时', 'ACTIVE', 3, 0, 'system', NOW()),

-- ==================== 云硬盘 CBS ====================
('DEFAULT', 'CBS', 'CLOUD_SSD', 'STORAGE', 'SSD', '云硬盘', 'SSD云盘', '存储容量', 'SSD存储', 1, 'GB', 0.0010, 1.00, 'GB·小时', 'ACTIVE', 1, 0, 'system', NOW()),
('DEFAULT', 'CBS', 'CLOUD_PREMIUM', 'STORAGE', 'HDD', '云硬盘', '高效云盘', '存储容量', 'HDD存储', 1, 'GB', 0.0004, 1.00, 'GB·小时', 'ACTIVE', 2, 0, 'system', NOW()),

-- ==================== 负载均衡 CLB ====================
('DEFAULT', 'CLB', 'PUBLIC_CLB', 'INSTANCE', 'CLB_STANDARD', '负载均衡', '公网负载均衡', '实例费', '标准型', 1, '个', 0.0200, 1.00, '个·小时', 'ACTIVE', 1, 0, 'system', NOW()),
('DEFAULT', 'CLB', 'PUBLIC_CLB', 'BANDWIDTH', 'BW_SHARED', '负载均衡', '公网负载均衡', '带宽费', '共享带宽', 1, 'Mbps', 0.0600, 1.00, 'Mbps·小时', 'ACTIVE', 2, 0, 'system', NOW()),
('DEFAULT', 'CLB', 'PUBLIC_CLB', 'LCU', 'LCU_STANDARD', '负载均衡', '公网负载均衡', '容量费', '标准LCU', 1, 'LCU', 0.0080, 1.00, 'LCU·小时', 'ACTIVE', 3, 0, 'system', NOW()),
('DEFAULT', 'CLB', 'PRIVATE_CLB', 'INSTANCE', 'CLB_INTERNAL', '负载均衡', '内网负载均衡', '实例费', '内网型', 1, '个', 0.0100, 1.00, '个·小时', 'ACTIVE', 1, 0, 'system', NOW()),

-- ==================== 弹性公网IP EIP ====================
('DEFAULT', 'EIP', 'BGP', 'BANDWIDTH', 'BGP_BW', '弹性公网IP', 'BGP线路', '带宽', 'BGP带宽', 1, 'Mbps', 0.0300, 1.00, 'Mbps·小时', 'ACTIVE', 1, 0, 'system', NOW()),
('DEFAULT', 'EIP', 'BGP', 'TRAFFIC', 'TRAFFIC_OUT', '弹性公网IP', 'BGP线路', '流量', '出流量', 1, 'GB', 0.8000, 1.00, 'GB', 'ACTIVE', 2, 0, 'system', NOW()),
('DEFAULT', 'EIP', 'BGP', 'IDLE', 'IP_IDLE', '弹性公网IP', 'BGP线路', '闲置费', 'IP闲置', 1, '个', 0.0100, 1.00, '个·小时', 'ACTIVE', 3, 0, 'system', NOW()),

-- ==================== NAT网关 NAT ====================
('DEFAULT', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_SMALL', 'NAT网关', 'NAT网关', '实例费', '小型', 1, '个', 0.0500, 1.00, '个·小时', 'ACTIVE', 1, 0, 'system', NOW()),
('DEFAULT', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_MEDIUM', 'NAT网关', 'NAT网关', '实例费', '中型', 1, '个', 0.1000, 1.00, '个·小时', 'ACTIVE', 2, 0, 'system', NOW()),
('DEFAULT', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_LARGE', 'NAT网关', 'NAT网关', '实例费', '大型', 1, '个', 0.2000, 1.00, '个·小时', 'ACTIVE', 3, 0, 'system', NOW()),
('DEFAULT', 'NAT', 'NAT_GATEWAY', 'TRAFFIC', 'NAT_TRAFFIC', 'NAT网关', 'NAT网关', '流量费', '数据处理', 1, 'GB', 0.0200, 1.00, 'GB', 'ACTIVE', 4, 0, 'system', NOW()),

-- ==================== 应用订阅 APP_SUBSCRIPTION ====================
('DEFAULT', 'APP_SUBSCRIPTION', 'BASIC', 'SUBSCRIPTION', 'MONTHLY', '应用订阅', '基础版', '订阅费', '月度订阅', 1, '月', 99.0000, 1.00, '月', 'ACTIVE', 1, 0, 'system', NOW()),
('DEFAULT', 'APP_SUBSCRIPTION', 'BASIC', 'SUBSCRIPTION', 'QUARTERLY', '应用订阅', '基础版', '订阅费', '季度订阅', 3, '月', 89.0000, 1.00, '月', 'ACTIVE', 2, 0, 'system', NOW()),
('DEFAULT', 'APP_SUBSCRIPTION', 'BASIC', 'SUBSCRIPTION', 'YEARLY', '应用订阅', '基础版', '订阅费', '年度订阅', 12, '月', 79.0000, 1.00, '月', 'ACTIVE', 3, 0, 'system', NOW()),
('DEFAULT', 'APP_SUBSCRIPTION', 'PROFESSIONAL', 'SUBSCRIPTION', 'MONTHLY', '应用订阅', '专业版', '订阅费', '月度订阅', 1, '月', 299.0000, 1.00, '月', 'ACTIVE', 1, 0, 'system', NOW()),
('DEFAULT', 'APP_SUBSCRIPTION', 'PROFESSIONAL', 'SUBSCRIPTION', 'QUARTERLY', '应用订阅', '专业版', '订阅费', '季度订阅', 3, '月', 269.0000, 1.00, '月', 'ACTIVE', 2, 0, 'system', NOW()),
('DEFAULT', 'APP_SUBSCRIPTION', 'PROFESSIONAL', 'SUBSCRIPTION', 'YEARLY', '应用订阅', '专业版', '订阅费', '年度订阅', 12, '月', 239.0000, 1.00, '月', 'ACTIVE', 3, 0, 'system', NOW()),
('DEFAULT', 'APP_SUBSCRIPTION', 'ENTERPRISE', 'SUBSCRIPTION', 'MONTHLY', '应用订阅', '企业版', '订阅费', '月度订阅', 1, '月', 999.0000, 1.00, '月', 'ACTIVE', 1, 0, 'system', NOW()),
('DEFAULT', 'APP_SUBSCRIPTION', 'ENTERPRISE', 'SUBSCRIPTION', 'YEARLY', '应用订阅', '企业版', '订阅费', '年度订阅', 12, '月', 799.0000, 1.00, '月', 'ACTIVE', 2, 0, 'system', NOW());

-- 3. 插入 product_sku 数据（可售卖单元示例）
INSERT INTO product_sku (
    tenant_id, sku_code, sku_name, product_code, sub_product_code, sku_type, saleable, visible, status, deleted, created_by, dt_created
) VALUES
-- 云服务器SKU
('DEFAULT', 'CVM-S5-2C4G', 'S5通用型 2核4G', 'CVM', 'S5_GENERAL', 'INSTANCE', 1, 1, 'ACTIVE', 0, 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', 'S5通用型 4核8G', 'CVM', 'S5_GENERAL', 'INSTANCE', 1, 1, 'ACTIVE', 0, 'system', NOW()),
('DEFAULT', 'CVM-S5-8C16G', 'S5通用型 8核16G', 'CVM', 'S5_GENERAL', 'INSTANCE', 1, 1, 'ACTIVE', 0, 'system', NOW()),
('DEFAULT', 'CVM-C6-4C8G-HYGON', 'C6计算型 4核8G (海光)', 'CVM', 'C6_COMPUTE', 'INSTANCE', 1, 1, 'ACTIVE', 0, 'system', NOW()),
-- 云硬盘SKU
('DEFAULT', 'CBS-SSD-100G', 'SSD云盘 100GB', 'CBS', 'CLOUD_SSD', 'ADDON', 1, 1, 'ACTIVE', 0, 'system', NOW()),
('DEFAULT', 'CBS-SSD-500G', 'SSD云盘 500GB', 'CBS', 'CLOUD_SSD', 'ADDON', 1, 1, 'ACTIVE', 0, 'system', NOW()),
-- 弹性公网IP SKU
('DEFAULT', 'EIP-BGP-5M', 'BGP弹性IP 5Mbps', 'EIP', 'BGP', 'ADDON', 1, 1, 'ACTIVE', 0, 'system', NOW());

-- 4. 插入 sku_item_combination 数据（SKU与计费项的组合）
INSERT INTO sku_item_combination (
    tenant_id, sku_code, product_code, sub_product_code, billing_item_code, sub_billing_item_code, quantity, pricing_included, created_by, dt_created
) VALUES
-- CVM-S5-2C4G: 2核CPU + 4GB内存 + 40GB系统盘
('DEFAULT', 'CVM-S5-2C4G', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL', 2, 1, 'system', NOW()),
('DEFAULT', 'CVM-S5-2C4G', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4', 4, 1, 'system', NOW()),
('DEFAULT', 'CVM-S5-2C4G', 'CVM', 'S5_GENERAL', 'SYSTEM_DISK', 'SSD', 40, 1, 'system', NOW()),

-- CVM-S5-4C8G: 4核CPU + 8GB内存 + 40GB系统盘
('DEFAULT', 'CVM-S5-4C8G', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL', 4, 1, 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4', 8, 1, 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', 'CVM', 'S5_GENERAL', 'SYSTEM_DISK', 'SSD', 40, 1, 'system', NOW()),

-- CVM-S5-8C16G: 8核CPU + 16GB内存 + 80GB系统盘
('DEFAULT', 'CVM-S5-8C16G', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL', 8, 1, 'system', NOW()),
('DEFAULT', 'CVM-S5-8C16G', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4', 16, 1, 'system', NOW()),
('DEFAULT', 'CVM-S5-8C16G', 'CVM', 'S5_GENERAL', 'SYSTEM_DISK', 'SSD', 80, 1, 'system', NOW()),

-- CVM-C6-4C8G-HYGON: 4核海光CPU + 8GB内存
('DEFAULT', 'CVM-C6-4C8G-HYGON', 'CVM', 'C6_COMPUTE', 'CPU', 'HYGON', 4, 1, 'system', NOW()),
('DEFAULT', 'CVM-C6-4C8G-HYGON', 'CVM', 'C6_COMPUTE', 'MEMORY', 'DDR4', 8, 1, 'system', NOW()),

-- CBS-SSD-100G: 100GB SSD存储
('DEFAULT', 'CBS-SSD-100G', 'CBS', 'CLOUD_SSD', 'STORAGE', 'SSD', 100, 1, 'system', NOW()),

-- CBS-SSD-500G: 500GB SSD存储
('DEFAULT', 'CBS-SSD-500G', 'CBS', 'CLOUD_SSD', 'STORAGE', 'SSD', 500, 1, 'system', NOW()),

-- EIP-BGP-5M: 5Mbps BGP带宽
('DEFAULT', 'EIP-BGP-5M', 'EIP', 'BGP', 'BANDWIDTH', 'BGP_BW', 5, 1, 'system', NOW());

-- 5. 插入 sku_pricing 数据（SKU定价示例）
INSERT INTO sku_pricing (
    tenant_id, sku_code, pricing_model, billing_period, period_count, original_price, sale_price, currency, discount_rate, status, priority, created_by, dt_created
) VALUES
-- 按量付费（小时）
('DEFAULT', 'CVM-S5-2C4G', 'PAY_AS_GO', 'HOURLY', 1, 0.88, 0.88, 'CNY', 1.00, 'ACTIVE', 1, 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', 'PAY_AS_GO', 'HOURLY', 1, 1.76, 1.76, 'CNY', 1.00, 'ACTIVE', 1, 'system', NOW()),
('DEFAULT', 'CVM-S5-8C16G', 'PAY_AS_GO', 'HOURLY', 1, 3.52, 3.52, 'CNY', 1.00, 'ACTIVE', 1, 'system', NOW()),
('DEFAULT', 'CVM-C6-4C8G-HYGON', 'PAY_AS_GO', 'HOURLY', 1, 1.50, 1.50, 'CNY', 1.00, 'ACTIVE', 1, 'system', NOW()),
('DEFAULT', 'CBS-SSD-100G', 'PAY_AS_GO', 'HOURLY', 1, 0.10, 0.10, 'CNY', 1.00, 'ACTIVE', 1, 'system', NOW()),
('DEFAULT', 'EIP-BGP-5M', 'PAY_AS_GO', 'HOURLY', 1, 0.15, 0.15, 'CNY', 1.00, 'ACTIVE', 1, 'system', NOW()),

-- 包月定价
('DEFAULT', 'CVM-S5-4C8G', 'PREPAID', 'MONTHLY', 1, 1267.20, 1140.48, 'CNY', 0.90, 'ACTIVE', 2, 'system', NOW()),
('DEFAULT', 'CVM-S5-8C16G', 'PREPAID', 'MONTHLY', 1, 2534.40, 2280.96, 'CNY', 0.90, 'ACTIVE', 2, 'system', NOW());
