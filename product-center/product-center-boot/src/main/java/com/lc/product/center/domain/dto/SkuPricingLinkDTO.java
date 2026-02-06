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

/**
 * SKU与定价关联表(product_center.sku_pricing_link)表数据传输类
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SkuPricingLinkDTO", description = "SKU与定价关联DTO")
public class SkuPricingLinkDTO implements Serializable, PaginationParams {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    @NotNull(message = "id不能为空", groups = { Groups.DeleteGroup.class, Groups.UpdateGroup.class })
    private Long id;

    @Schema(description = "租户ID")
    @Null(message = "禁止修改租户ID", groups = Groups.UpdateGroup.class)
    private String tenantId;

    // ==================== SKU关联 ====================

    @Schema(description = "SKU编码")
    @NotBlank(message = "SKU编码不能为空", groups = Groups.AddGroup.class)
    private String skuCode;

    @Schema(description = "SKU版本号")
    @NotBlank(message = "SKU版本号不能为空", groups = Groups.AddGroup.class)
    private String skuRevision;

    // ==================== 定价关联 ====================

    @Schema(description = "定价编码")
    @NotBlank(message = "定价编码不能为空", groups = Groups.AddGroup.class)
    private String pricingCode;

    @Schema(description = "定价版本号")
    @NotBlank(message = "定价版本号不能为空", groups = Groups.AddGroup.class)
    private String pricingRevision;

    // ==================== 覆盖配置 ====================

    @Schema(description = "覆盖系数(可选)")
    @DecimalMin(value = "0.00", message = "系数不能为负数", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private BigDecimal overrideFactor;

    @Schema(description = "是否默认收费模式: 1是 0否")
    private Boolean isDefault;

    // ==================== 状态 ====================

    @Schema(description = "状态: ACTIVE/INACTIVE")
    private String status;

    // ==================== 分页参数 ====================

    @Min(value = 1, message = "{page.index}", groups = { Groups.PageGroup.class })
    private Long pageIndex;

    @Min(value = 1, message = "{page.pageSize}", groups = { Groups.PageGroup.class })
    private Long pageSize;

    private Long total;
}
