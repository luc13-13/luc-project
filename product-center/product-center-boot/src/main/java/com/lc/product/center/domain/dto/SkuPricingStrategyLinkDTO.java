package com.lc.product.center.domain.dto;

import com.lc.framework.core.utils.validator.Groups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * SKU与策略关联表(product_center.sku_pricing_strategy_link)数据传输对象
 *
 * @author lucheng
 * @since 2026-02-05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SkuPricingStrategyLinkDTO", description = "SKU与策略关联DTO")
public class SkuPricingStrategyLinkDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @NotNull(message = "ID不能为空", groups = { Groups.UpdateGroup.class })
    private Long id;

    @Schema(description = "租户ID")
    private String tenantId;

    // ==================== SKU关联 ====================

    @Schema(description = "SKU编码")
    @NotBlank(message = "SKU编码不能为空", groups = { Groups.AddGroup.class })
    private String skuCode;

    @Schema(description = "SKU版本号")
    @NotBlank(message = "SKU版本号不能为空", groups = { Groups.AddGroup.class })
    private String skuRevision;

    // ==================== 策略关联 ====================

    @Schema(description = "策略编码")
    @NotBlank(message = "策略编码不能为空", groups = { Groups.AddGroup.class })
    private String strategyCode;

    // ==================== 执行优先级 ====================

    @Schema(description = "优先级(NULL使用策略默认值)")
    private Integer priority;

    // ==================== 有效期 ====================

    @Schema(description = "生效时间")
    private Date effectiveTime;

    @Schema(description = "失效时间")
    private Date expiryTime;

    // ==================== 状态 ====================

    @Schema(description = "状态: ACTIVE/INACTIVE")
    private String status;
}
