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
    `pricing_strategy_code` VARCHAR(64) COMMENT '默认定价策略编码',
    `billing_strategy_code` VARCHAR(64) COMMENT '默认计费策略编码',

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
-- Table 5: sku_pricing SKU定价表(四维度收费+版本化+区域)
-- ----------------------------
DROP TABLE IF EXISTS `sku_pricing`;
CREATE TABLE `sku_pricing` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` VARCHAR(64) NOT NULL COMMENT '租户ID',

    -- 业务标识
    `pricing_code` VARCHAR(64) NOT NULL COMMENT '定价编码: POSTPAID-MONTHLY-LINEAR',

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

    -- ==================== 策略驱动 ====================

    `pricing_strategy_code` VARCHAR(64) COMMENT '定价策略编码(覆盖SKU默认)',
    `billing_strategy_code` VARCHAR(64) COMMENT '计费策略编码(覆盖SKU默认)',
    `refund_policy` VARCHAR(32) DEFAULT 'PRO_RATA' COMMENT '退款政策: PRO_RATA/NON_REFUNDABLE',

    -- ==================== 价格信息 ====================

    `unit_price` DECIMAL(20,6) NOT NULL COMMENT '单价',
    `original_price` DECIMAL(20,2) COMMENT '原价(用于展示折扣)',
    `sale_price` DECIMAL(20,2) NOT NULL COMMENT '售价',
    `currency` VARCHAR(8) DEFAULT 'CNY' COMMENT '币种',
    `discount_rate` DECIMAL(10,4) DEFAULT 1.0000 COMMENT '折扣率: 0.85表示85折',

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
    KEY `idx_pricing_strategy` (`pricing_strategy_code`),
    KEY `idx_is_current` (`pricing_code`, `is_current`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定价模板表(可复用)';


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

    -- 应用范围
    `apply_scope` VARCHAR(32) NOT NULL COMMENT '应用范围: ALL/SKU/PRODUCT_LINE',
    `apply_scope_value` VARCHAR(128) COMMENT '范围值: SKU编码或产品线',

    -- 策略配置(JSON)
    `strategy_config` JSON COMMENT '策略配置(阶梯区间/区域系数等)',

    -- 优先级
    `priority` INT DEFAULT 0 COMMENT '优先级',

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
-- Table 7: pricing_strategy_param 定价策略参数细目(阶梯配置)
-- ----------------------------
DROP TABLE IF EXISTS `pricing_strategy_param`;
CREATE TABLE `pricing_strategy_param` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` VARCHAR(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '租户ID',

    -- 关联定价模板(可选,直接关联定价)
    `pricing_code` VARCHAR(64) COMMENT '关联sku_pricing.pricing_code',
    `pricing_revision` VARCHAR(32) COMMENT '关联sku_pricing.revision',

    -- 关联策略模板(可选,关联策略模板)
    `strategy_code` VARCHAR(64) COMMENT '关联pricing_strategy.strategy_code',

    -- 阶梯区间
    `range_start` DECIMAL(20,6) NOT NULL COMMENT '区间起始',
    `range_end` DECIMAL(20,6) COMMENT '区间结束(NULL为无穷大)',

    -- 价格
    `unit_price` DECIMAL(20,6) NOT NULL COMMENT '阶梯单价',
    `fixed_amount` DECIMAL(20,6) DEFAULT 0.000000 COMMENT '固定附加费/起步价',

    -- 排序
    `sort_order` INT DEFAULT 0 COMMENT '排序',

    -- 审计字段
    `created_by` VARCHAR(64) COMMENT '创建者',
    `dt_created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (`id`),
    KEY `idx_pricing` (`tenant_id`, `pricing_code`, `pricing_revision`),
    KEY `idx_strategy` (`tenant_id`, `strategy_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定价策略参数细目(阶梯配置)';


-- ========================================================================
-- 3. 交付层 (2表)
-- ========================================================================

-- ----------------------------
-- Table 8: resource_pricing_snapshot 资源实例定价快照表(交付时刻真相)
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
-- Table 9: pricing_change_log 价格变更记录表(审计追踪)
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
-- Table 10: region_config 区域配置表(物理机房)
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
-- Table 11: availability_zone_config 可用区配置表
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
-- Table 12: sku_region_mapping SKU区域可用区映射表
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


-- ========================================================================
-- 5. 初始化数据
-- ========================================================================

-- 初始化区域数据
INSERT INTO `region_config` (`region_code`, `region_name`, `region_type`, `country_code`, `price_factor`, `status`, `sort_order`) VALUES
('cn-beijing', '华北-北京', 'PUBLIC', 'CN', 1.0000, 'ACTIVE', 1),
('cn-shanghai', '华东-上海', 'PUBLIC', 'CN', 1.2000, 'ACTIVE', 2),
('cn-guangzhou', '华南-广州', 'PUBLIC', 'CN', 0.9500, 'ACTIVE', 3),
('cn-shenzhen', '华南-深圳', 'PUBLIC', 'CN', 1.0000, 'ACTIVE', 4),
('cn-hongkong', '中国香港', 'PUBLIC', 'HK', 1.5000, 'ACTIVE', 5);

-- 初始化可用区数据
INSERT INTO `availability_zone_config` (`region_code`, `zone_code`, `zone_name`, `zone_type`, `status`, `sort_order`) VALUES
('cn-beijing', 'cn-beijing-a', '北京可用区A', 'STANDARD', 'ACTIVE', 1),
('cn-beijing', 'cn-beijing-b', '北京可用区B', 'STANDARD', 'ACTIVE', 2),
('cn-beijing', 'cn-beijing-c', '北京可用区C', 'STANDARD', 'ACTIVE', 3),
('cn-shanghai', 'cn-shanghai-a', '上海可用区A', 'STANDARD', 'ACTIVE', 1),
('cn-shanghai', 'cn-shanghai-b', '上海可用区B', 'STANDARD', 'ACTIVE', 2),
('cn-guangzhou', 'cn-guangzhou-a', '广州可用区A', 'STANDARD', 'ACTIVE', 1),
('cn-shenzhen', 'cn-shenzhen-a', '深圳可用区A', 'STANDARD', 'ACTIVE', 1),
('cn-hongkong', 'cn-hongkong-a', '香港可用区A', 'STANDARD', 'ACTIVE', 1);

-- 初始化定价策略模板
INSERT INTO `pricing_strategy` (`tenant_id`, `strategy_code`, `strategy_name`, `strategy_type`, `apply_scope`, `strategy_config`, `priority`, `status`) VALUES
('DEFAULT', 'LINEAR', '线性计价', 'LINEAR', 'ALL', '{"description": "按单价*用量计算"}', 0, 'ACTIVE'),
('DEFAULT', 'TIERED_CPU_001', 'CPU分层定价策略', 'TIERED', 'PRODUCT_LINE', '{"tiers":[{"minUsage":0,"maxUsage":100,"price":3.64},{"minUsage":100,"maxUsage":500,"price":3.28},{"minUsage":500,"maxUsage":null,"price":2.91}]}', 10, 'ACTIVE'),
('DEFAULT', 'VOLUME_DISCOUNT_001', '批量折扣策略', 'VOLUME_DISCOUNT', 'ALL', '{"tiers":[{"minQty":1,"maxQty":10,"discount":1.0},{"minQty":10,"maxQty":100,"discount":0.9},{"minQty":100,"maxQty":null,"discount":0.8}]}', 20, 'ACTIVE');

SET FOREIGN_KEY_CHECKS = 1;

-- ========================================================================
-- END OF SCRIPT
-- 共12张表: 产品目录层(4) + 定价层(3) + 交付层(2) + 区域管理层(3)
-- ========================================================================
