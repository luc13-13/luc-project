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
 * 产品信息表(product_center.product_info)表数据传输类
 *
 * @author lucheng
 * @since 2025-08-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ProductInfoDTO", description = "产品信息DTO")
public class ProductInfoDTO implements Serializable, PaginationParams {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Schema(description = "主键id")
    @NotNull(groups = { Groups.UpdateGroup.class, Groups.DeleteGroup.class })
    private Long id;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    @Null(message = "禁止修改租户ID", groups = Groups.UpdateGroup.class)
    private String tenantId;

    // ==================== 四层产品结构 ====================

    /**
     * 产品编码: CVM/CBS/CLB
     */
    @Schema(description = "产品编码: CVM/CBS/CLB")
    @NotBlank(message = "产品编码不能为空", groups = Groups.AddGroup.class)
    @Null(message = "禁止修改产品编码", groups = Groups.UpdateGroup.class)
    private String productCode;

    /**
     * 规格族编码: S5_GENERAL/C6_COMPUTE
     */
    @Schema(description = "规格族编码: S5_GENERAL/C6_COMPUTE")
    @NotBlank(message = "规格族编码不能为空", groups = Groups.AddGroup.class)
    @Null(message = "禁止修改", groups = Groups.UpdateGroup.class)
    private String subProductCode;

    /**
     * 计费项编码: CPU/MEMORY/STORAGE
     */
    @Schema(description = "计费项编码: CPU/MEMORY/STORAGE")
    @NotBlank(message = "计费项编码不能为空", groups = Groups.AddGroup.class)
    @Null(message = "禁止修改", groups = Groups.UpdateGroup.class)
    private String billingItemCode;

    /**
     * 计费规格编码: INTEL_4C/HYGON_4C
     */
    @Schema(description = "子计费规格编码: INTEL_4C/HYGON_4C")
    @NotBlank(message = "子计费规格编码不能为空", groups = Groups.AddGroup.class)
    @Null(message = "禁止修改子计费规格编码", groups = Groups.UpdateGroup.class)
    private String subBillingItemCode;

    // ==================== 名称 ====================

    /**
     * 产品名称
     */
    @Schema(description = "产品名称")
    @NotBlank(message = "产品名称不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private String productName;

    /**
     * 规格族名称
     */
    @Schema(description = "规格族名称")
    @NotBlank(message = "规格族名称不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private String subProductName;

    /**
     * 计费项名称
     */
    @Schema(description = "计费项名称")
    @NotBlank(message = "计费项名称不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private String billingItemName;

    /**
     * 计费规格名称
     */
    @Schema(description = "子计费规格名称")
    @NotBlank(message = "子计费规格名称不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private String subBillingItemName;

    // ==================== 规格属性 ====================

    /**
     * 规格值: 4, 8, 100
     */
    @Schema(description = "规格值: 4, 8, 100")
    @NotNull(message = "规格值不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private BigDecimal specValue;

    /**
     * 规格单位: 核, GB, Mbps
     */
    @Schema(description = "规格单位: 核, GB, Mbps")
    @NotBlank(message = "规格单位不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private String specUnit;

    // ==================== 计费属性 ====================

    /**
     * 基准单价
     */
    @Schema(description = "基准单价")
    @NotNull(message = "基准单价不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private BigDecimal basePrice;

    /**
     * 价格系数
     */
    @Schema(description = "价格系数，默认1.00")
    @NotNull(message = "价格系数不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private BigDecimal priceFactor;

    /**
     * 计量单位（账单展示）: 核·小时, GB·月
     */
    @Schema(description = "计量单位（账单展示）: 核·小时, GB·月")
    @NotBlank(message = "计量单位不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private String meteringUnit;

    // ==================== 状态与排序 ====================

    /**
     * 状态: DRAFT/ACTIVE/INACTIVE
     */
    @Schema(description = "状态: DRAFT/ACTIVE/INACTIVE")
    @NotBlank(message = "状态不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private String status;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sortOrder;

    // ==================== 分页参数 ====================

    @Min(value = 1, message = "{page.index}", groups = { Groups.PageGroup.class })
    private Long pageIndex;

    @Min(value = 1, message = "{page.pageSize}", groups = { Groups.PageGroup.class })
    private Long pageSize;

    private Long total;
}
