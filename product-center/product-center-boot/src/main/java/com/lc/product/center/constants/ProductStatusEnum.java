package com.lc.product.center.constants;

import lombok.Getter;

/**
 * 产品状态常量
 *
 * @author lucheng
 * @since 2025-12-26
 */
@Getter
public enum ProductStatusEnum {
    DRAFT("DRAFT", "草稿"),
    ACTIVE("ACTIVE", "生效"),
    INACTIVE("INACTIVE", "失效");

    final String code;

    final String description;

    ProductStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String getDescByCode(String code) {
        for (ProductStatusEnum productStatusEnum : ProductStatusEnum.values()) {
            if (productStatusEnum.code.equals(code)) {
                return productStatusEnum.description;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
