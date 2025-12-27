package com.lc.product.center.domain.dto;

import com.lc.framework.core.page.PaginationParams;
import com.lc.framework.core.utils.validator.Groups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * SKU定价表(product_center.sku_pricing)表数据传输类
 *
 * @author lucheng
 * @since 2025-12-27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SkuPricingDTO", description = "SKU定价DTO")
public class SkuPricingDTO implements Serializable, PaginationParams {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    @NotNull(message = "id不能为空", groups = { Groups.DeleteGroup.class, Groups.UpdateGroup.class })
    private Long id;

    @Schema(description = "租户ID")
    @Null(message = "禁止修改租户ID", groups = Groups.UpdateGroup.class)
    private String tenantId;

    @Schema(description = "SKU编码")
    @NotBlank(message = "SKU编码不能为空", groups = Groups.AddGroup.class)
    @Null(message = "禁止修改SKU编码", groups = Groups.UpdateGroup.class)
    private String skuCode;

    @Schema(description = "定价模式: PAY_AS_GO/PREPAID/SUBSCRIPTION")
    @NotBlank(message = "定价模式不能为空", groups = Groups.AddGroup.class)
    private String pricingModel;

    @Schema(description = "计费周期: HOURLY/DAILY/MONTHLY/QUARTERLY/YEARLY")
    private String billingPeriod;

    @Schema(description = "周期数量")
    private Integer periodCount;

    @Schema(description = "原价")
    @DecimalMin(value = "0.00", message = "价格不能为负数", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private BigDecimal originalPrice;

    @Schema(description = "售价")
    @NotNull(message = "售价不能为空", groups = Groups.AddGroup.class)
    @DecimalMin(value = "0.00", message = "价格不能为负数", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private BigDecimal salePrice;

    @Schema(description = "币种: CNY/USD")
    private String currency;

    @Schema(description = "折扣率: 0.85表示85折")
    private BigDecimal discountRate;

    @Schema(description = "生效时间")
    private Date effectiveTime;

    @Schema(description = "失效时间")
    private Date expiryTime;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "状态: ACTIVE/INACTIVE")
    private String status;

    // ==================== 查询控制参数 ====================

    @Schema(description = "是否只查询有效定价")
    private Boolean effectiveOnly;

    // ==================== 分页参数 ====================

    @Min(value = 1, message = "{page.index}", groups = { Groups.PageGroup.class })
    private Long pageIndex;

    @Min(value = 1, message = "{page.pageSize}", groups = { Groups.PageGroup.class })
    private Long pageSize;

    private Long total;
}
