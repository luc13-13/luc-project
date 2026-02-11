package com.lc.product.center.service;

import com.lc.framework.core.utils.validator.EnumValueManager;
import com.lc.product.center.properties.ProductProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 10/2/26 09:35
 * @version : 1.0
 */
@Service
public class ProductEnumValueManager implements EnumValueManager, InitializingBean {


    private final ProductProperties productProperties;

    private final Map<String, Set<String>> enumValueMap = new ConcurrentHashMap<>();

    public ProductEnumValueManager(ProductProperties productProperties) {
        this.productProperties = productProperties;
    }

    @Override
    public Set<String> getEnumValues(String enumName) {
        return enumValueMap.getOrDefault(enumName, Set.of());
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(productProperties, "productProperties should not be null!");
        if (CollectionUtils.isEmpty(productProperties.getMeteringMode())) {
            enumValueMap.put("sku.meteringMode", productProperties.getMeteringMode());
        }
        if (CollectionUtils.isEmpty(productProperties.getPaymentMode())) {
            enumValueMap.put("sku.paymentMode", productProperties.getPaymentMode());
        }
        if (CollectionUtils.isEmpty(productProperties.getBillingCycle())) {
            enumValueMap.put("sku.billingCycle", productProperties.getBillingCycle());
        }
        if (CollectionUtils.isEmpty(productProperties.getBillingUnit())) {
            enumValueMap.put("sku.billingUnit", productProperties.getBillingUnit());
        }
    }
}
