package com.lc.product.center.domain.dto;

import com.lc.framework.core.page.PaginationParams;
import com.lc.framework.core.utils.validator.Groups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
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
 * 定价策略表(product_center.pricing_strategy)数据传输对象
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "PricingStrategyDTO", description = "定价策略DTO")
public class PricingStrategyDTO implements Serializable, PaginationParams {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @NotNull(message = "ID不能为空", groups = { Groups.UpdateGroup.class })
    private Long id;

    @Schema(description = "租户ID")
    private String tenantId;

    // ==================== 策略标识 ====================

    @Schema(description = "策略编码")
    @NotBlank(message = "策略编码不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private String strategyCode;

    @Schema(description = "策略名称")
    @NotBlank(message = "策略名称不能为空", groups = { Groups.AddGroup.class, Groups.UpdateGroup.class })
    private String strategyName;

    // ==================== 策略类型 ====================

    @Schema(description = "策略类型: LINEAR/TIERED/VOLUME_DISCOUNT/REGION/PROMOTION")
    @NotBlank(message = "策略类型不能为空", groups = { Groups.AddGroup.class })
    private String strategyType;

    // ==================== 应用范围 ====================

    @Schema(description = "应用范围: ALL/SKU/PRODUCT_LINE")
    @NotBlank(message = "应用范围不能为空", groups = { Groups.AddGroup.class })
    private String applyScope;

    @Schema(description = "范围值: SKU编码或产品线")
    private String applyScopeValue;

    // ==================== 计算方法 ====================

    @Schema(description = "计算方法: MULTIPLY(乘法)/SUBTRACT(减法)")
    private String calcMethod;

    // ==================== 优先级 ====================

    @Schema(description = "优先级")
    private Integer priority;

    // ==================== 时间有效性 ====================

    @Schema(description = "生效时间")
    private Date effectiveTime;

    @Schema(description = "失效时间")
    private Date expiryTime;

    // ==================== 状态 ====================

    @Schema(description = "状态: ACTIVE/INACTIVE")
    private String status;

    @Schema(description = "备注说明")
    private String remark;

    // ==================== 分页参数 ====================

    @Min(value = 1, message = "{page.index}", groups = { Groups.PageGroup.class })
    private Long pageIndex;

    @Min(value = 1, message = "{page.pageSize}", groups = { Groups.PageGroup.class })
    private Long pageSize;

    private Long total;
}
