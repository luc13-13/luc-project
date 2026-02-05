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
-- Table: product_info (产品四层结构)
-- ----------------------------
TRUNCATE TABLE `product_info`;
INSERT INTO `product_info` (`tenant_id`, `product_code`, `sub_product_code`, `billing_item_code`, `sub_billing_item_code`, `product_name`, `sub_product_name`, `billing_item_name`, `sub_billing_item_name`, `spec_value`, `spec_unit`, `metering_unit`, `status`, `sort_order`, `created_by`, `dt_created`) VALUES

-- CVM 云服务器产品线
('DEFAULT', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL', '云服务器', '通用型S5', 'CPU', 'Intel处理器', 1.000000, '核', '核·小时', 'ACTIVE', 100, 'system', NOW()),
('DEFAULT', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4', '云服务器', '通用型S5', '内存', 'DDR4内存', 1.000000, 'GB', 'GB·小时', 'ACTIVE', 101, 'system', NOW()),
('DEFAULT', 'CVM', 'C6_COMPUTE', 'CPU', 'HYGON', '云服务器', '计算型C6', 'CPU', '海光处理器', 1.000000, '核', '核·小时', 'ACTIVE', 110, 'system', NOW()),
('DEFAULT', 'CVM', 'C6_COMPUTE', 'MEMORY', 'DDR4', '云服务器', '计算型C6', '内存', 'DDR4内存', 1.000000, 'GB', 'GB·小时', 'ACTIVE', 111, 'system', NOW()),

-- CBS 云硬盘产品线
('DEFAULT', 'CBS', 'CLOUD_SSD', 'STORAGE', 'SSD', '云硬盘', 'SSD云盘', '存储容量', 'NVMe SSD', 1.000000, 'GB', 'GB·小时', 'ACTIVE', 200, 'system', NOW()),
('DEFAULT', 'CBS', 'CLOUD_PREMIUM', 'STORAGE', 'HDD', '云硬盘', '高效云盘', '存储容量', 'SATA HDD', 1.000000, 'GB', 'GB·小时', 'ACTIVE', 210, 'system', NOW()),
('DEFAULT', 'CBS', 'CLOUD_HDD', 'STORAGE', 'HDD', '云硬盘', 'HDD云盘', '存储容量', 'SATA HDD', 1.000000, 'GB', 'GB·小时', 'ACTIVE', 220, 'system', NOW()),

-- CLB 负载均衡产品线
('DEFAULT', 'CLB', 'PUBLIC_CLB', 'INSTANCE', 'CLB_STANDARD', '负载均衡', '公网CLB', '实例费', '标准实例', 1.000000, '个', '个·小时', 'ACTIVE', 300, 'system', NOW()),
('DEFAULT', 'CLB', 'PUBLIC_CLB', 'BANDWIDTH', 'BW_SHARED', '负载均衡', '公网CLB', '带宽费', '共享带宽', 1.000000, 'Mbps', 'Mbps·小时', 'ACTIVE', 301, 'system', NOW()),
('DEFAULT', 'CLB', 'PUBLIC_CLB', 'LCU', 'LCU_STANDARD', '负载均衡', '公网CLB', 'LCU费', '标准LCU', 1.000000, 'LCU', 'LCU·小时', 'ACTIVE', 302, 'system', NOW()),
('DEFAULT', 'CLB', 'PRIVATE_CLB', 'INSTANCE', 'CLB_INTERNAL', '负载均衡', '内网CLB', '实例费', '内网实例', 1.000000, '个', '个·小时', 'ACTIVE', 310, 'system', NOW()),

-- EIP 弹性公网IP产品线
('DEFAULT', 'EIP', 'BGP', 'BANDWIDTH', 'BGP_BW', '弹性公网IP', 'BGP线路', '带宽费', 'BGP带宽', 1.000000, 'Mbps', 'Mbps·小时', 'ACTIVE', 400, 'system', NOW()),

-- NAT 网关产品线
('DEFAULT', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_SMALL', 'NAT网关', 'NAT网关', '实例费', '小型规格', 1.000000, '个', '个·小时', 'ACTIVE', 500, 'system', NOW()),
('DEFAULT', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_MEDIUM', 'NAT网关', 'NAT网关', '实例费', '中型规格', 1.000000, '个', '个·小时', 'ACTIVE', 501, 'system', NOW()),
('DEFAULT', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_LARGE', 'NAT网关', 'NAT网关', '实例费', '大型规格', 1.000000, '个', '个·小时', 'ACTIVE', 502, 'system', NOW()),

-- APP_SUBSCRIPTION 应用订阅产品线
('DEFAULT', 'APP_SUBSCRIPTION', 'BASIC', 'SUBSCRIPTION', 'MONTHLY', '应用订阅', '基础版', '订阅费', '月度订阅', 1.000000, '月', '月', 'ACTIVE', 600, 'system', NOW()),
('DEFAULT', 'APP_SUBSCRIPTION', 'BASIC', 'SUBSCRIPTION', 'YEARLY', '应用订阅', '基础版', '订阅费', '年度订阅', 12.000000, '月', '年', 'ACTIVE', 601, 'system', NOW()),
('DEFAULT', 'APP_SUBSCRIPTION', 'PROFESSIONAL', 'SUBSCRIPTION', 'MONTHLY', '应用订阅', '专业版', '订阅费', '月度订阅', 1.000000, '月', '月', 'ACTIVE', 610, 'system', NOW()),
('DEFAULT', 'APP_SUBSCRIPTION', 'PROFESSIONAL', 'SUBSCRIPTION', 'YEARLY', '应用订阅', '专业版', '订阅费', '年度订阅', 12.000000, '月', '年', 'ACTIVE', 611, 'system', NOW()),
('DEFAULT', 'APP_SUBSCRIPTION', 'ENTERPRISE', 'SUBSCRIPTION', 'MONTHLY', '应用订阅', '企业版', '订阅费', '月度订阅', 1.000000, '月', '月', 'ACTIVE', 620, 'system', NOW()),
('DEFAULT', 'APP_SUBSCRIPTION', 'ENTERPRISE', 'SUBSCRIPTION', 'YEARLY', '应用订阅', '企业版', '订阅费', '年度订阅', 12.000000, '月', '年', 'ACTIVE', 621, 'system', NOW());


-- ----------------------------
-- Table: product_sku (售卖单元)
-- ----------------------------
TRUNCATE TABLE `product_sku`;
INSERT INTO `product_sku` (`tenant_id`, `sku_code`, `sku_name`, `revision`, `sku_type`, `base_unit_price`, `currency`, `saleable`, `visible`, `quota_limit`, `is_current`, `effective_time`, `expiry_time`, `status`, `publish_time`, `created_by`, `dt_created`) VALUES

-- CVM 云服务器 SKU
('DEFAULT', 'CVM-S5-1C1G', '通用型S5 1核1G', 'SKU-20260205084700', 'INSTANCE', 0.6000, 'CNY', 1, 1, 100, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CVM-S5-2C4G', '通用型S5 2核4G', 'SKU-20260205084700', 'INSTANCE', 1.6000, 'CNY', 1, 1, 100, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', '通用型S5 4核8G', 'SKU-20260205084700', 'INSTANCE', 3.2000, 'CNY', 1, 1, 50, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CVM-S5-8C16G', '通用型S5 8核16G', 'SKU-20260205084700', 'INSTANCE', 6.4000, 'CNY', 1, 1, 20, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CVM-C6-4C8G', '计算型C6 4核8G', 'SKU-20260205084700', 'INSTANCE', 2.7200, 'CNY', 1, 1, 50, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CVM-C6-8C16G', '计算型C6 8核16G', 'SKU-20260205084700', 'INSTANCE', 5.4400, 'CNY', 1, 1, 20, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),

-- CBS 云硬盘 SKU
('DEFAULT', 'CBS-SSD-100G', 'SSD云盘 100GB', 'SKU-20260205084700', 'ADDON', 0.1000, 'CNY', 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CBS-SSD-500G', 'SSD云盘 500GB', 'SKU-20260205084700', 'ADDON', 0.5000, 'CNY', 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CBS-PREMIUM-100G', '高效云盘 100GB', 'SKU-20260205084700', 'ADDON', 0.0400, 'CNY', 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CBS-PREMIUM-500G', '高效云盘 500GB', 'SKU-20260205084700', 'ADDON', 0.2000, 'CNY', 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CBS-HDD-500G', 'HDD云盘 500GB', 'SKU-20260205084700', 'ADDON', 0.0300, 'CNY', 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),

-- CLB 负载均衡 SKU
('DEFAULT', 'CLB-PUBLIC-STANDARD', '公网负载均衡标准版', 'SKU-20260205084700', 'INSTANCE', 0.0280, 'CNY', 1, 1, 20, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'CLB-PRIVATE-STANDARD', '内网负载均衡标准版', 'SKU-20260205084700', 'INSTANCE', 0.0100, 'CNY', 1, 1, 50, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),

-- EIP 弹性公网IP SKU
('DEFAULT', 'EIP-BGP-1M', '弹性公网IP BGP 1Mbps', 'SKU-20260205084700', 'ADDON', 0.0300, 'CNY', 1, 1, 20, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'EIP-BGP-5M', '弹性公网IP BGP 5Mbps', 'SKU-20260205084700', 'ADDON', 0.1500, 'CNY', 1, 1, 20, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),

-- NAT 网关 SKU
('DEFAULT', 'NAT-SMALL', 'NAT网关 小型', 'SKU-20260205084700', 'INSTANCE', 0.0500, 'CNY', 1, 1, 10, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'NAT-MEDIUM', 'NAT网关 中型', 'SKU-20260205084700', 'INSTANCE', 0.1000, 'CNY', 1, 1, 5, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'NAT-LARGE', 'NAT网关 大型', 'SKU-20260205084700', 'INSTANCE', 0.2000, 'CNY', 1, 1, 3, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),

-- APP_SUBSCRIPTION 应用订阅 SKU
('DEFAULT', 'APP-BASIC-MONTHLY', '应用基础版 月付', 'SKU-20260205084700', 'SUBSCRIPTION', 99.0000, 'CNY', 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'APP-BASIC-YEARLY', '应用基础版 年付', 'SKU-20260205084700', 'SUBSCRIPTION', 948.0000, 'CNY', 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'APP-PRO-MONTHLY', '应用专业版 月付', 'SKU-20260205084700', 'SUBSCRIPTION', 299.0000, 'CNY', 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'APP-PRO-YEARLY', '应用专业版 年付', 'SKU-20260205084700', 'SUBSCRIPTION', 2868.0000, 'CNY', 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'APP-ENT-MONTHLY', '应用企业版 月付', 'SKU-20260205084700', 'SUBSCRIPTION', 999.0000, 'CNY', 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW()),
('DEFAULT', 'APP-ENT-YEARLY', '应用企业版 年付', 'SKU-20260205084700', 'SUBSCRIPTION', 9588.0000, 'CNY', 1, 1, NULL, 1, NOW(), NULL, 'ACTIVE', NOW(), 'system', NOW());


-- ----------------------------
-- Table: sku_item_combination (SKU BOM 组合)
-- ----------------------------
TRUNCATE TABLE `sku_item_combination`;
INSERT INTO `sku_item_combination` (`tenant_id`, `sku_code`, `sku_revision`, `product_code`, `sub_product_code`, `billing_item_code`, `sub_billing_item_code`, `quantity`, `pricing_included`, `created_by`, `dt_created`) VALUES

-- CVM-S5-1C1G: 1核CPU + 1GB内存
('DEFAULT', 'CVM-S5-1C1G', 'SKU-20260205084700', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL', 1.00, 1, 'system', NOW()),
('DEFAULT', 'CVM-S5-1C1G', 'SKU-20260205084700', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4', 1.00, 1, 'system', NOW()),

-- CVM-S5-2C4G: 2核CPU + 4GB内存
('DEFAULT', 'CVM-S5-2C4G', 'SKU-20260205084700', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL', 2.00, 1, 'system', NOW()),
('DEFAULT', 'CVM-S5-2C4G', 'SKU-20260205084700', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4', 4.00, 1, 'system', NOW()),

-- CVM-S5-4C8G: 4核CPU + 8GB内存
('DEFAULT', 'CVM-S5-4C8G', 'SKU-20260205084700', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL', 4.00, 1, 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', 'SKU-20260205084700', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4', 8.00, 1, 'system', NOW()),

-- CVM-S5-8C16G: 8核CPU + 16GB内存
('DEFAULT', 'CVM-S5-8C16G', 'SKU-20260205084700', 'CVM', 'S5_GENERAL', 'CPU', 'INTEL', 8.00, 1, 'system', NOW()),
('DEFAULT', 'CVM-S5-8C16G', 'SKU-20260205084700', 'CVM', 'S5_GENERAL', 'MEMORY', 'DDR4', 16.00, 1, 'system', NOW()),

-- CVM-C6-4C8G: 4核海光CPU + 8GB内存
('DEFAULT', 'CVM-C6-4C8G', 'SKU-20260205084700', 'CVM', 'C6_COMPUTE', 'CPU', 'HYGON', 4.00, 1, 'system', NOW()),
('DEFAULT', 'CVM-C6-4C8G', 'SKU-20260205084700', 'CVM', 'C6_COMPUTE', 'MEMORY', 'DDR4', 8.00, 1, 'system', NOW()),

-- CVM-C6-8C16G: 8核海光CPU + 16GB内存
('DEFAULT', 'CVM-C6-8C16G', 'SKU-20260205084700', 'CVM', 'C6_COMPUTE', 'CPU', 'HYGON', 8.00, 1, 'system', NOW()),
('DEFAULT', 'CVM-C6-8C16G', 'SKU-20260205084700', 'CVM', 'C6_COMPUTE', 'MEMORY', 'DDR4', 16.00, 1, 'system', NOW()),

-- CBS-SSD-100G
('DEFAULT', 'CBS-SSD-100G', 'SKU-20260205084700', 'CBS', 'CLOUD_SSD', 'STORAGE', 'SSD', 100.00, 1, 'system', NOW()),

-- CBS-SSD-500G
('DEFAULT', 'CBS-SSD-500G', 'SKU-20260205084700', 'CBS', 'CLOUD_SSD', 'STORAGE', 'SSD', 500.00, 1, 'system', NOW()),

-- CBS-PREMIUM-100G
('DEFAULT', 'CBS-PREMIUM-100G', 'SKU-20260205084700', 'CBS', 'CLOUD_PREMIUM', 'STORAGE', 'HDD', 100.00, 1, 'system', NOW()),

-- CBS-PREMIUM-500G
('DEFAULT', 'CBS-PREMIUM-500G', 'SKU-20260205084700', 'CBS', 'CLOUD_PREMIUM', 'STORAGE', 'HDD', 500.00, 1, 'system', NOW()),

-- CBS-HDD-500G
('DEFAULT', 'CBS-HDD-500G', 'SKU-20260205084700', 'CBS', 'CLOUD_HDD', 'STORAGE', 'HDD', 500.00, 1, 'system', NOW()),

-- CLB-PUBLIC-STANDARD: 公网LB实例费 + 带宽费 + LCU费
('DEFAULT', 'CLB-PUBLIC-STANDARD', 'SKU-20260205084700', 'CLB', 'PUBLIC_CLB', 'INSTANCE', 'CLB_STANDARD', 1.00, 1, 'system', NOW()),
('DEFAULT', 'CLB-PUBLIC-STANDARD', 'SKU-20260205084700', 'CLB', 'PUBLIC_CLB', 'BANDWIDTH', 'BW_SHARED', 1.00, 0, 'system', NOW()),
('DEFAULT', 'CLB-PUBLIC-STANDARD', 'SKU-20260205084700', 'CLB', 'PUBLIC_CLB', 'LCU', 'LCU_STANDARD', 1.00, 0, 'system', NOW()),

-- CLB-PRIVATE-STANDARD: 内网LB实例费
('DEFAULT', 'CLB-PRIVATE-STANDARD', 'SKU-20260205084700', 'CLB', 'PRIVATE_CLB', 'INSTANCE', 'CLB_INTERNAL', 1.00, 1, 'system', NOW()),

-- EIP-BGP-1M: 带宽费
('DEFAULT', 'EIP-BGP-1M', 'SKU-20260205084700', 'EIP', 'BGP', 'BANDWIDTH', 'BGP_BW', 1.00, 1, 'system', NOW()),

-- EIP-BGP-5M: 带宽费
('DEFAULT', 'EIP-BGP-5M', 'SKU-20260205084700', 'EIP', 'BGP', 'BANDWIDTH', 'BGP_BW', 5.00, 1, 'system', NOW()),

-- NAT-SMALL
('DEFAULT', 'NAT-SMALL', 'SKU-20260205084700', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_SMALL', 1.00, 1, 'system', NOW()),

-- NAT-MEDIUM
('DEFAULT', 'NAT-MEDIUM', 'SKU-20260205084700', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_MEDIUM', 1.00, 1, 'system', NOW()),

-- NAT-LARGE
('DEFAULT', 'NAT-LARGE', 'SKU-20260205084700', 'NAT', 'NAT_GATEWAY', 'INSTANCE', 'NAT_LARGE', 1.00, 1, 'system', NOW()),

-- APP-BASIC-MONTHLY
('DEFAULT', 'APP-BASIC-MONTHLY', 'SKU-20260205084700', 'APP_SUBSCRIPTION', 'BASIC', 'SUBSCRIPTION', 'MONTHLY', 1.00, 1, 'system', NOW()),

-- APP-BASIC-YEARLY
('DEFAULT', 'APP-BASIC-YEARLY', 'SKU-20260205084700', 'APP_SUBSCRIPTION', 'BASIC', 'SUBSCRIPTION', 'YEARLY', 12.00, 1, 'system', NOW()),

-- APP-PRO-MONTHLY
('DEFAULT', 'APP-PRO-MONTHLY', 'SKU-20260205084700', 'APP_SUBSCRIPTION', 'PROFESSIONAL', 'SUBSCRIPTION', 'MONTHLY', 1.00, 1, 'system', NOW()),

-- APP-PRO-YEARLY
('DEFAULT', 'APP-PRO-YEARLY', 'SKU-20260205084700', 'APP_SUBSCRIPTION', 'PROFESSIONAL', 'SUBSCRIPTION', 'YEARLY', 12.00, 1, 'system', NOW()),

-- APP-ENT-MONTHLY
('DEFAULT', 'APP-ENT-MONTHLY', 'SKU-20260205084700', 'APP_SUBSCRIPTION', 'ENTERPRISE', 'SUBSCRIPTION', 'MONTHLY', 1.00, 1, 'system', NOW()),

-- APP-ENT-YEARLY
('DEFAULT', 'APP-ENT-YEARLY', 'SKU-20260205084700', 'APP_SUBSCRIPTION', 'ENTERPRISE', 'SUBSCRIPTION', 'YEARLY', 12.00, 1, 'system', NOW());


-- ========================================================================
-- 2. 定价层数据
-- ========================================================================

-- ----------------------------
-- Table: sku_pricing (定价模板)
-- ----------------------------
TRUNCATE TABLE `sku_pricing`;
INSERT INTO `sku_pricing` (`tenant_id`, `pricing_code`, `revision`, `metering_mode`, `payment_mode`, `billing_cycle`, `cycle_count`, `billing_unit`, `refund_policy`, `discount_rate`, `currency`, `metering_unit`, `metering_precision`, `effective_time`, `expiry_time`, `is_current`, `priority`, `status`, `remark`, `created_by`, `dt_created`) VALUES

-- 按量计费 - 按小时
('DEFAULT', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 'BY_USAGE', 'POSTPAID', 'HOURLY', 1, 'PERIOD', 'PRO_RATA', 1.0000, 'CNY', '小时', 2, NOW(), NULL, 1, 10, 'ACTIVE', '按量计费-按小时', 'system', NOW()),

-- 包月
('DEFAULT', 'PREPAID_MONTHLY', 'PRC-20260205084700', 'BY_QUOTA', 'PREPAID', 'MONTHLY', 1, 'PERIOD', 'PRO_RATA', 0.8500, 'CNY', '月', 0, NOW(), NULL, 1, 20, 'ACTIVE', '包月(85折)', 'system', NOW()),

-- 包季
('DEFAULT', 'PREPAID_QUARTERLY', 'PRC-20260205084700', 'BY_QUOTA', 'PREPAID', 'QUARTERLY', 1, 'PERIOD', 'PRO_RATA', 0.8000, 'CNY', '季度', 0, NOW(), NULL, 1, 30, 'ACTIVE', '包季(8折)', 'system', NOW()),

-- 包年
('DEFAULT', 'PREPAID_YEARLY', 'PRC-20260205084700', 'BY_QUOTA', 'PREPAID', 'YEARLY', 1, 'PERIOD', 'PRO_RATA', 0.7000, 'CNY', '年', 0, NOW(), NULL, 1, 40, 'ACTIVE', '包年(7折)', 'system', NOW()),

-- 订阅-月付
('DEFAULT', 'SUBSCRIPTION_MONTHLY', 'PRC-20260205084700', 'BY_QUOTA', 'SUBSCRIPTION', 'MONTHLY', 1, 'PERIOD', 'NON_REFUNDABLE', 1.0000, 'CNY', '月', 0, NOW(), NULL, 1, 50, 'ACTIVE', '月度订阅', 'system', NOW()),

-- 订阅-年付
('DEFAULT', 'SUBSCRIPTION_YEARLY', 'PRC-20260205084700', 'BY_QUOTA', 'SUBSCRIPTION', 'YEARLY', 1, 'PERIOD', 'NON_REFUNDABLE', 0.8000, 'CNY', '年', 0, NOW(), NULL, 1, 60, 'ACTIVE', '年度订阅(8折)', 'system', NOW()),

-- 阶梯计费 - CPU (按量)
('DEFAULT', 'TIERED_CPU', 'PRC-20260205084700', 'BY_USAGE', 'POSTPAID', 'HOURLY', 1, 'QUANTITY', 'PRO_RATA', 1.0000, 'CNY', '核·小时', 4, NOW(), NULL, 1, 100, 'ACTIVE', 'CPU阶梯计费', 'system', NOW()),

-- 流量计费
('DEFAULT', 'TRAFFIC_PAY_AS_GO', 'PRC-20260205084700', 'BY_USAGE', 'POSTPAID', 'ONCE', 1, 'QUANTITY', 'NON_REFUNDABLE', 1.0000, 'CNY', 'GB', 6, NOW(), NULL, 1, 70, 'ACTIVE', '流量按量计费', 'system', NOW());


-- ----------------------------
-- Table: sku_pricing_link (SKU与定价模板关联)
-- ----------------------------
TRUNCATE TABLE `sku_pricing_link`;
INSERT INTO `sku_pricing_link` (`tenant_id`, `sku_code`, `sku_revision`, `pricing_code`, `pricing_revision`, `override_factor`, `is_default`, `status`, `created_by`, `dt_created`) VALUES

-- CVM SKU 关联多种定价模式 (按量/包月/包年)
('DEFAULT', 'CVM-S5-1C1G', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-1C1G', 'SKU-20260205084700', 'PREPAID_MONTHLY', 'PRC-20260205084700', 720.0000, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-1C1G', 'SKU-20260205084700', 'PREPAID_YEARLY', 'PRC-20260205084700', 8760.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-S5-2C4G', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-2C4G', 'SKU-20260205084700', 'PREPAID_MONTHLY', 'PRC-20260205084700', 720.0000, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-2C4G', 'SKU-20260205084700', 'PREPAID_YEARLY', 'PRC-20260205084700', 8760.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-S5-4C8G', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', 'SKU-20260205084700', 'PREPAID_MONTHLY', 'PRC-20260205084700', 720.0000, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', 'SKU-20260205084700', 'PREPAID_YEARLY', 'PRC-20260205084700', 8760.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-S5-8C16G', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-8C16G', 'SKU-20260205084700', 'PREPAID_MONTHLY', 'PRC-20260205084700', 720.0000, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-8C16G', 'SKU-20260205084700', 'PREPAID_YEARLY', 'PRC-20260205084700', 8760.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-C6-4C8G', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-C6-4C8G', 'SKU-20260205084700', 'PREPAID_MONTHLY', 'PRC-20260205084700', 720.0000, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-C6-4C8G', 'SKU-20260205084700', 'PREPAID_YEARLY', 'PRC-20260205084700', 8760.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-C6-8C16G', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-C6-8C16G', 'SKU-20260205084700', 'PREPAID_MONTHLY', 'PRC-20260205084700', 720.0000, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-C6-8C16G', 'SKU-20260205084700', 'PREPAID_YEARLY', 'PRC-20260205084700', 8760.0000, 0, 'ACTIVE', 'system', NOW()),

-- CBS SKU 关联定价 (按量/包月)
('DEFAULT', 'CBS-SSD-100G', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-SSD-100G', 'SKU-20260205084700', 'PREPAID_MONTHLY', 'PRC-20260205084700', 720.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CBS-SSD-500G', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-SSD-500G', 'SKU-20260205084700', 'PREPAID_MONTHLY', 'PRC-20260205084700', 720.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CBS-PREMIUM-100G', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-PREMIUM-100G', 'SKU-20260205084700', 'PREPAID_MONTHLY', 'PRC-20260205084700', 720.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CBS-PREMIUM-500G', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-PREMIUM-500G', 'SKU-20260205084700', 'PREPAID_MONTHLY', 'PRC-20260205084700', 720.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CBS-HDD-500G', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-HDD-500G', 'SKU-20260205084700', 'PREPAID_MONTHLY', 'PRC-20260205084700', 720.0000, 0, 'ACTIVE', 'system', NOW()),

-- CLB SKU 关联定价 (按量/包月)
('DEFAULT', 'CLB-PUBLIC-STANDARD', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CLB-PUBLIC-STANDARD', 'SKU-20260205084700', 'PREPAID_MONTHLY', 'PRC-20260205084700', 720.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CLB-PRIVATE-STANDARD', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CLB-PRIVATE-STANDARD', 'SKU-20260205084700', 'PREPAID_MONTHLY', 'PRC-20260205084700', 720.0000, 0, 'ACTIVE', 'system', NOW()),

-- EIP SKU 关联定价 (按量)
('DEFAULT', 'EIP-BGP-1M', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'EIP-BGP-5M', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),

-- NAT SKU 关联定价 (按量/包月)
('DEFAULT', 'NAT-SMALL', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'NAT-SMALL', 'SKU-20260205084700', 'PREPAID_MONTHLY', 'PRC-20260205084700', 720.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'NAT-MEDIUM', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'NAT-MEDIUM', 'SKU-20260205084700', 'PREPAID_MONTHLY', 'PRC-20260205084700', 720.0000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'NAT-LARGE', 'SKU-20260205084700', 'PAY_AS_GO_HOURLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'NAT-LARGE', 'SKU-20260205084700', 'PREPAID_MONTHLY', 'PRC-20260205084700', 720.0000, 0, 'ACTIVE', 'system', NOW()),

-- APP 订阅 SKU 关联定价 (订阅)
('DEFAULT', 'APP-BASIC-MONTHLY', 'SKU-20260205084700', 'SUBSCRIPTION_MONTHLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'APP-BASIC-YEARLY', 'SKU-20260205084700', 'SUBSCRIPTION_YEARLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'APP-PRO-MONTHLY', 'SKU-20260205084700', 'SUBSCRIPTION_MONTHLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'APP-PRO-YEARLY', 'SKU-20260205084700', 'SUBSCRIPTION_YEARLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'APP-ENT-MONTHLY', 'SKU-20260205084700', 'SUBSCRIPTION_MONTHLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'APP-ENT-YEARLY', 'SKU-20260205084700', 'SUBSCRIPTION_YEARLY', 'PRC-20260205084700', 1.0000, 1, 'ACTIVE', 'system', NOW());

-- ----------------------------
-- Table: pricing_strategy (定价策略)
-- ----------------------------
TRUNCATE TABLE `pricing_strategy`;
INSERT INTO `pricing_strategy` (`tenant_id`, `strategy_code`, `strategy_name`, `strategy_type`, `calc_method`, `apply_scope`, `apply_scope_value`, `priority`, `effective_time`, `expiry_time`, `status`, `remark`, `created_by`, `dt_created`) VALUES

-- 线性定价策略
('DEFAULT', 'LINEAR', '线性定价', 'LINEAR', 'MULTIPLY', 'ALL', NULL, 0, NOW(), NULL, 'ACTIVE', '默认线性定价策略', 'system', NOW()),

-- CPU阶梯定价策略
('DEFAULT', 'TIERED_CPU_001', 'CPU阶梯定价', 'TIERED', 'MULTIPLY', 'PRODUCT_LINE', 'CVM', 10, NOW(), NULL, 'ACTIVE', 'CPU分层定价策略', 'system', NOW()),

-- 存储阶梯定价策略
('DEFAULT', 'TIERED_STORAGE_001', '存储阶梯定价', 'TIERED', 'MULTIPLY', 'PRODUCT_LINE', 'CBS', 10, NOW(), NULL, 'ACTIVE', '存储分层定价策略', 'system', NOW()),

-- 批量折扣策略
('DEFAULT', 'VOLUME_DISCOUNT_001', '批量折扣', 'VOLUME_DISCOUNT', 'MULTIPLY', 'ALL', NULL, 20, NOW(), NULL, 'ACTIVE', '批量折扣策略', 'system', NOW()),

-- 区域定价策略-上海加价
('DEFAULT', 'REGION_SHANGHAI', '上海区域定价', 'REGION', 'MULTIPLY', 'ALL', NULL, 5, NOW(), NULL, 'ACTIVE', '上海区域加价策略', 'system', NOW()),

-- 区域定价策略-广州优惠
('DEFAULT', 'REGION_GUANGZHOU', '广州区域定价', 'REGION', 'MULTIPLY', 'ALL', NULL, 5, NOW(), NULL, 'ACTIVE', '广州区域优惠策略', 'system', NOW()),

-- 促销策略-首月优惠
('DEFAULT', 'PROMO_FIRST_MONTH', '首月优惠', 'PROMOTION', 'MULTIPLY', 'ALL', NULL, 100, NOW(), NULL, 'ACTIVE', '新用户首月优惠', 'system', NOW()),

-- 满减策略
('DEFAULT', 'MANJIAN_1000_100', '满1000减100', 'THRESHOLD', 'SUBTRACT', 'ALL', NULL, 50, NOW(), NULL, 'ACTIVE', '满减活动', 'system', NOW());


-- ----------------------------
-- Table: pricing_strategy_param (定价策略参数)
-- ----------------------------
TRUNCATE TABLE `pricing_strategy_param`;
INSERT INTO `pricing_strategy_param` (`tenant_id`, `strategy_code`, `param_type`, `range_start`, `range_end`, `value`, `sort_order`, `created_by`, `dt_created`) VALUES

-- TIERED_CPU_001 阶梯参数
('DEFAULT', 'TIERED_CPU_001', 'TIER', 0.000000, 100.000000, 0.400000, 1, 'system', NOW()),
('DEFAULT', 'TIERED_CPU_001', 'TIER', 100.000000, 500.000000, 0.360000, 2, 'system', NOW()),
('DEFAULT', 'TIERED_CPU_001', 'TIER', 500.000000, NULL, 0.320000, 3, 'system', NOW()),

-- TIERED_STORAGE_001 阶梯参数
('DEFAULT', 'TIERED_STORAGE_001', 'TIER', 0.000000, 100.000000, 0.001000, 1, 'system', NOW()),
('DEFAULT', 'TIERED_STORAGE_001', 'TIER', 100.000000, 1000.000000, 0.000900, 2, 'system', NOW()),
('DEFAULT', 'TIERED_STORAGE_001', 'TIER', 1000.000000, NULL, 0.000800, 3, 'system', NOW()),

-- VOLUME_DISCOUNT_001 批量折扣参数
('DEFAULT', 'VOLUME_DISCOUNT_001', 'TIER', 1.000000, 10.000000, 1.000000, 1, 'system', NOW()),
('DEFAULT', 'VOLUME_DISCOUNT_001', 'TIER', 10.000000, 100.000000, 0.900000, 2, 'system', NOW()),
('DEFAULT', 'VOLUME_DISCOUNT_001', 'TIER', 100.000000, NULL, 0.800000, 3, 'system', NOW()),

-- REGION_SHANGHAI 区域系数
('DEFAULT', 'REGION_SHANGHAI', 'RATE', NULL, NULL, 1.200000, 1, 'system', NOW()),

-- REGION_GUANGZHOU 区域系数
('DEFAULT', 'REGION_GUANGZHOU', 'RATE', NULL, NULL, 0.950000, 1, 'system', NOW()),

-- PROMO_FIRST_MONTH 促销参数
('DEFAULT', 'PROMO_FIRST_MONTH', 'RATE', NULL, NULL, 0.500000, 1, 'system', NOW()),
('DEFAULT', 'PROMO_FIRST_MONTH', 'CAP', NULL, NULL, 100.000000, 2, 'system', NOW()),

-- MANJIAN_1000_100 满减参数
('DEFAULT', 'MANJIAN_1000_100', 'THRESHOLD', 1000.000000, NULL, 100.000000, 1, 'system', NOW()),
('DEFAULT', 'MANJIAN_1000_100', 'THRESHOLD', 2000.000000, NULL, 200.000000, 2, 'system', NOW()),
('DEFAULT', 'MANJIAN_1000_100', 'CAP', NULL, NULL, 500.000000, 99, 'system', NOW());


-- ----------------------------
-- Table: sku_pricing_strategy_link (SKU与策略关联)
-- ----------------------------
TRUNCATE TABLE `sku_pricing_strategy_link`;
INSERT INTO `sku_pricing_strategy_link` (`tenant_id`, `sku_code`, `sku_revision`, `strategy_code`, `priority`, `effective_time`, `expiry_time`, `status`, `created_by`, `dt_created`) VALUES

-- CVM SKU 关联 CPU 阶梯策略和批量折扣
('DEFAULT', 'CVM-S5-1C1G', 'SKU-20260205084700', 'TIERED_CPU_001', 100, NULL, NULL, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-1C1G', 'SKU-20260205084700', 'VOLUME_DISCOUNT_001', 50, NULL, NULL, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-S5-2C4G', 'SKU-20260205084700', 'TIERED_CPU_001', 100, NULL, NULL, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-2C4G', 'SKU-20260205084700', 'VOLUME_DISCOUNT_001', 50, NULL, NULL, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-S5-4C8G', 'SKU-20260205084700', 'TIERED_CPU_001', 100, NULL, NULL, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', 'SKU-20260205084700', 'VOLUME_DISCOUNT_001', 50, NULL, NULL, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-S5-8C16G', 'SKU-20260205084700', 'TIERED_CPU_001', 100, NULL, NULL, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-8C16G', 'SKU-20260205084700', 'VOLUME_DISCOUNT_001', 50, NULL, NULL, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-C6-4C8G', 'SKU-20260205084700', 'TIERED_CPU_001', 100, NULL, NULL, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-C6-4C8G', 'SKU-20260205084700', 'VOLUME_DISCOUNT_001', 50, NULL, NULL, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-C6-8C16G', 'SKU-20260205084700', 'TIERED_CPU_001', 100, NULL, NULL, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-C6-8C16G', 'SKU-20260205084700', 'VOLUME_DISCOUNT_001', 50, NULL, NULL, 'ACTIVE', 'system', NOW()),

-- CBS SKU 关联存储阶梯策略和批量折扣
('DEFAULT', 'CBS-SSD-100G', 'SKU-20260205084700', 'TIERED_STORAGE_001', 100, NULL, NULL, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-SSD-100G', 'SKU-20260205084700', 'VOLUME_DISCOUNT_001', 50, NULL, NULL, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CBS-SSD-500G', 'SKU-20260205084700', 'TIERED_STORAGE_001', 100, NULL, NULL, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-SSD-500G', 'SKU-20260205084700', 'VOLUME_DISCOUNT_001', 50, NULL, NULL, 'ACTIVE', 'system', NOW()),


('DEFAULT', 'CBS-PREMIUM-100G', 'SKU-20260205084700', 'TIERED_STORAGE_001', 100, NULL, NULL, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-PREMIUM-500G', 'SKU-20260205084700', 'TIERED_STORAGE_001', 100, NULL, NULL, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-HDD-500G', 'SKU-20260205084700', 'TIERED_STORAGE_001', 100, NULL, NULL, 'ACTIVE', 'system', NOW()),

-- CLB SKU 关联批量折扣
('DEFAULT', 'CLB-PUBLIC-STANDARD', 'SKU-20260205084700', 'VOLUME_DISCOUNT_001', 50, NULL, NULL, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CLB-PRIVATE-STANDARD', 'SKU-20260205084700', 'VOLUME_DISCOUNT_001', 50, NULL, NULL, 'ACTIVE', 'system', NOW()),

-- NAT SKU 关联批量折扣
('DEFAULT', 'NAT-SMALL', 'SKU-20260205084700', 'VOLUME_DISCOUNT_001', 50, NULL, NULL, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'NAT-MEDIUM', 'SKU-20260205084700', 'VOLUME_DISCOUNT_001', 50, NULL, NULL, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'NAT-LARGE', 'SKU-20260205084700', 'VOLUME_DISCOUNT_001', 50, NULL, NULL, 'ACTIVE', 'system', NOW());


-- ========================================================================
-- 3. 区域配置层数据
-- ========================================================================

-- ----------------------------
-- Table: region_config (区域配置)
-- ----------------------------
TRUNCATE TABLE `region_config`;
INSERT INTO `region_config` (`region_code`, `region_name`, `region_type`, `country_code`, `geographic_location`, `operator`, `network_latency_tier`, `price_factor`, `status`, `sort_order`, `created_by`, `dt_created`) VALUES

-- 华北区域
('cn-beijing', '华北-北京', 'PUBLIC', 'CN', '北京市', '多线BGP', 'LOW', 1.0000, 'ACTIVE', 10, 'system', NOW()),
('cn-zhangjiakou', '华北-张家口', 'PUBLIC', 'CN', '河北省张家口市', '多线BGP', 'STANDARD', 0.9500, 'ACTIVE', 20, 'system', NOW()),

-- 华东区域
('cn-shanghai', '华东-上海', 'PUBLIC', 'CN', '上海市', '多线BGP', 'LOW', 1.2000, 'ACTIVE', 30, 'system', NOW()),
('cn-hangzhou', '华东-杭州', 'PUBLIC', 'CN', '浙江省杭州市', '多线BGP', 'LOW', 1.0500, 'ACTIVE', 40, 'system', NOW()),
('cn-nanjing', '华东-南京', 'PUBLIC', 'CN', '江苏省南京市', '多线BGP', 'STANDARD', 1.0000, 'ACTIVE', 50, 'system', NOW()),

-- 华南区域
('cn-guangzhou', '华南-广州', 'PUBLIC', 'CN', '广东省广州市', '多线BGP', 'LOW', 0.9500, 'ACTIVE', 60, 'system', NOW()),
('cn-shenzhen', '华南-深圳', 'PUBLIC', 'CN', '广东省深圳市', '多线BGP', 'LOW', 1.0000, 'ACTIVE', 70, 'system', NOW()),

-- 西南区域
('cn-chengdu', '西南-成都', 'PUBLIC', 'CN', '四川省成都市', '多线BGP', 'STANDARD', 0.9000, 'ACTIVE', 80, 'system', NOW()),
('cn-chongqing', '西南-重庆', 'PUBLIC', 'CN', '重庆市', '多线BGP', 'STANDARD', 0.9000, 'ACTIVE', 90, 'system', NOW()),

-- 港澳台及海外区域
('ap-hongkong', '中国香港', 'PUBLIC', 'HK', '香港特别行政区', 'BGP国际', 'STANDARD', 1.3000, 'ACTIVE', 100, 'system', NOW()),
('ap-singapore', '新加坡', 'PUBLIC', 'SG', '新加坡', 'BGP国际', 'STANDARD', 1.4000, 'ACTIVE', 110, 'system', NOW()),
('ap-tokyo', '日本-东京', 'PUBLIC', 'JP', '东京都', 'BGP国际', 'STANDARD', 1.5000, 'ACTIVE', 120, 'system', NOW());


-- ----------------------------
-- Table: availability_zone_config (可用区配置)
-- ----------------------------
TRUNCATE TABLE `availability_zone_config`;
INSERT INTO `availability_zone_config` (`region_code`, `zone_code`, `zone_name`, `zone_type`, `data_center`, `capacity_status`, `price_factor`, `status`, `sort_order`, `created_by`, `dt_created`) VALUES

-- 北京可用区
('cn-beijing', 'cn-beijing-a', '北京可用区A', 'STANDARD', 'BJ-DC1', 'SUFFICIENT', 1.0000, 'ACTIVE', 10, 'system', NOW()),
('cn-beijing', 'cn-beijing-b', '北京可用区B', 'STANDARD', 'BJ-DC2', 'SUFFICIENT', 1.0000, 'ACTIVE', 20, 'system', NOW()),
('cn-beijing', 'cn-beijing-c', '北京可用区C', 'STANDARD', 'BJ-DC3', 'SUFFICIENT', 1.0000, 'ACTIVE', 30, 'system', NOW()),

-- 上海可用区
('cn-shanghai', 'cn-shanghai-a', '上海可用区A', 'STANDARD', 'SH-DC1', 'SUFFICIENT', 1.0000, 'ACTIVE', 10, 'system', NOW()),
('cn-shanghai', 'cn-shanghai-b', '上海可用区B', 'STANDARD', 'SH-DC2', 'SUFFICIENT', 1.0000, 'ACTIVE', 20, 'system', NOW()),

-- 广州可用区
('cn-guangzhou', 'cn-guangzhou-a', '广州可用区A', 'STANDARD', 'GZ-DC1', 'SUFFICIENT', 1.0000, 'ACTIVE', 10, 'system', NOW()),
('cn-guangzhou', 'cn-guangzhou-b', '广州可用区B', 'STANDARD', 'GZ-DC2', 'SUFFICIENT', 1.0000, 'ACTIVE', 20, 'system', NOW()),
('cn-guangzhou', 'cn-guangzhou-c', '广州可用区C', 'STANDARD', 'GZ-DC3', 'LOW', 1.0000, 'ACTIVE', 30, 'system', NOW()),

-- 杭州可用区
('cn-hangzhou', 'cn-hangzhou-a', '杭州可用区A', 'STANDARD', 'HZ-DC1', 'SUFFICIENT', 1.0000, 'ACTIVE', 10, 'system', NOW()),
('cn-hangzhou', 'cn-hangzhou-b', '杭州可用区B', 'STANDARD', 'HZ-DC2', 'SUFFICIENT', 1.0000, 'ACTIVE', 20, 'system', NOW()),

-- 成都可用区
('cn-chengdu', 'cn-chengdu-a', '成都可用区A', 'STANDARD', 'CD-DC1', 'SUFFICIENT', 1.0000, 'ACTIVE', 10, 'system', NOW()),

-- 香港可用区
('ap-hongkong', 'ap-hongkong-a', '香港可用区A', 'STANDARD', 'HK-DC1', 'SUFFICIENT', 1.0000, 'ACTIVE', 10, 'system', NOW()),
('ap-hongkong', 'ap-hongkong-b', '香港可用区B', 'STANDARD', 'HK-DC2', 'SUFFICIENT', 1.0000, 'ACTIVE', 20, 'system', NOW()),

-- 新加坡可用区
('ap-singapore', 'ap-singapore-a', '新加坡可用区A', 'STANDARD', 'SG-DC1', 'SUFFICIENT', 1.0000, 'ACTIVE', 10, 'system', NOW());


-- ----------------------------
-- Table: sku_region_mapping (SKU区域可用性映射)
-- ----------------------------
TRUNCATE TABLE `sku_region_mapping`;
INSERT INTO `sku_region_mapping` (`tenant_id`, `sku_code`, `region_code`, `zone_code`, `available`, `saleable`, `inventory_status`, `zone_quota_limit`, `zone_quota_used`, `status`, `created_by`, `dt_created`) VALUES

-- CVM SKU 在各区域可用 (以 CVM-S5-4C8G 为例覆盖主要区域)
('DEFAULT', 'CVM-S5-1C1G', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', 1000, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-1C1G', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', 1000, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-1C1G', 'cn-guangzhou', NULL, 1, 1, 'SUFFICIENT', 1000, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-1C1G', 'cn-hangzhou', NULL, 1, 1, 'SUFFICIENT', 500, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-1C1G', 'ap-hongkong', NULL, 1, 1, 'SUFFICIENT', 200, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-S5-2C4G', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', 1000, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-2C4G', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', 1000, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-2C4G', 'cn-guangzhou', NULL, 1, 1, 'SUFFICIENT', 1000, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-S5-4C8G', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', 500, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', 500, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', 'cn-guangzhou', NULL, 1, 1, 'SUFFICIENT', 500, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', 'cn-hangzhou', NULL, 1, 1, 'SUFFICIENT', 300, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', 'cn-chengdu', NULL, 1, 1, 'SUFFICIENT', 200, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', 'ap-hongkong', NULL, 1, 1, 'SUFFICIENT', 100, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-4C8G', 'ap-singapore', NULL, 1, 1, 'SUFFICIENT', 100, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-S5-8C16G', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', 200, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-8C16G', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', 200, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-S5-8C16G', 'cn-guangzhou', NULL, 1, 1, 'SUFFICIENT', 200, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-C6-4C8G', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', 300, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-C6-4C8G', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', 300, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CVM-C6-8C16G', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', 150, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CVM-C6-8C16G', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', 150, 0, 'ACTIVE', 'system', NOW()),

-- CBS SKU 区域可用性
('DEFAULT', 'CBS-SSD-100G', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-SSD-100G', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-SSD-100G', 'cn-guangzhou', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-SSD-100G', 'cn-hangzhou', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-SSD-100G', 'ap-hongkong', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CBS-SSD-500G', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-SSD-500G', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-SSD-500G', 'cn-guangzhou', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CBS-HDD-500G', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-HDD-500G', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CBS-HDD-500G', 'cn-guangzhou', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),

-- CLB SKU 区域可用性
('DEFAULT', 'CLB-PUBLIC-STANDARD', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', 200, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CLB-PUBLIC-STANDARD', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', 200, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CLB-PUBLIC-STANDARD', 'cn-guangzhou', NULL, 1, 1, 'SUFFICIENT', 200, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CLB-PUBLIC-STANDARD', 'ap-hongkong', NULL, 1, 1, 'SUFFICIENT', 50, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'CLB-PRIVATE-STANDARD', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', 500, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CLB-PRIVATE-STANDARD', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', 500, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'CLB-PRIVATE-STANDARD', 'cn-guangzhou', NULL, 1, 1, 'SUFFICIENT', 500, 0, 'ACTIVE', 'system', NOW()),

-- NAT SKU 区域可用性
('DEFAULT', 'NAT-SMALL', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', 100, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'NAT-SMALL', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', 100, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'NAT-SMALL', 'cn-guangzhou', NULL, 1, 1, 'SUFFICIENT', 100, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'NAT-MEDIUM', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', 50, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'NAT-MEDIUM', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', 50, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'NAT-LARGE', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', 20, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'NAT-LARGE', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', 20, 0, 'ACTIVE', 'system', NOW()),

-- EIP SKU 区域可用性
('DEFAULT', 'EIP-BGP-1M', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', 200, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'EIP-BGP-1M', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', 200, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'EIP-BGP-1M', 'cn-guangzhou', NULL, 1, 1, 'SUFFICIENT', 200, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'EIP-BGP-1M', 'ap-hongkong', NULL, 1, 1, 'SUFFICIENT', 50, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'EIP-BGP-5M', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', 100, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'EIP-BGP-5M', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', 100, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'EIP-BGP-5M', 'cn-guangzhou', NULL, 1, 1, 'SUFFICIENT', 100, 0, 'ACTIVE', 'system', NOW()),

-- APP 订阅全球可用
('DEFAULT', 'APP-BASIC-MONTHLY', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'APP-BASIC-MONTHLY', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'APP-BASIC-MONTHLY', 'ap-hongkong', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'APP-BASIC-MONTHLY', 'ap-singapore', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'APP-PRO-MONTHLY', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'APP-PRO-MONTHLY', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'APP-PRO-MONTHLY', 'ap-hongkong', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),

('DEFAULT', 'APP-ENT-MONTHLY', 'cn-beijing', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW()),
('DEFAULT', 'APP-ENT-MONTHLY', 'cn-shanghai', NULL, 1, 1, 'SUFFICIENT', NULL, 0, 'ACTIVE', 'system', NOW());


SET FOREIGN_KEY_CHECKS = 1;

-- ========================================================================
-- END OF INIT DATA SCRIPT
-- ========================================================================
