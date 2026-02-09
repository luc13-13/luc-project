package com.lc.product.center.constants;

import lombok.Getter;

/**
 * SKU类型常量
 *
 * @author lucheng
 * @since 2025-12-26
 */
public interface SkuConstants {

    @Getter
    enum SkuTypeEnum {
        INSTANCE("INSTANCE", "实例"),


        ADDON("ADDON", "附加项"),


        BUNDLE("BUNDLE", "套餐"),
        SUBSCRIPTION("SUBSCRIPTION", "订阅");


        final String code;

        final String description;

        SkuTypeEnum(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public static String getDescByCode(String code) {
            for (SkuTypeEnum skuTypeEnum : SkuTypeEnum.values()) {
                if (skuTypeEnum.getCode().equals(code)) {
                    return skuTypeEnum.getDescription();
                }
            }
            return null;
        }
    }
}
