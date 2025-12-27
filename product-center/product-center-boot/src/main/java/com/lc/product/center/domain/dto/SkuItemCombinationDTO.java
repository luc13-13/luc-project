package com.lc.product.center.domain.dto;

import com.lc.framework.core.page.PaginationParams;
import com.lc.framework.core.utils.validator.Groups;
import io.swagger.v3.oas.annotations.media.Schema;
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
 * SKU计费项组合表(product_center.sku_item_combination)表数据传输类
 *
 * @author lucheng
 * @since 2025-12-27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SkuItemCombinationDTO", description = "SKU计费项组合DTO")
public class SkuItemCombinationDTO implements Serializable, PaginationParams {

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

    @Schema(description = "产品编码")
    @NotBlank(message = "产品编码不能为空", groups = Groups.AddGroup.class)
    private String productCode;

    @Schema(description = "规格族编码")
    @NotBlank(message = "规格族编码不能为空", groups = Groups.AddGroup.class)
    private String subProductCode;

    @Schema(description = "计费项编码")
    @NotBlank(message = "计费项编码不能为空", groups = Groups.AddGroup.class)
    private String billingItemCode;

    @Schema(description = "计费规格编码")
    @NotBlank(message = "计费规格编码不能为空", groups = Groups.AddGroup.class)
    private String subBillingItemCode;

    @Schema(description = "数量/份数")
    private BigDecimal quantity;

    @Schema(description = "是否计入SKU定价: 1是 0否")
    private Short pricingIncluded;

    // ==================== 分页参数 ====================

    @Min(value = 1, message = "{page.index}", groups = { Groups.PageGroup.class })
    private Long pageIndex;

    @Min(value = 1, message = "{page.pageSize}", groups = { Groups.PageGroup.class })
    private Long pageSize;

    private Long total;
}
