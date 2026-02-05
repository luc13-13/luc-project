-- ========================================================================
-- Product-Center 融合设计方案
-- 结合 Gemini(版本化) + Qoder(四维度收费) + 完整区域体系
-- 表数量: 12张表 (产品目录4 + 定价3 + 交付2 + 区域管理3)
-- 时间: 2026-01-28
-- ========================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS product_center DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE product_center;

-- ========================================================================
-- 1. 产品目录层 (4表)
-- ========================================================================

-- ----------------------------
-- Table 1: product_info 产品资源信息主档(四层结构+版本化)
-- ----------------------------
DROP TABLE IF EXISTS `product_info`;
CREATE TABLE `product_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` VARCHAR(64) NOT NULL COMMENT '租户ID',

    -- 四层产品结构（寻址标识）
    `product_code` VARCHAR(64) NOT NULL COMMENT '产品编码: CVM/CBS/CLB',
    `sub_product_code` VARCHAR(64) NOT NULL COMMENT '规格族编码: S5_GENERAL/C6_COMPUTE',
    `billing_item_code` VARCHAR(64) NOT NULL COMMENT '计费项编码: CPU/MEMORY/STORAGE',
    `sub_billing_item_code` VARCHAR(64) NOT NULL COMMENT '计费规格编码: INTEL_4C/HYGON_4C',

    -- 名称
    `product_name` VARCHAR(128) NOT NULL COMMENT '产品名称',
    `sub_product_name` VARCHAR(128) NOT NULL COMMENT '规格族名称',
    `billing_item_name` VARCHAR(128) NOT NULL COMMENT '计费项名称',
    `sub_billing_item_name` VARCHAR(128) NOT NULL COMMENT '计费规格名称',

    -- 规格属性（驱动计量逻辑）
    `spec_value` DECIMAL(20,6) COMMENT '规格值: 4, 8, 100',
    `spec_unit` VARCHAR(32) COMMENT '规格单位: 核, GB, Mbps',
    `metering_unit` VARCHAR(32) COMMENT '计量展示单位: 核·小时, GB·月',

    -- 状态与管理
    `status` VARCHAR(32) DEFAULT 'ACTIVE' COMMENT '状态: DRAFT/ACTIVE/INACTIVE',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0未删除 1已删除',

    -- 审计字段
    `created_by` VARCHAR(64) COMMENT '创建者',
    `dt_created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_by` VARCHAR(64) COMMENT '更新者',
    `dt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_four_layer` (`tenant_id`, `product_code`, `sub_product_code`, 
                                         `billing_item_code`, `sub_billing_item_code`, `deleted`),
    KEY `idx_product_code` (`tenant_id`, `product_code`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品资源信息主档(四层结构)';


-- ----------------------------
-- Table 2: product_sku 产品SKU表(版本化+基准定价)
-- ----------------------------
DROP TABLE IF EXISTS `product_sku`;
CREATE TABLE `product_sku` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` VARCHAR(64) NOT NULL COMMENT '租户ID',

    -- SKU基本信息
    `sku_code` VARCHAR(128) NOT NULL COMMENT 'SKU编码: CVM-S5-4C8G',
    `sku_name` VARCHAR(256) COMMENT 'SKU名称: 通用型S5 4核8G',

    -- SKU版本(时间戳字符串,系统生成)
    `revision` VARCHAR(32) NOT NULL DEFAULT '20260101000000' COMMENT 'SKU版本号: yyyyMMddHHmmss',

    -- SKU类型
    `sku_type` VARCHAR(32) DEFAULT 'INSTANCE' COMMENT 'SKU类型: INSTANCE/ADDON/BUNDLE/SUBSCRIPTION',

    -- 基准定价
    `base_unit_price` DECIMAL(20,6) NOT NULL COMMENT '基准单价',
    `currency` VARCHAR(8) DEFAULT 'CNY' COMMENT '币种',

    -- 售卖控制
    `saleable` TINYINT DEFAULT 1 COMMENT '是否可售: 1是 0否',
    `visible` TINYINT DEFAULT 1 COMMENT '是否可见: 1是 0否',
    `quota_limit` INT COMMENT '默认配额限制',

    -- 版本状态
    `is_current` TINYINT DEFAULT 1 COMMENT '是否当前主版本: 1是 0否',
    `effective_time` DATETIME COMMENT '生效时间',
    `expiry_time` DATETIME COMMENT '失效时间',

    -- 状态
    `status` VARCHAR(32) DEFAULT 'ACTIVE' COMMENT '状态: DRAFT/ACTIVE/INACTIVE',
    `publish_time` DATETIME COMMENT '上架时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',

    -- 审计字段
    `created_by` VARCHAR(64) COMMENT '创建者',
    `dt_created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_by` VARCHAR(64) COMMENT '更新者',
    `dt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sku_revision` (`tenant_id`, `sku_code`, `revision`, `deleted`),
    KEY `idx_status_saleable` (`status`, `saleable`),
    KEY `idx_is_current` (`sku_code`, `is_current`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品SKU表(版本化+基准定价)';


-- ----------------------------
-- Table 3: sku_item_combination SKU计费项组合BOM表
-- ----------------------------
DROP TABLE IF EXISTS `sku_item_combination`;
CREATE TABLE `sku_item_combination` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` VARCHAR(64) NOT NULL COMMENT '租户ID',
    `sku_code` VARCHAR(128) NOT NULL COMMENT 'SKU编码',
    `sku_revision` VARCHAR(32) NOT NULL COMMENT '关联SKU版本号',

    -- 四层计费项编码
    `product_code` VARCHAR(64) NOT NULL COMMENT '产品编码',
    `sub_product_code` VARCHAR(64) NOT NULL COMMENT '规格族编码',
    `billing_item_code` VARCHAR(64) NOT NULL COMMENT '计费项编码',
    `sub_billing_item_code` VARCHAR(64) NOT NULL COMMENT '计费规格编码',

    -- 数量与权重
    `quantity` DECIMAL(20,6) NOT NULL DEFAULT 1.000000 COMMENT '数量/份数',
    `pricing_included` TINYINT DEFAULT 1 COMMENT '是否计入SKU定价: 1是 0否',

    -- 审计字段
    `created_by` VARCHAR(64) COMMENT '创建者',
    `dt_created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_by` VARCHAR(64) COMMENT '更新者',
    `dt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sku_item` (`tenant_id`, `sku_code`, `sku_revision`, `billing_item_code`, `sub_billing_item_code`),
    KEY `idx_sku_revision` (`tenant_id`, `sku_code`, `sku_revision`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='SKU计费项组合BOM表';


-- ----------------------------
-- Table 4: sku_pricing_link SKU与定价关联表
-- ----------------------------
DROP TABLE IF EXISTS `sku_pricing_link`;
CREATE TABLE `sku_pricing_link` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` VARCHAR(64) NOT NULL COMMENT '租户ID',

    -- SKU关联
    `sku_code` VARCHAR(128) NOT NULL COMMENT 'SKU编码',
    `sku_revision` VARCHAR(32) NOT NULL COMMENT 'SKU版本号',

    -- 定价关联
    `pricing_code` VARCHAR(64) NOT NULL COMMENT '定价编码',
    `pricing_revision` VARCHAR(32) NOT NULL COMMENT '定价版本号',

    -- 覆盖配置
    `override_factor` DECIMAL(10,4) DEFAULT 1.0000 COMMENT '覆盖系数(可选)',
    `is_default` TINYINT DEFAULT 0 COMMENT '是否默认收费模式: 1是 0否',

    -- 状态
    `status` VARCHAR(32) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE/INACTIVE',

    -- 审计字段
    `created_by` VARCHAR(64) COMMENT '创建者',
    `dt_created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_by` VARCHAR(64) COMMENT '更新者',
    `dt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sku_pricing_link` (`tenant_id`, `sku_code`, `sku_revision`, `pricing_code`),
    KEY `idx_sku_revision` (`sku_code`, `sku_revision`),
    KEY `idx_pricing_revision` (`pricing_code`, `pricing_revision`),
    KEY `idx_default` (`sku_code`, `sku_revision`, `is_default`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='SKU与定价关联表';


-- ========================================================================
-- 2. 定价层 (3表)
-- ========================================================================

-- ----------------------------
-- Table 5: sku_pricing 定价模板表(收费模式模板,可复用)
-- ----------------------------
DROP TABLE IF EXISTS `sku_pricing`;
CREATE TABLE `sku_pricing` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` VARCHAR(64) NOT NULL COMMENT '租户ID',

    -- 业务标识
    `pricing_code` VARCHAR(64) NOT NULL COMMENT '定价编码: PAY_AS_GO_HOURLY/PREPAID_MONTHLY',

    -- 定价版本(时间戳字符串)
    `revision` VARCHAR(32) NOT NULL DEFAULT '20260101000000' COMMENT '定价版本号: yyyyMMddHHmmss',

    -- ==================== 四维度收费模式 ====================

    -- 维度1: 计量方式
    `metering_mode` VARCHAR(32) NOT NULL COMMENT '计量方式: BY_USAGE/BY_QUOTA',

    -- 维度2: 付费方式
    `payment_mode` VARCHAR(32) NOT NULL COMMENT '付费方式: POSTPAID/PREPAID/SUBSCRIPTION',

    -- 维度3: 计费周期
    `billing_cycle` VARCHAR(32) NOT NULL COMMENT '计费周期: HOURLY/DAILY/MONTHLY/QUARTERLY/YEARLY/ONCE',
    `cycle_count` INT DEFAULT 1 COMMENT '周期数量: 1月/3月/12月',

    -- 维度4: 计费单位
    `billing_unit` VARCHAR(32) NOT NULL COMMENT '计费单位类型: PERIOD/QUANTITY',

    -- ==================== 定价配置 ====================

    `refund_policy` VARCHAR(32) DEFAULT 'PRO_RATA' COMMENT '退款政策: PRO_RATA/NON_REFUNDABLE',
    `discount_rate` DECIMAL(10,4) DEFAULT 1.0000 COMMENT '折扣率: 0.85表示85折',
    `currency` VARCHAR(8) DEFAULT 'CNY' COMMENT '币种',

    -- ==================== 计量配置 ====================

    `metering_unit` VARCHAR(32) COMMENT '计量单位: 核·小时/GB·月/次',
    `metering_precision` INT DEFAULT 2 COMMENT '计量精度: 小数位数',

    -- ==================== 时间与优先级 ====================

    `effective_time` DATETIME NOT NULL COMMENT '生效时间',
    `expiry_time` DATETIME COMMENT '失效时间',
    `is_current` TINYINT DEFAULT 1 COMMENT '是否当前主版本: 1是 0否',
    `priority` INT DEFAULT 0 COMMENT '优先级(数值越大优先级越高)',

    -- 状态与备注
    `status` VARCHAR(32) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE/INACTIVE',
    `remark` TEXT COMMENT '备注说明',

    -- 审计字段
    `created_by` VARCHAR(64) COMMENT '创建者',
    `dt_created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_by` VARCHAR(64) COMMENT '更新者',
    `dt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_pricing_revision` (`tenant_id`, `pricing_code`, `revision`),
    UNIQUE KEY `uk_pricing_dim` (`tenant_id`, `pricing_code`, `metering_mode`, `payment_mode`, `billing_cycle`, `cycle_count`, `revision`),
    KEY `idx_pricing_code` (`pricing_code`),
    KEY `idx_effective_time` (`effective_time`, `expiry_time`),
    KEY `idx_is_current` (`pricing_code`, `is_current`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定价模板表(收费模式模板,可复用)';


-- ----------------------------
-- Table 6: pricing_strategy 定价策略表
-- ----------------------------
DROP TABLE IF EXISTS `pricing_strategy`;
CREATE TABLE `pricing_strategy` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` VARCHAR(64) NOT NULL COMMENT '租户ID',

    `strategy_code` VARCHAR(64) NOT NULL COMMENT '策略编码: TIERED_CPU_001',
    `strategy_name` VARCHAR(128) NOT NULL COMMENT '策略名称',

    -- 策略类型
    `strategy_type` VARCHAR(32) NOT NULL COMMENT '策略类型: LINEAR/TIERED/VOLUME_DISCOUNT/REGION/PROMOTION',

    -- 计算方式
    `calc_method` VARCHAR(32) NOT NULL DEFAULT 'MULTIPLY' COMMENT '计算方式: MULTIPLY(乘法)/SUBTRACT(减法)',

    -- 应用范围
    `apply_scope` VARCHAR(32) NOT NULL DEFAULT 'ALL' COMMENT '应用范围: ALL/SKU/PRODUCT_LINE',
    `apply_scope_value` VARCHAR(128) COMMENT '范围值: SKU编码或产品线',

    -- 优先级
    `priority` INT DEFAULT 0 COMMENT '默认优先级(数值越大越先执行)',

    -- 时间有效性
    `effective_time` DATETIME COMMENT '生效时间',
    `expiry_time` DATETIME COMMENT '失效时间',

    -- 状态
    `status` VARCHAR(32) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE/INACTIVE',
    `remark` TEXT COMMENT '备注说明',

    -- 审计字段
    `created_by` VARCHAR(64) COMMENT '创建者',
    `dt_created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_by` VARCHAR(64) COMMENT '更新者',
    `dt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_strategy_code` (`tenant_id`, `strategy_code`),
    KEY `idx_type_scope` (`strategy_type`, `apply_scope`),
    KEY `idx_effective_time` (`effective_time`, `expiry_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定价策略表';


-- ----------------------------
-- Table 7: pricing_strategy_param 定价策略参数表
-- ----------------------------
DROP TABLE IF EXISTS `pricing_strategy_param`;
CREATE TABLE `pricing_strategy_param` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` VARCHAR(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '租户ID',

    -- 策略关联
    `strategy_code` VARCHAR(64) NOT NULL COMMENT '关联pricing_strategy.strategy_code',

    -- 参数类型
    `param_type` VARCHAR(32) NOT NULL COMMENT '参数类型: TIER/CAP/FLOOR/THRESHOLD/RATE/FIXED/QUANTITY_LIMIT',

    -- 通用参数字段
    `range_start` DECIMAL(20,6) COMMENT '区间起始(阶梯/满减门槛)',
    `range_end` DECIMAL(20,6) COMMENT '区间结束(NULL为无穷大)',
    `value` DECIMAL(20,6) NOT NULL COMMENT '参数值(折扣率/金额/单价等)',

    -- 排序
    `sort_order` INT DEFAULT 0 COMMENT '排序(阶梯顺序)',

    -- 审计字段
    `created_by` VARCHAR(64) COMMENT '创建者',
    `dt_created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (`id`),
    KEY `idx_strategy` (`tenant_id`, `strategy_code`),
    KEY `idx_param_type` (`strategy_code`, `param_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定价策略参数表';


-- ----------------------------
-- Table 8: sku_pricing_strategy_link SKU与策略关联表(支持多策略叠加)
-- ----------------------------
DROP TABLE IF EXISTS `sku_pricing_strategy_link`;
CREATE TABLE `sku_pricing_strategy_link` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` VARCHAR(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '租户ID',

    -- SKU关联
    `sku_code` VARCHAR(128) NOT NULL COMMENT 'SKU编码',
    `sku_revision` VARCHAR(32) NOT NULL COMMENT 'SKU版本号',

    -- 策略关联
    `strategy_code` VARCHAR(64) NOT NULL COMMENT '策略编码',

    -- 执行优先级(覆盖策略默认)
    `priority` INT COMMENT '优先级(NULL使用策略默认值)',

    -- 有效期(支持临时策略)
    `effective_time` DATETIME COMMENT '生效时间',
    `expiry_time` DATETIME COMMENT '失效时间',

    -- 状态
    `status` VARCHAR(32) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE/INACTIVE',

    -- 审计字段
    `created_by` VARCHAR(64) COMMENT '创建者',
    `dt_created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_by` VARCHAR(64) COMMENT '更新者',
    `dt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sku_strategy` (`tenant_id`, `sku_code`, `sku_revision`, `strategy_code`),
    KEY `idx_sku` (`sku_code`, `sku_revision`),
    KEY `idx_strategy` (`strategy_code`),
    KEY `idx_effective` (`effective_time`, `expiry_time`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='SKU与策略关联表(支持多策略叠加)';


-- ========================================================================
-- 3. 交付层 (2表)
-- ========================================================================

-- ----------------------------
-- Table 9: resource_pricing_snapshot 资源实例定价快照表(交付时刻真相)
-- ----------------------------
DROP TABLE IF EXISTS `resource_pricing_snapshot`;
CREATE TABLE `resource_pricing_snapshot` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    -- 资源标识
    `snapshot_id` VARCHAR(64) NOT NULL COMMENT '快照唯一标识',
    `resource_instance_id` VARCHAR(128) NOT NULL COMMENT '资源实例ID',
    `tenant_id` VARCHAR(64) NOT NULL COMMENT '租户ID',

    -- SKU信息(关联版本)
    `sku_code` VARCHAR(128) NOT NULL COMMENT 'SKU编码',
    `sku_revision` VARCHAR(32) NOT NULL COMMENT 'SKU版本号',
    `region` VARCHAR(32) NOT NULL COMMENT '区域',
    `availability_zone` VARCHAR(32) COMMENT '可用区',

    -- 计费模式快照
    `metering_mode` VARCHAR(32) NOT NULL COMMENT '计量方式',
    `payment_mode` VARCHAR(32) NOT NULL COMMENT '付费方式',
    `billing_cycle` VARCHAR(32) NOT NULL COMMENT '计费周期',
    `billing_unit` VARCHAR(32) NOT NULL COMMENT '计费单位',

    -- 价格快照
    `unit_price` DECIMAL(20,6) NOT NULL COMMENT '单价快照',
    `sale_price` DECIMAL(20,2) NOT NULL COMMENT '售价快照',
    `currency` VARCHAR(8) NOT NULL COMMENT '币种',
    `discount_rate` DECIMAL(10,4) COMMENT '折扣率快照',

    -- 计量配置快照
    `metering_unit` VARCHAR(32) COMMENT '计量单位',

    -- 策略快照(JSON展平,支持极端性能读取)
    `strategy_snapshot` JSON COMMENT 'SKU结构+策略参数+营销比率',

    -- 价格锁定策略
    `price_lock_strategy` VARCHAR(32) DEFAULT 'LOCKED' COMMENT '锁价策略: LOCKED/FOLLOW_PLATFORM/RENEWAL_UPDATE',

    -- 锁定时间范围
    `lock_start_time` DATETIME NOT NULL COMMENT '锁定开始时间(生效时间)',
    `lock_end_time` DATETIME COMMENT '锁定结束时间(失效时间)',

    -- 快照版本
    `snapshot_version` INT DEFAULT 1 COMMENT '快照版本号',
    `is_current` TINYINT DEFAULT 1 COMMENT '是否当前有效: 1是 0否',

    -- 变更原因
    `change_reason` VARCHAR(512) COMMENT '变更原因',

    -- 审计字段
    `created_by` VARCHAR(64) COMMENT '创建者',
    `dt_created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_by` VARCHAR(64) COMMENT '更新者',
    `dt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_snapshot` (`snapshot_id`),
    KEY `idx_resource_instance` (`resource_instance_id`),
    KEY `idx_sku_revision` (`sku_code`, `sku_revision`),
    KEY `idx_is_current` (`resource_instance_id`, `is_current`),
    KEY `idx_lock_time` (`lock_start_time`, `lock_end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资源实例定价快照表(交付时刻不可变契约)';


-- ----------------------------
-- Table 10: pricing_change_log 价格变更记录表(审计追踪)
-- ----------------------------
DROP TABLE IF EXISTS `pricing_change_log`;
CREATE TABLE `pricing_change_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` VARCHAR(64) NOT NULL COMMENT '租户ID',

    -- 变更标识
    `change_no` VARCHAR(64) NOT NULL COMMENT '变更单号',
    `sku_code` VARCHAR(128) NOT NULL COMMENT 'SKU编码',
    `region` VARCHAR(32) COMMENT '区域',
    `availability_zone` VARCHAR(32) COMMENT '可用区',

    -- 变更类型
    `change_type` VARCHAR(32) NOT NULL COMMENT '变更类型: CREATE/PRICE_ADJUST/NEW_VERSION/DEPRECATED',

    -- 价格变更
    `old_pricing_id` BIGINT COMMENT '原定价ID',
    `new_pricing_id` BIGINT COMMENT '新定价ID',
    `old_price` DECIMAL(20,6) COMMENT '原价格',
    `new_price` DECIMAL(20,6) COMMENT '新价格',
    `price_change_rate` DECIMAL(10,4) COMMENT '价格变化率',

    -- 版本变更
    `old_revision_id` BIGINT COMMENT '原版本号',
    `new_revision_id` BIGINT COMMENT '新版本号',

    -- 影响策略
    `affect_existing_resources` TINYINT DEFAULT 0 COMMENT '是否影响已交付资源: 0否 1是',
    `affect_strategy` VARCHAR(32) DEFAULT 'NONE' COMMENT '影响策略: NONE/IMMEDIATE/RENEWAL_UPDATE/GRACEFUL',

    -- 生效时间
    `effective_time` DATETIME NOT NULL COMMENT '变更生效时间',

    -- 变更原因
    `change_reason` TEXT COMMENT '变更原因',

    -- 影响范围统计
    `affected_resource_count` INT DEFAULT 0 COMMENT '受影响资源数量',
    `affected_tenant_count` INT DEFAULT 0 COMMENT '受影响租户数量',

    -- 操作信息
    `operator` VARCHAR(64) NOT NULL COMMENT '操作人',
    `operate_time` DATETIME NOT NULL COMMENT '操作时间',

    -- 审计字段
    `dt_created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_change_no` (`change_no`),
    KEY `idx_sku_time` (`sku_code`, `effective_time`),
    KEY `idx_change_type` (`change_type`),
    KEY `idx_tenant_time` (`tenant_id`, `operate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='价格变更记录表(审计追踪)';


-- ========================================================================
-- 4. 区域管理层 (3表)
-- ========================================================================

-- ----------------------------
-- Table 11: region_config 区域配置表(物理机房)
-- ----------------------------
DROP TABLE IF EXISTS `region_config`;
CREATE TABLE `region_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    `region_code` VARCHAR(32) NOT NULL COMMENT '区域编码: cn-beijing/cn-shanghai',
    `region_name` VARCHAR(128) NOT NULL COMMENT '区域名称: 华北-北京',

    -- 区域属性
    `region_type` VARCHAR(32) DEFAULT 'PUBLIC' COMMENT '区域类型: PUBLIC/PRIVATE/HYBRID',
    `country_code` VARCHAR(8) COMMENT '国家代码: CN/US/HK',
    `geographic_location` VARCHAR(256) COMMENT '地理位置描述',

    -- 运营属性
    `operator` VARCHAR(64) COMMENT '运营商/机房供应商',
    `network_latency_tier` VARCHAR(16) DEFAULT 'STANDARD' COMMENT '网络延迟等级: LOW/STANDARD/HIGH',

    -- 价格系数
    `price_factor` DECIMAL(10,4) DEFAULT 1.0000 COMMENT '区域价格系数',

    -- 状态与排序
    `status` VARCHAR(32) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE/INACTIVE/MAINTENANCE',
    `sort_order` INT DEFAULT 0 COMMENT '排序',

    -- 审计字段
    `created_by` VARCHAR(64) COMMENT '创建者',
    `dt_created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_by` VARCHAR(64) COMMENT '更新者',
    `dt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_region_code` (`region_code`),
    KEY `idx_status` (`status`),
    KEY `idx_country` (`country_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='区域配置表(物理机房)';


-- ----------------------------
-- Table 12: availability_zone_config 可用区配置表
-- ----------------------------
DROP TABLE IF EXISTS `availability_zone_config`;
CREATE TABLE `availability_zone_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    -- 所属区域
    `region_code` VARCHAR(32) NOT NULL COMMENT '所属区域编码',

    -- 可用区信息
    `zone_code` VARCHAR(32) NOT NULL COMMENT '可用区编码: cn-beijing-a',
    `zone_name` VARCHAR(128) NOT NULL COMMENT '可用区名称: 北京可用区A',

    -- 可用区属性
    `zone_type` VARCHAR(32) DEFAULT 'STANDARD' COMMENT '可用区类型: STANDARD/EDGE/DEDICATED',
    `data_center` VARCHAR(64) COMMENT '数据中心标识',

    -- 容量与库存状态
    `capacity_status` VARCHAR(32) DEFAULT 'SUFFICIENT' COMMENT '容量状态: SUFFICIENT/LOW/SOLD_OUT',

    -- 价格系数(可用区级别微调)
    `price_factor` DECIMAL(10,4) DEFAULT 1.0000 COMMENT '可用区价格系数(叠加区域系数)',

    -- 状态与排序
    `status` VARCHAR(32) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE/INACTIVE/MAINTENANCE',
    `sort_order` INT DEFAULT 0 COMMENT '排序',

    -- 审计字段
    `created_by` VARCHAR(64) COMMENT '创建者',
    `dt_created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_by` VARCHAR(64) COMMENT '更新者',
    `dt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_zone_code` (`zone_code`),
    KEY `idx_region` (`region_code`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='可用区配置表';


-- ----------------------------
-- Table 13: sku_region_mapping SKU区域可用区映射表
-- ----------------------------
DROP TABLE IF EXISTS `sku_region_mapping`;
CREATE TABLE `sku_region_mapping` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` VARCHAR(64) NOT NULL COMMENT '租户ID',

    -- SKU标识
    `sku_code` VARCHAR(128) NOT NULL COMMENT 'SKU编码',

    -- 区域与可用区
    `region_code` VARCHAR(32) NOT NULL COMMENT '区域编码',
    `zone_code` VARCHAR(32) COMMENT '可用区编码(NULL表示整个区域)',

    -- 可用性控制
    `available` TINYINT DEFAULT 1 COMMENT '是否可用: 1可用 0不可用',
    `saleable` TINYINT DEFAULT 1 COMMENT '是否可售: 1可售 0不可售',
    `inventory_status` VARCHAR(32) DEFAULT 'SUFFICIENT' COMMENT '库存状态: SUFFICIENT/LOW/SOLD_OUT/RESERVED',

    -- 配额限制(区域级别)
    `zone_quota_limit` INT COMMENT '该区域/可用区配额限制',
    `zone_quota_used` INT DEFAULT 0 COMMENT '已使用配额',

    -- 特殊配置(JSON)
    `zone_config` JSON COMMENT '区域特殊配置: 规格限制/性能参数等',

    -- 价格覆盖(区域级别单独定价)
    `price_override` TINYINT DEFAULT 0 COMMENT '是否覆盖默认价格: 0否 1是',
    `override_pricing_id` BIGINT COMMENT '覆盖定价ID(关联sku_pricing)',

    -- 状态
    `status` VARCHAR(32) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE/INACTIVE',

    -- 审计字段
    `created_by` VARCHAR(64) COMMENT '创建者',
    `dt_created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_by` VARCHAR(64) COMMENT '更新者',
    `dt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sku_zone` (`tenant_id`, `sku_code`, `region_code`, `zone_code`),
    KEY `idx_sku` (`sku_code`),
    KEY `idx_region_zone` (`region_code`, `zone_code`, `available`),
    KEY `idx_inventory` (`inventory_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='SKU区域可用区映射表';


SET FOREIGN_KEY_CHECKS = 1;

-- ========================================================================
-- END OF SCRIPT
-- 共12张表: 产品目录层(4) + 定价层(3) + 交付层(2) + 区域管理层(3)
-- ========================================================================
