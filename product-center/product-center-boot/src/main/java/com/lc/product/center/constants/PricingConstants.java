package com.lc.product.center.constants;

import lombok.Getter;

/**
 * <pre>
 *    sku定价相关常量
 * <pre/>
 * @author : Lu Cheng
 * @date : 3/2/26 09:52
 * @version : 1.0
 */
public interface PricingConstants {
    /**
     * 定价策略
     */
    @Getter
    enum PricingStrategyTypeEnum {
        LINEAR("LINEAR","线性定价"),
        TIERED("TIERED","阶梯定价"),
        VOLUME_DISCOUNT("VOLUME_DISCOUNT", "批量折扣"),
        REGION("REGION", "区域定价"),
        PROMOTION("PROMOTION", "促销定价");

        final String type;

        final String desc;

        PricingStrategyTypeEnum(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public static String getDescByType(String type) {
            for (PricingStrategyTypeEnum pricingStrategyTypeEnum : PricingStrategyTypeEnum.values()) {
                if (pricingStrategyTypeEnum.type.equals(type)) {
                    return pricingStrategyTypeEnum.desc;
                }
            }
            return null;
        }
    }

    /**
     * 定价策略应用范围
     */
    @Getter
    enum StrategyApplyScopeEnum {
        ALL("ALL", "全局"),
        SKU("SKU", "指定SKU"),
        PRODUCT_LINE( "PRODUCT_LINE", "产品线");

        final String scope;

        final String desc;

        StrategyApplyScopeEnum(String scope, String desc) {
            this.scope = scope;
            this.desc = desc;
        }

        public static String getDescByScope(String scope) {
            for (StrategyApplyScopeEnum strategyApplyScope : StrategyApplyScopeEnum.values()) {
                if (strategyApplyScope.scope.equals(scope)) {
                    return strategyApplyScope.desc;
                }
            }
            return null;
        }
    }

    /**
     * 定价维度-计量方式
     */
    @Getter
    enum MeteringModeEnum {
        BY_USAGE("BY_USAGE", "按用量"),
        BY_QUOTA("BY_QUOTA", "按配额");

        final String scope;

        final String desc;

        MeteringModeEnum(String scope, String desc) {
            this.scope = scope;
            this.desc = desc;
        }

        public static String getDescByScope(String scope) {
            for (MeteringModeEnum meteringModeEnum : MeteringModeEnum.values()) {
                if (meteringModeEnum.scope.equals(scope)) {
                    return meteringModeEnum.desc;
                }
            }
            return null;
        }
    }

    /**
     * 定价维度-付费方式
     */
    @Getter
    enum PaymentModeEnum {
        POSTPAID("POSTPAID", "后付费"),
        PREPAID("PREPAID", "预付费"),
        SUBSCRIPTION("SUBSCRIPTION", "订阅制");

        final String scope;

        final String desc;

        PaymentModeEnum(String scope, String desc) {
            this.scope = scope;
            this.desc = desc;
        }

        public static String getDescByScope(String scope) {
            for (PaymentModeEnum paymentModeEnum : PaymentModeEnum.values()) {
                if (paymentModeEnum.scope.equals(scope)) {
                    return paymentModeEnum.desc;
                }
            }
            return null;
        }
    }

    /**
     * 定价维度-计费周期
     */
    @Getter
    enum BillingCycleEnum {
        HOURLY("HOURLY", "按小时"),
        DAILY("DAILY", "按天"),
        MONTHLY("MONTHLY", "按月"),
        QUARTERLY("QUARTERLY", "按季度"),
        YEARLY("YEARLY", "按年"),
        ONCE("ONCE", "一次性");

        final String scope;

        final String desc;

        BillingCycleEnum(String scope, String desc) {
            this.scope = scope;
            this.desc = desc;
        }

        public static String getDescByScope(String scope) {
            for (BillingCycleEnum billingCycleEnum : BillingCycleEnum.values()) {
                if (billingCycleEnum.scope.equals(scope)) {
                    return billingCycleEnum.desc;
                }
            }
            return null;
        }
    }

    /**
     * 定价维度-计费单位类型
     */
    @Getter
    enum BillingUnitEnum {
        PERIOD("PERIOD", "按周期"),
        QUANTITY("QUANTITY", "按数量");

        final String scope;

        final String desc;

        BillingUnitEnum(String scope, String desc) {
            this.scope = scope;
            this.desc = desc;
        }

        public static String getDescByScope(String scope) {
            for (BillingUnitEnum billingUnitEnum : BillingUnitEnum.values()) {
                if (billingUnitEnum.scope.equals(scope)) {
                    return billingUnitEnum.desc;
                }
            }
            return null;
        }
    }

}
