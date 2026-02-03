# Product Center 代码更新计划

## 1. SQL 表结构变更总览

### 1.1 表数量变化

- 原始：11张表
- 更新后：12张表（新增 `sku_pricing_link`）

### 1.2 各表变更明细

#### product_sku (售卖单元)

| 变更类型 | 字段                      | 类型            | 说明                     |
|------|-------------------------|---------------|------------------------|
| 新增   | `revision`              | VARCHAR(32)   | SKU版本号(yyyyMMddHHmmss) |
| 新增   | `base_unit_price`       | DECIMAL(20,6) | 基准单价                   |
| 新增   | `currency`              | VARCHAR(8)    | 币种                     |
| 新增   | `pricing_strategy_code` | VARCHAR(64)   | 默认定价策略编码               |
| 新增   | `billing_strategy_code` | VARCHAR(64)   | 默认计费策略编码               |
| 新增   | `is_current`            | TINYINT→Short | 是否当前主版本                |
| 新增   | `effective_time`        | DATETIME      | 生效时间                   |
| 新增   | `expiry_time`           | DATETIME      | 失效时间                   |
| 删除   | `revision_id`           | -             | 已更名为 revision          |

---

#### sku_item_combination (SKU BOM)

| 变更类型 | 字段             | 类型          | 说明       |
|------|----------------|-------------|----------|
| 新增   | `sku_revision` | VARCHAR(32) | 关联SKU版本号 |

---

#### sku_pricing_link (新增表)

| 字段                 | 类型         | 说明       |
|--------------------|------------|----------|
| `id`               | Long       | 主键       |
| `tenant_id`        | String     | 租户ID     |
| `sku_code`         | String     | SKU编码    |
| `sku_revision`     | String     | SKU版本号   |
| `pricing_code`     | String     | 定价编码     |
| `pricing_revision` | String     | 定价版本号    |
| `override_factor`  | BigDecimal | 覆盖系数     |
| `is_default`       | Short      | 是否默认收费模式 |
| `status`           | String     | 状态       |

---

#### sku_pricing (定价模板)

| 变更类型 | 字段                                        | 类型            | 说明          |
|------|-------------------------------------------|---------------|-------------|
| 新增   | `pricing_code`                            | VARCHAR(64)   | 定价编码        |
| 新增   | `revision`                                | VARCHAR(32)   | 定价版本号       |
| 新增   | `metering_mode`                           | VARCHAR(32)   | 计量方式        |
| 新增   | `payment_mode`                            | VARCHAR(32)   | 付费方式        |
| 新增   | `billing_cycle`                           | VARCHAR(32)   | 计费周期        |
| 新增   | `cycle_count`                             | INT           | 周期数量        |
| 新增   | `billing_unit`                            | VARCHAR(32)   | 计费单位        |
| 新增   | `pricing_strategy_code`                   | VARCHAR(64)   | 定价策略编码      |
| 新增   | `billing_strategy_code`                   | VARCHAR(64)   | 计费策略编码      |
| 新增   | `refund_policy`                           | VARCHAR(32)   | 退款政策        |
| 新增   | `unit_price`                              | DECIMAL(20,6) | 单价          |
| 新增   | `price_factor`                            | DECIMAL(10,4) | 价格系数        |
| 新增   | `metering_unit`                           | VARCHAR(32)   | 计量单位        |
| 新增   | `metering_precision`                      | INT           | 计量精度        |
| 新增   | `is_current`                              | TINYINT→Short | 是否当前版本      |
| 新增   | `remark`                                  | TEXT          | 备注          |
| 删除   | `sku_code`                                | -             | 通过 link 表关联 |
| 删除   | `sku_revision`                            | -             | 通过 link 表关联 |
| 更名   | `strategy_code` → `pricing_strategy_code` | -             | 消除歧义        |

---

## 2. Java 代码修改清单

### 2.1 DO 实体类

#### [MODIFY] ProductSkuDO.java

```java
// 新增字段
private String revision;           // SKU版本号
private BigDecimal baseUnitPrice;  // 基准单价
private String currency;           // 币种
private String pricingStrategyCode;// 默认定价策略编码
private String billingStrategyCode;// 默认计费策略编码
private Short isCurrent;           // 是否当前主版本
private Date effectiveTime;        // 生效时间
private Date expiryTime;           // 失效时间

// 删除字段
// private String revisionId;  -- 删除
```

#### [MODIFY] SkuItemCombinationDO.java

```java
// 新增字段
private String skuRevision;  // 关联SKU版本号
```

#### [NEW] SkuPricingLinkDO.java

```java
@Data
@TableName("sku_pricing_link")
public class SkuPricingLinkDO {
    private Long id;
    private String tenantId;
    private String skuCode;
    private String skuRevision;
    private String pricingCode;
    private String pricingRevision;
    private BigDecimal overrideFactor;
    private Short isDefault;
    private String status;
    // 审计字段...
}
```

#### [MODIFY] SkuPricingDO.java (重构)

```java
// 完全重构，新结构：
private String pricingCode;         // 定价编码
private String revision;            // 定价版本号
private String meteringMode;        // 计量方式
private String paymentMode;         // 付费方式
private String billingCycle;        // 计费周期
private Integer cycleCount;         // 周期数量
private String billingUnit;         // 计费单位
private String pricingStrategyCode; // 定价策略编码
private String billingStrategyCode; // 计费策略编码
private String refundPolicy;        // 退款政策
private BigDecimal unitPrice;       // 单价
private BigDecimal originalPrice;   // 原价
private BigDecimal salePrice;       // 售价
private String currency;            // 币种
private BigDecimal discountRate;    // 折扣率
private BigDecimal priceFactor;     // 价格系数
private String meteringUnit;        // 计量单位
private Integer meteringPrecision;  // 计量精度
private Date effectiveTime;         // 生效时间
private Date expiryTime;            // 失效时间
private Short isCurrent;            // 是否当前版本
private Integer priority;           // 优先级
private String status;              // 状态
private String remark;              // 备注

// 删除字段
// private String skuCode;       -- 通过 link 表关联
// private String skuRevision;   -- 通过 link 表关联
// private String strategyCode;  -- 改名为 pricingStrategyCode
```

---

### 2.2 Mapper 层

- [NEW] `SkuPricingLinkMapper.java`
- [MODIFY] 更新 XML 查询语句

### 2.3 Service 层

- 更新相关服务方法签名

---

## 3. 前端同步修改指南

### 3.1 API 字段变更

#### ProductSku 接口

| 操作 | 字段                    | 类型     |
|----|-----------------------|--------|
| 新增 | `revision`            | string |
| 新增 | `baseUnitPrice`       | number |
| 新增 | `currency`            | string |
| 新增 | `pricingStrategyCode` | string |
| 新增 | `billingStrategyCode` | string |
| 新增 | `isCurrent`           | number |
| 新增 | `effectiveTime`       | date   |
| 新增 | `expiryTime`          | date   |
| 删除 | `revisionId`          | -      |

#### SkuPricing 接口 (重构)

| 操作 | 字段                    | 类型     |
|----|-----------------------|--------|
| 新增 | `pricingCode`         | string |
| 新增 | `revision`            | string |
| 新增 | `meteringMode`        | string |
| 新增 | `paymentMode`         | string |
| 新增 | `billingCycle`        | string |
| 新增 | `cycleCount`          | number |
| 新增 | `billingUnit`         | string |
| 新增 | `pricingStrategyCode` | string |
| 新增 | `billingStrategyCode` | string |
| 新增 | `refundPolicy`        | string |
| 新增 | `unitPrice`           | number |
| 新增 | `priceFactor`         | number |
| 新增 | `meteringUnit`        | string |
| 新增 | `meteringPrecision`   | number |
| 新增 | `isCurrent`           | number |
| 新增 | `remark`              | string |
| 删除 | `skuCode`             | -      |
| 删除 | `strategyCode`        | -      |

#### 新增接口: SkuPricingLink

| 字段                | 类型     |
|-------------------|--------|
| `skuCode`         | string |
| `skuRevision`     | string |
| `pricingCode`     | string |
| `pricingRevision` | string |
| `overrideFactor`  | number |
| `isDefault`       | number |
| `status`          | string |

### 3.2 枚举值

```typescript
// 计量方式
type MeteringMode = 'BY_USAGE' | 'BY_QUOTA';

// 付费方式
type PaymentMode = 'POSTPAID' | 'PREPAID' | 'SUBSCRIPTION';

// 计费周期
type BillingCycle = 'HOURLY' | 'DAILY' | 'MONTHLY' | 'QUARTERLY' | 'YEARLY' | 'ONCE';

// 计费单位
type BillingUnit = 'PERIOD' | 'QUANTITY';

// 退款政策
type RefundPolicy = 'PRO_RATA' | 'NON_REFUNDABLE';

// 定价策略类型
type StrategyType = 'LINEAR' | 'TIERED' | 'VOLUME_DISCOUNT' | 'REGION' | 'PROMOTION';

// 应用范围
type ApplyScope = 'ALL' | 'SKU' | 'PRODUCT_LINE';
```

---

## 4. 新增接口: PricingStrategy (定价策略)

### 4.1 接口端点

| 方法     | 路径                              | 描述          |
|--------|---------------------------------|-------------|
| POST   | `/pricing-strategy/list`        | 查询策略列表      |
| POST   | `/pricing-strategy/page`        | 分页查询策略      |
| GET    | `/pricing-strategy/detail/{id}` | 策略详情(含阶梯参数) |
| GET    | `/pricing-strategy/by-code`     | 根据编码查询      |
| GET    | `/pricing-strategy/effective`   | 查询有效策略      |
| POST   | `/pricing-strategy/create`      | 创建策略        |
| PUT    | `/pricing-strategy/update`      | 更新策略        |
| DELETE | `/pricing-strategy/delete/{id}` | 删除策略        |
| POST   | `/pricing-strategy/{id}/params` | 保存阶梯参数      |

### 4.2 PricingStrategy 字段

| 字段                | 类型     | 说明             |
|-------------------|--------|----------------|
| `id`              | number | 主键             |
| `tenantId`        | string | 租户ID           |
| `strategyCode`    | string | 策略编码           |
| `strategyName`    | string | 策略名称           |
| `strategyType`    | string | 策略类型           |
| `applyScope`      | string | 应用范围           |
| `applyScopeValue` | string | 范围值(SKU编码/产品线) |
| `strategyConfig`  | object | 策略配置(JSON)     |
| `priority`        | number | 优先级            |
| `effectiveTime`   | date   | 生效时间           |
| `expiryTime`      | date   | 失效时间           |
| `status`          | string | 状态             |
| `remark`          | string | 备注             |

### 4.3 PricingStrategyParam 字段 (阶梯参数)

| 字段            | 类型     | 说明             |
|---------------|--------|----------------|
| `id`          | number | 主键             |
| `strategyId`  | number | 关联策略ID         |
| `pricingId`   | number | 关联定价ID(可选)     |
| `rangeStart`  | number | 区间起始           |
| `rangeEnd`    | number | 区间结束(null=无穷大) |
| `unitPrice`   | number | 阶梯单价           |
| `fixedAmount` | number | 固定附加费          |
| `sortOrder`   | number | 排序             |

