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
public interface PricingStrategyConstants {

    @Getter
    enum StrategyTypeEnum {
        LINEAR("LINEAR","线性定价"),
        TIERED("TIERED","阶梯定价"),
        VOLUME_DISCOUNT("VOLUME_DISCOUNT", "批量折扣"),
        REGION("REGION", "区域定价"),
        PROMOTION("PROMOTION", "促销定价");

        final String type;

        final String desc;

        StrategyTypeEnum(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public static String getDescByType(String type) {
            for (StrategyTypeEnum strategyTypeEnum : StrategyTypeEnum.values()) {
                if (strategyTypeEnum.type.equals(type)) {
                    return strategyTypeEnum.desc;
                }
            }
            return null;
        }
    }

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
}
