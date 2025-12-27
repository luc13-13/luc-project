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

/**
 * 产品SKU表(product_center.product_sku)表数据传输类
 *
 * @author lucheng
 * @since 2025-12-21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ProductSkuDTO", description = "产品SKU DTO")
public class ProductSkuDTO implements Serializable, PaginationParams {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Schema(description = "主键id")
    @NotNull(message = "id cannot be null", groups = { Groups.DeleteGroup.class, Groups.UpdateGroup.class })
    private Long id;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    @Null(message = "禁止修改租户ID", groups = Groups.UpdateGroup.class)
    private String tenantId;

    // ==================== SKU基本信息 ====================

    /**
     * SKU编码
     */
    @Schema(description = "SKU编码: CVM-S5-4C8G")
    @NotBlank(message = "SKU编码不能为空", groups = Groups.AddGroup.class)
    @Null(message = "禁止修改SKU编码", groups = Groups.UpdateGroup.class)
    private String skuCode;

    /**
     * SKU名称
     */
    @Schema(description = "SKU名称: 通用型S5 4核8G")
    @NotBlank(message = "SKU名称不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private String skuName;

    // ==================== 关联产品 ====================

    /**
     * 所属产品编码
     */
    @Schema(description = "所属产品编码")
    @NotBlank(message = "产品编码不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private String productCode;

    /**
     * 所属规格族编码
     */
    @Schema(description = "所属规格族编码")
    @NotBlank(message = "规格族编码不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private String subProductCode;

    // ==================== SKU类型 ====================

    /**
     * SKU类型
     */
    @Schema(description = "SKU类型: INSTANCE/ADDON/BUNDLE/SUBSCRIPTION")
    @NotBlank(message = "SKU类型不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private String skuType;

    // ==================== 售卖控制 ====================

    /**
     * 是否可售
     */
    @Schema(description = "是否可售: 1是 0否")
    @NotNull(message = "是否可售不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private Short saleable;

    /**
     * 是否可见
     */
    @Schema(description = "是否可见: 1是 0否")
    @NotNull(message = "是否可见不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private Short visible;

    /**
     * 配额限制
     */
    @Schema(description = "配额限制，NULL表示无限制")
    private Integer quotaLimit;

    // ==================== 状态 ====================

    /**
     * 状态
     */
    @Schema(description = "状态: DRAFT/ACTIVE/INACTIVE")
    @NotBlank(message = "状态不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private String status;

    // ==================== 分页参数 ====================

    @Min(value = 1, message = "{page.index}", groups = { Groups.PageGroup.class })
    private Long pageIndex;

    @Min(value = 1, message = "{page.pageSize}", groups = { Groups.PageGroup.class })
    private Long pageSize;

    private Long total;
}
