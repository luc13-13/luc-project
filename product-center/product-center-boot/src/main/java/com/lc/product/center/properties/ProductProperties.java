package com.lc.product.center.properties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 10/2/26 09:31
 * @version : 1.0
 */
@Data
@ConfigurationProperties(prefix = "product")
public class ProductProperties {

    @Schema(description = "计量方式: BY_USAGE/BY_QUOTA")
    private Set<String> meteringMode = new HashSet<>();

    @Schema(description = "付费方式: POSTPAID/PREPAID/SUBSCRIPTION")
    private Set<String> paymentMode = new HashSet<>();

    @Schema(description = "计费周期: HOURLY/DAILY/MONTHLY/QUARTERLY/YEARLY/ONCE")
    private Set<String> billingCycle = new HashSet<>();

    @Schema(description = "计费单位: PERIOD/QUANTITY")
    private Set<String> billingUnit = new HashSet<>();
}
