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
import java.util.Date;

/**
 * 定价模板表(product_center.sku_pricing)表数据传输类
 *
 * @author lucheng
 * @since 2026-01-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SkuPricingDTO", description = "定价模板DTO")
public class SkuPricingDTO implements Serializable, PaginationParams {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    @NotNull(message = "id不能为空", groups = { Groups.DeleteGroup.class, Groups.UpdateGroup.class })
    private Long id;

    @Schema(description = "租户ID")
    @Null(message = "禁止修改租户ID", groups = Groups.UpdateGroup.class)
    private String tenantId;

    // ==================== 业务标识 ====================

    @Schema(description = "定价编码: POSTPAID-MONTHLY-LINEAR")
    @NotBlank(message = "定价编码不能为空", groups = Groups.AddGroup.class)
    @Null(message = "禁止修改定价编码", groups = Groups.UpdateGroup.class)
    private String pricingCode;

    @Schema(description = "定价版本号: yyyyMMddHHmmss")
    private String revision;

    // ==================== 四维度收费模式 ====================

    @Schema(description = "计量方式: BY_USAGE/BY_QUOTA")
    @NotBlank(message = "计量方式不能为空", groups = Groups.AddGroup.class)
    private String meteringMode;

    @Schema(description = "付费方式: POSTPAID/PREPAID/SUBSCRIPTION")
    @NotBlank(message = "付费方式不能为空", groups = Groups.AddGroup.class)
    private String paymentMode;

    @Schema(description = "计费周期: HOURLY/DAILY/MONTHLY/QUARTERLY/YEARLY/ONCE")
    @NotBlank(message = "计费周期不能为空", groups = Groups.AddGroup.class)
    private String billingCycle;

    @Schema(description = "周期数量: 1月/3月/12月")
    private Integer cycleCount;

    @Schema(description = "计费单位: PERIOD/QUANTITY")
    @NotBlank(message = "计费单位不能为空", groups = Groups.AddGroup.class)
    private String billingUnit;

    @Schema(description = "退款政策: PRO_RATA/NON_REFUNDABLE")
    private String refundPolicy;

    // ==================== 价格信息 ====================

    @Schema(description = "折扣率: 0.85表示85折")
    private BigDecimal discountRate;

    @Schema(description = "币种: CNY/USD")
    private String currency;

    // ==================== 计量配置 ====================

    @Schema(description = "计量单位: 核·小时/GB·月/次")
    private String meteringUnit;

    @Schema(description = "计量精度: 小数位数")
    private Integer meteringPrecision;

    // ==================== 时间与优先级 ====================

    @Schema(description = "生效时间")
    @NotNull(message = "生效时间不能为空", groups = Groups.AddGroup.class)
    private Date effectiveTime;

    @Schema(description = "失效时间")
    private Date expiryTime;

    @Schema(description = "是否当前主版本: 1是 0否")
    private Boolean isCurrent;

    @Schema(description = "优先级(数值越大优先级越高)")
    private Integer priority;

    // ==================== 状态与备注 ====================

    @Schema(description = "状态: ACTIVE/INACTIVE")
    private String status;

    @Schema(description = "备注说明")
    private String remark;

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
