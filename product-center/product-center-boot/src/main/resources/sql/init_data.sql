-- ========================================================================
-- Product Center 初始化数据脚本
-- 生成时间: 2026-01-31
-- 租户: DEFAULT
-- 创建者: system
-- ========================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ========================================================================
-- 1. 产品目录层数据
-- ========================================================================

-- ----------------------------
-- Table: product_info (产品目录) - 保持现有数据，此处仅供参考
-- ----------------------------
-- 现有数据已包含: CVM, CBS, CLB, EIP, NAT, APP_SUBSCRIPTION

-- ----------------------------
-- Table: product_sku (售卖单元)
-- ----------------------------
TRUNCATE TABLE `product_sku`;
INSERT INTO `product_sku` (`tenant_id`, `sku_code`, `sku_name`, `revision`, `sku_type`, `base_unit_price`, `currency`, `pricing_strategy_code`, `billing_strategy_code`, `saleable`, `visible`, `quota_limit`, `is_current`, `effective_time`, `expiry_time`, `status`, `publish_time`, `created_by`, `dt_created`) VALUES

-- CVM 云服务器 SKU
('DEFAULT', 'CVM-S5-1C1G', '通用型S5 1核1G', '20260131135900', 'INSTANCE', 0.6000, 'CNY', 'LINEAR', NULL, 1, 1, 100, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CVM-S5-2C4G', '通用型S5 2核4G', '20260131135900', 'INSTANCE', 1.6000, 'CNY', 'LINEAR', NULL, 1, 1, 100, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', '通用型S5 4核8G', '20260131135900', 'INSTANCE', 3.2000, 'CNY', 'LINEAR', NULL, 1, 1, 50, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CVM-S5-8C16G', '通用型S5 8核16G', '20260131135900', 'INSTANCE', 6.4000, 'CNY', 'LINEAR', NULL, 1, 1, 20, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CVM-C6-4C8G', '计算型C6 4核8G', '20260131135900', 'INSTANCE', 2.7200, 'CNY', 'LINEAR', NULL, 1, 1, 50, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CVM-C6-8C16G', '计算型C6 8核16G', '20260131135900', 'INSTANCE', 5.4400, 'CNY', 'LINEAR', NULL, 1, 1, 20, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),

-- CBS 云硬盘 SKU
('DEFAULT', 'CBS-SSD-100G', 'SSD云盘 100GB', '20260131135900', 'ADDON', 0.1000, 'CNY', 'LINEAR', NULL, 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CBS-SSD-500G', 'SSD云盘 500GB', '20260131135900', 'ADDON', 0.5000, 'CNY', 'LINEAR', NULL, 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CBS-PREMIUM-100G', '高效云盘 100GB', '20260131135900', 'ADDON', 0.0400, 'CNY', 'LINEAR', NULL, 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CBS-PREMIUM-500G', '高效云盘 500GB', '20260131135900', 'ADDON', 0.2000, 'CNY', 'LINEAR', NULL, 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),

-- CLB 负载均衡 SKU
('DEFAULT', 'CLB-PUBLIC-STANDARD', '公网负载均衡标准版', '20260131135900', 'INSTANCE', 0.0280, 'CNY', 'LINEAR', NULL, 1, 1, 20, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CLB-PRIVATE-STANDARD', '内网负载均衡标准版', '20260131135900', 'INSTANCE', 0.0100, 'CNY', 'LINEAR', NULL, 1, 1, 50, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),

-- EIP 弹性公网IP SKU
('DEFAULT', 'EIP-BGP-1M', '弹性公网IP BGP 1Mbps', '20260131135900', 'ADDON', 0.0300, 'CNY', 'LINEAR', NULL, 1, 1, 20, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'EIP-BGP-5M', '弹性公网IP BGP 5Mbps', '20260131135900', 'ADDON', 0.1500, 'CNY', 'LINEAR', NULL, 1, 1, 20, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),

-- NAT 网关 SKU
('DEFAULT', 'NAT-SMALL', 'NAT网关 小型', '20260131135900', 'INSTANCE', 0.0500, 'CNY', 'LINEAR', NULL, 1, 1, 10, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'NAT-MEDIUM', 'NAT网关 中型', '20260131135900', 'INSTANCE', 0.1000, 'CNY', 'LINEAR', NULL, 1, 1, 5, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'NAT-LARGE', 'NAT网关 大型', '20260131135900', 'INSTANCE', 0.2000, 'CNY', 'LINEAR', NULL, 1, 1, 3, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),

-- APP_SUBSCRIPTION 应用订阅 SKU
('DEFAULT', 'APP-BASIC-MONTHLY', '应用基础版 月付', '20260131135900', 'SUBSCRIPTION', 99.0000, 'CNY', 'LINEAR', NULL, 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'APP-BASIC-YEARLY', '应用基础版 年付', '20260131135900', 'SUBSCRIPTION', 948.0000, 'CNY', 'LINEAR', NULL, 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'APP-PRO-MONTHLY', '应用专业版 月付', '20260131135900', 'SUBSCRIPTION', 299.0000, 'CNY', 'LINEAR', NULL, 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'APP-PRO-YEARLY', '应用专业版 年付', '20260131135900', 'SUBSCRIPTION', 2868.0000, 'CNY', 'LINEAR', NULL, 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'APP-ENT-MONTHLY', '应用企业版 月付', '20260131135900', 'SUBSCRIPTION', 999.0000, 'CNY', 'LINEAR', NULL, 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'APP-ENT-YEARLY', '应用企业版 年付', '20260131135900', 'SUBSCRIPTION', 9588.0000, 'CNY', 'LINEAR', NULL, 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW());


-- ----------------------------
-- Table: sku_item_combination (SKU BOM 组合)
-- ----------------------------
TRUNCATE TABLE `sku_item_combination`;
INSERT INTO `sku_item_combination` (`tenant_id`, `sku_code`, `sku_revision`, `product_code`, `sub_product_code`, `billing_item_code`, `sub_billing_item_code`, `quantity`, `pricing_included`, `created_by`, `dt_created`) VALUES

-- CVM-S5-1C1G: 1核CPU + 1GB内存
('DEFAULT', 'CVM-S5-1C1G', '20260131135900', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL', 1.00, 1, 'system', NOW()),
('DEFAULT', 'CVM-S5-1C1G', '20260131135900', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4', 1.00, 1, 'system', NOW()),

-- CVM-S5-2C4G: 2核CPU + 4GB内存
('DEFAULT', 'CVM-S5-2C4G', '20260131135900', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL', 2.00, 1, 'system', NOW()),
('DEFAULT', 'CVM-S5-2C4G', '20260131135900', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4', 4.00, 1, 'system', NOW()),

-- CVM-S5-4C8G: 4核CPU + 8GB内存
('DEFAULT', 'CVM-S5-4C8G', '20260131135900', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL', 4.00, 1, 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', '20260131135900', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4', 8.00, 1, 'system', NOW()),

-- CVM-S5-8C16G: 8核CPU + 16GB内存
('DEFAULT', 'CVM-S5-8C16G', '20260131135900', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL', 8.00, 1, 'system', NOW()),
('DEFAULT', 'CVM-S5-8C16G', '20260131135900', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4', 16.00, 1, 'system', NOW()),

-- CVM-C6-4C8G: 4核海光CPU + 8GB内存
('DEFAULT', 'CVM-C6-4C8G', '20260131135900', 'CVM', 'C6_COMPUTE', 'CPU', 'HYGON', 4.00, 1, 'system', NOW()),
('DEFAULT', 'CVM-C6-4C8G', '20260131135900', 'CVM', 'C6_COMPUTE', 'MEMORY', 'DDR4', 8.00, 1, 'system', NOW()),

-- CVM-C6-8C16G: 8核海光CPU + 16GB内存
('DEFAULT', 'CVM-C6-8C16G', '20260131135900', 'CVM', 'C6_COMPUTE', 'CPU', 'HYGON', 8.00, 1, 'system', NOW()),
('DEFAULT', 'CVM-C6-8C16G', '20260131135900', 'CVM', 'C6_COMPUTE', 'MEMORY', 'DDR4', 16.00, 1, 'system', NOW()),

-- CBS-SSD-100G
('DEFAULT', 'CBS-SSD-100G', '20260131135900', 'CBS', 'CLOUD_SSD', 'STORAGE', 'SSD', 100.00, 1, 'system', NOW()),

-- CBS-SSD-500G
('DEFAULT', 'CBS-SSD-500G', '20260131135900', 'CBS', 'CLOUD_SSD', 'STORAGE', 'SSD', 500.00, 1, 'system', NOW()),

-- CBS-PREMIUM-100G
('DEFAULT', 'CBS-PREMIUM-100G', '20260131135900', 'CBS', 'CLOUD_PREMIUM', 'STORAGE', 'HDD', 100.00, 1, 'system', NOW()),

-- CBS-PREMIUM-500G
('DEFAULT', 'CBS-PREMIUM-500G', '20260131135900', 'CBS', 'CLOUD_PREMIUM', 'STORAGE', 'HDD', 500.00, 1, 'system', NOW()),

-- CLB-PUBLIC-STANDARD: 公网LB实例费 + 带宽费 + LCU费
('DEFAULT', 'CLB-PUBLIC-STANDARD', '20260131135900', 'CLB', 'PUBLIC_CLB', 'INSTANCE', 'CLB_STANDARD', 1.00, 1, 'system', NOW()),
('DEFAULT', 'CLB-PUBLIC-STANDARD', '20260131135900', 'CLB', 'PUBLIC_CLB', 'BANDWIDTH', 'BW_SHARED', 1.00, 0, 'system', NOW()),
('DEFAULT', 'CLB-PUBLIC-STANDARD', '20260131135900', 'CLB', 'PUBLIC_CLB', 'LCU', 'LCU_STANDARD', 1.00, 0, 'system', NOW()),

-- CLB-PRIVATE-STANDARD: 内网LB实例费
('DEFAULT', 'CLB-PRIVATE-STANDARD', '20260131135900', 'CLB', 'PRIVATE_CLB', 'INSTANCE', 'CLB_INTERNAL', 1.00, 1, 'system', NOW()),

-- EIP-BGP-1M: 带宽费
('DEFAULT', 'EIP-BGP-1M', '20260131135900', 'EIP', 'BGP', 'BANDWIDTH', 'BGP_BW', 1.00, 1, 'system', NOW()),

-- EIP-BGP-5M: 带宽费
('DEFAULT', 'EIP-BGP-5M', '20260131135900', 'EIP', 'BGP', 'BANDWIDTH', 'BGP_BW', 5.00, 1, 'system', NOW()),

-- NAT-SMALL
('DEFAULT', 'NAT-SMALL', '20260131135900', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_SMALL', 1.00, 1, 'system', NOW()),

-- NAT-MEDIUM
('DEFAULT', 'NAT-MEDIUM', '20260131135900', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_MEDIUM', 1.00, 1, 'system', NOW()),

-- NAT-LARGE
('DEFAULT', 'NAT-LARGE', '20260131135900', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_LARGE', 1.00, 1, 'system', NOW()),

-- APP-BASIC-MONTHLY
('DEFAULT', 'APP-BASIC-MONTHLY', '20260131135900', 'APP_SUBSCRIPTION', 'BASIC', 'SUBSCRIPTION', 'MONTHLY', 1.00, 1, 'system', NOW()),

-- APP-BASIC-YEARLY
('DEFAULT', 'APP-BASIC-YEARLY', '20260131135900', 'APP_SUBSCRIPTION', 'BASIC', 'SUBSCRIPTION', 'YEARLY', 12.00, 1, 'system', NOW()),

-- APP-PRO-MONTHLY
('DEFAULT', 'APP-PRO-MONTHLY', '20260131135900', 'APP_SUBSCRIPTION', 'PROFESSIONAL', 'SUBSCRIPTION', 'MONTHLY', 1.00, 1, 'system', NOW()),

-- APP-PRO-YEARLY
('DEFAULT', 'APP-PRO-YEARLY', '20260131135900', 'APP_SUBSCRIPTION', 'PROFESSIONAL', 'SUBSCRIPTION', 'YEARLY', 12.00, 1, 'system', NOW()),

-- APP-ENT-MONTHLY
('DEFAULT', 'APP-ENT-MONTHLY', '20260131135900', 'APP_SUBSCRIPTION', 'ENTERPRISE', 'SUBSCRIPTION', 'MONTHLY', 1.00, 1, 'system', NOW()),

-- APP-ENT-YEARLY
('DEFAULT', 'APP-ENT-YEARLY', '20260131135900', 'APP_SUBSCRIPTION', 'ENTERPRISE', 'SUBSCRIPTION', 'YEARLY', 12.00, 1, 'system', NOW());


-- ========================================================================
-- 2. 定价层数据
-- ========================================================================

-- ----------------------------
-- Table: sku_pricing (定价模板)
-- ----------------------------
TRUNCATE TABLE `sku_pricing`;
INSERT INTO `sku_pricing` (`tenant_id`, `pricing_code`, `revision`, `metering_mode`, `payment_mode`, `billing_cycle`, `cycle_count`, `billing_unit`, `pricing_strategy_code`, `billing_strategy_code`, `refund_policy`, `unit_price`, `original_price`, `sale_price`, `currency`, `discount_rate`, `metering_unit`, `metering_precision`, `effective_time`, `expiry_time`, `is_current`, `priority`, `status`, `remark`, `created_by`, `dt_created`) VALUES

-- 按量计费 - 按小时
('DEFAULT', 'PAY_AS_GO_HOURLY', '20260131135900', 'BY_USAGE', 'POSTPAID', 'HOURLY', 1, 'PERIOD', 'LINEAR', NULL, 'PRO_RATA', 1.0000, NULL, 1.00, 'CNY', 1.0000, '小时', 2, NOW(), NULL, 1, 10, 'ACTIVE', '按量计费-按小时', 'system', NOW()),

-- 包月
('DEFAULT', 'PREPAID_MONTHLY', '20260131135900', 'BY_QUOTA', 'PREPAID', 'MONTHLY', 1, 'PERIOD', 'LINEAR', NULL, 'PRO_RATA', 1.0000, 1.00, 0.85, 'CNY', 0.8500, '月', 0, NOW(), NULL, 1, 20, 'ACTIVE', '包月(85折)', 'system', NOW()),

-- 包季
('DEFAULT', 'PREPAID_QUARTERLY', '20260131135900', 'BY_QUOTA', 'PREPAID', 'QUARTERLY', 1, 'PERIOD', 'LINEAR', NULL, 'PRO_RATA', 1.0000, 3.00, 2.40, 'CNY', 0.8000, '季度', 0, NOW(), NULL, 1, 30, 'ACTIVE', '包季(8折)', 'system', NOW()),

-- 包年
('DEFAULT', 'PREPAID_YEARLY', '20260131135900', 'BY_QUOTA', 'PREPAID', 'YEARLY', 1, 'PERIOD', 'LINEAR', NULL, 'PRO_RATA', 1.0000, 12.00, 8.40, 'CNY', 0.7000, '年', 0, NOW(), NULL, 1, 40, 'ACTIVE', '包年(7折)', 'system', NOW()),

-- 订阅-月付
('DEFAULT', 'SUBSCRIPTION_MONTHLY', '20260131135900', 'BY_QUOTA', 'SUBSCRIPTION', 'MONTHLY', 1, 'PERIOD', 'LINEAR', NULL, 'NON_REFUNDABLE', 1.0000, NULL, 1.00, 'CNY', 1.0000, '月', 0, NOW(), NULL, 1, 50, 'ACTIVE', '月度订阅', 'system', NOW()),

-- 订阅-年付
('DEFAULT', 'SUBSCRIPTION_YEARLY', '20260131135900', 'BY_QUOTA', 'SUBSCRIPTION', 'YEARLY', 1, 'PERIOD', 'LINEAR', NULL, 'NON_REFUNDABLE', 1.0000, 12.00, 9.60, 'CNY', 0.8000, '年', 0, NOW(), NULL, 1, 60, 'ACTIVE', '年度订阅(8折)', 'system', NOW()),

-- 阶梯计费 - CPU
('DEFAULT', 'TIERED_CPU', '20260131135900', 'BY_USAGE', 'POSTPAID', 'HOURLY', 1, 'QUANTITY', 'TIERED_CPU_001', NULL, 'PRO_RATA', 0.4000, NULL, 0.40, 'CNY', 1.0000, '核·小时', 4, NOW(), NULL, 1, 100, 'ACTIVE', 'CPU阶梯计费', 'system', NOW()),

-- 流量计费
('DEFAULT', 'TRAFFIC_PAY_AS_GO', '20260131135900', 'BY_USAGE', 'POSTPAID', 'ONCE', 1, 'QUANTITY', 'LINEAR', NULL, 'NON_REFUNDABLE', 0.8000, NULL, 0.80, 'CNY', 1.0000, 'GB', 6, NOW(), NULL, 1, 70, 'ACTIVE', '流量按量计费', 'system', NOW());


-- ----------------------------
-- Table: sku_pricing_link (SKU与定价模板关联)
-- ----------------------------
TRUNCATE TABLE `sku_pricing_link`;
INSERT INTO `sku_pricing_link` (`tenant_id`, `sku_code`, `sku_revision`, `pricing_code`, `pricing_revision`, `override_factor`, `is_default`, `status`, `created_by`, `dt_created`) VALUES

-- CVM SKU 关联多种定价模式
('DEFAULT', 'CVM-S5-1C1G', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-1C1G', '20260131135900', 'PREPAID_MONTHLY', '20260131135900', 720.0000, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-1C1G', '20260131135900', 'PREPAID_YEARLY', '20260131135900', 8640.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-S5-2C4G', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-2C4G', '20260131135900', 'PREPAID_MONTHLY', '20260131135900', 720.0000, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-2C4G', '20260131135900', 'PREPAID_YEARLY', '20260131135900', 8640.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-S5-4C8G', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', '20260131135900', 'PREPAID_MONTHLY', '20260131135900', 720.0000, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', '20260131135900', 'PREPAID_YEARLY', '20260131135900', 8640.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-S5-8C16G', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-8C16G', '20260131135900', 'PREPAID_MONTHLY', '20260131135900', 720.0000, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-8C16G', '20260131135900', 'PREPAID_YEARLY', '20260131135900', 8640.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-C6-4C8G', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-C6-4C8G', '20260131135900', 'PREPAID_MONTHLY', '20260131135900', 720.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-C6-8C16G', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-C6-8C16G', '20260131135900', 'PREPAID_MONTHLY', '20260131135900', 720.0000, 0, 'ACTIVE', 'system', NOW()),

-- CBS SKU 关联定价
('DEFAULT', 'CBS-SSD-100G', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-SSD-100G', '20260131135900', 'PREPAID_MONTHLY', '20260131135900', 720.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CBS-SSD-500G', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-SSD-500G', '20260131135900', 'PREPAID_MONTHLY', '20260131135900', 720.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CBS-PREMIUM-100G', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-PREMIUM-500G', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),

-- CLB SKU 关联定价
('DEFAULT', 'CLB-PUBLIC-STANDARD', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CLB-PRIVATE-STANDARD', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),

-- EIP SKU 关联定价
('DEFAULT', 'EIP-BGP-1M', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'EIP-BGP-5M', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),

-- NAT SKU 关联定价
('DEFAULT', 'NAT-SMALL', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'NAT-MEDIUM', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'NAT-LARGE', '20260131135900', 'PAY_AS_GO_HOURLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),

-- APP 订阅 SKU 关联定价
('DEFAULT', 'APP-BASIC-MONTHLY', '20260131135900', 'SUBSCRIPTION_MONTHLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'APP-BASIC-YEARLY', '20260131135900', 'SUBSCRIPTION_YEARLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'APP-PRO-MONTHLY', '20260131135900', 'SUBSCRIPTION_MONTHLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'APP-PRO-YEARLY', '20260131135900', 'SUBSCRIPTION_YEARLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'APP-ENT-MONTHLY', '20260131135900', 'SUBSCRIPTION_MONTHLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'APP-ENT-YEARLY', '20260131135900', 'SUBSCRIPTION_YEARLY', '20260131135900', 1.0000, 1, 'ACTIVE', 'system', NOW());


-- ----------------------------
-- Table: pricing_strategy (定价策略)
-- ----------------------------
TRUNCATE TABLE `pricing_strategy`;
INSERT INTO `pricing_strategy` (`tenant_id`, `strategy_code`, `strategy_name`, `strategy_type`, `apply_scope`, `apply_scope_value`, `strategy_config`, `priority`, `effective_time`, `expiry_time`, `status`, `remark`, `created_by`, `dt_created`) VALUES

-- 线性定价策略
('DEFAULT', 'LINEAR', '线性定价', 'LINEAR', 'ALL', NULL, '{"description": "标准线性定价: 单价 × 用量"}', 0, NOW(), NULL, 'ACTIVE', '默认线性定价策略', 'system', NOW()),

-- CPU阶梯定价策略
('DEFAULT', 'TIERED_CPU_001', 'CPU阶梯定价', 'TIERED', 'PRODUCT_LINE', 'CVM', '{"description": "CPU用量越多单价越低", "tiers": [{"minUsage": 0, "maxUsage": 100, "price": 0.40}, {"minUsage": 100, "maxUsage": 500, "price": 0.36}, {"minUsage": 500, "maxUsage": null, "price": 0.32}]}', 10, NOW(), NULL, 'ACTIVE', 'CPU分层定价策略', 'system', NOW()),

-- 批量折扣策略
('DEFAULT', 'VOLUME_DISCOUNT_001', '批量折扣', 'VOLUME_DISCOUNT', 'ALL', NULL, '{"description": "购买数量越多折扣越大", "tiers": [{"minQty": 1, "maxQty": 10, "discount": 1.0}, {"minQty": 10, "maxQty": 100, "discount": 0.9}, {"minQty": 100, "maxQty": null, "discount": 0.8}]}', 20, NOW(), NULL, 'ACTIVE', '批量折扣策略', 'system', NOW()),

-- 区域定价策略-上海加价
('DEFAULT', 'REGION_SHANGHAI', '上海区域定价', 'REGION', 'ALL', NULL, '{"description": "上海区域加价20%", "regionCode": "cn-shanghai", "priceFactor": 1.2}', 5, NOW(), NULL, 'ACTIVE', '上海区域加价策略', 'system', NOW()),

-- 区域定价策略-广州优惠
('DEFAULT', 'REGION_GUANGZHOU', '广州区域定价', 'REGION', 'ALL', NULL, '{"description": "广州区域优惠5%", "regionCode": "cn-guangzhou", "priceFactor": 0.95}', 5, NOW(), NULL, 'ACTIVE', '广州区域优惠策略', 'system', NOW()),

-- 促销策略-首月优惠
('DEFAULT', 'PROMO_FIRST_MONTH', '首月优惠', 'PROMOTION', 'ALL', NULL, '{"description": "新用户首月5折", "discountRate": 0.5, "maxDiscount": 100, "validDays": 30}', 100, NOW(), NULL, 'ACTIVE', '新用户首月优惠', 'system', NOW());


-- ----------------------------
-- Table: pricing_strategy_param (定价策略参数/阶梯配置)
-- ----------------------------
TRUNCATE TABLE `pricing_strategy_param`;
INSERT INTO `pricing_strategy_param` (`tenant_id`, `pricing_code`, `pricing_revision`, `strategy_code`, `range_start`, `range_end`, `unit_price`, `fixed_amount`, `sort_order`, `created_by`, `dt_created`) VALUES

-- TIERED_CPU_001 的阶梯参数
('DEFAULT', NULL, NULL, 'TIERED_CPU_001', 0.000000, 100.000000, 0.400000, 0.000000, 1, 'system', NOW()),
('DEFAULT', NULL, NULL, 'TIERED_CPU_001', 100.000000, 500.000000, 0.360000, 0.000000, 2, 'system', NOW()),
('DEFAULT', NULL, NULL, 'TIERED_CPU_001', 500.000000, NULL, 0.320000, 0.000000, 3, 'system', NOW()),

-- VOLUME_DISCOUNT_001 的阶梯参数
('DEFAULT', NULL, NULL, 'VOLUME_DISCOUNT_001', 1.000000, 10.000000, 1.000000, 0.000000, 1, 'system', NOW()),
('DEFAULT', NULL, NULL, 'VOLUME_DISCOUNT_001', 10.000000, 100.000000, 0.900000, 0.000000, 2, 'system', NOW()),
('DEFAULT', NULL, NULL, 'VOLUME_DISCOUNT_001', 100.000000, NULL, 0.800000, 0.000000, 3, 'system', NOW()),

-- TIERED_CPU 定价模板的阶梯参数(直接关联定价)
('DEFAULT', 'TIERED_CPU', '20260131135900', NULL, 0.000000, 100.000000, 0.400000, 0.000000, 1, 'system', NOW()),
('DEFAULT', 'TIERED_CPU', '20260131135900', NULL, 100.000000, 500.000000, 0.360000, 0.000000, 2, 'system', NOW()),
('DEFAULT', 'TIERED_CPU', '20260131135900', NULL, 500.000000, NULL, 0.320000, 0.000000, 3, 'system', NOW());


SET FOREIGN_KEY_CHECKS = 1;

-- ========================================================================
-- END OF INIT DATA SCRIPT
-- ========================================================================
