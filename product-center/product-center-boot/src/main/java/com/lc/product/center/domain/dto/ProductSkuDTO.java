package com.lc.product.center.domain.dto;

import com.lc.framework.core.page.PaginationParams;
import com.lc.framework.core.utils.validator.Groups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 产品SKU表(product_center.product_sku)表数据传输类
 *
 * @author lucheng
 * @since 2026-01-31
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

    // ==================== 版本控制 ====================

    /**
     * SKU版本号
     */
    @Schema(description = "SKU版本号: yyyyMMddHHmmss")
    private String revision;

    // ==================== SKU类型 ====================

    /**
     * SKU类型
     */
    @Schema(description = "SKU类型: INSTANCE/ADDON/BUNDLE/SUBSCRIPTION")
    @NotBlank(message = "SKU类型不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private String skuType;

    // ==================== 基准定价 ====================

    /**
     * 基准单价
     */
    @Schema(description = "基准单价")
    @NotNull(message = "基准单价不能为空", groups = Groups.AddGroup.class)
    @DecimalMin(value = "0.00", message = "价格不能为负数", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private BigDecimal baseUnitPrice;

    /**
     * 币种
     */
    @Schema(description = "币种: CNY/USD")
    private String currency;

    // ==================== 售卖控制 ====================

    /**
     * 是否可售
     */
    @Schema(description = "是否可售: 1是 0否")
    @NotNull(message = "是否可售不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private Boolean saleable;

    /**
     * 是否可见
     */
    @Schema(description = "是否可见: 1是 0否")
    @NotNull(message = "是否可见不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private Boolean visible;

    /**
     * 配额限制
     */
    @Schema(description = "配额限制，NULL表示无限制")
    private Integer quotaLimit;

    // ==================== 版本状态 ====================

    /**
     * 是否当前主版本
     */
    @Schema(description = "是否当前主版本: 1是 0否")
    private Boolean isCurrent;

    /**
     * 生效时间
     */
    @Schema(description = "生效时间")
    private Date effectiveTime;

    /**
     * 失效时间
     */
    @Schema(description = "失效时间")
    private Date expiryTime;

    /**
     * 状态
     */
    @Schema(description = "状态: DRAFT/ACTIVE/INACTIVE")
    @NotBlank(message = "状态不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private String status;

    // ==================== 关联数据（创建/更新时可传入） ====================

    /**
     * 计费项组合列表（BOM）
     */
    @Schema(description = "计费项组合列表")
    @NotEmpty(message = "计费项组合不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private List<SkuItemCombinationDTO> itemCombinations;

    /**
     * 定价模板关联列表
     */
    @Schema(description = "定价模板关联列表")
    private List<SkuPricingLinkDTO> pricingLinks;

    /**
     * 定价策略关联列表
     */
    @Schema(description = "定价策略关联列表")
    private List<SkuPricingStrategyLinkDTO> strategyLinks;

    @Schema(description = "是否删除：1是，0否")
    private Boolean deleted;

    // ==================== 分页参数 ====================

    @Min(value = 1, message = "{page.index}", groups = { Groups.PageGroup.class })
    private Long pageIndex;

    @Min(value = 1, message = "{page.pageSize}", groups = { Groups.PageGroup.class })
    private Long pageSize;

    private Long total;
}
