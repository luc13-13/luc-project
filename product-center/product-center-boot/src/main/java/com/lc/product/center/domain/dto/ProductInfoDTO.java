package com.lc.product.center.domain.dto;

import com.lc.framework.core.page.PaginationParams;
import com.lc.framework.core.utils.validator.Groups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Schema(name = "ProductInfoDTO")
public class ProductInfoDTO implements Serializable , PaginationParams {
    /**
     * 主键id
     */
    @Schema(name = "id", title = "主键id")
    private Long id;

    /**
     * 产品code
     */
    @Schema(name = "productCode", title = "产品code")
    private String productCode;

    /**
     * 子产品code
     */
    @Schema(name = "subProductCode", title = "子产品code")
    private String subProductCode;

    /**
     * 计费项code
     */
    @Schema(name = "billingItemCode", title = "计费项code")
    private String billingItemCode;

    /**
     * 子计费项code
     */
    @Schema(name = "subBillingItemCode", title = "子计费项code")
    private String subBillingItemCode;

    /**
     * 产品名称
     */
    @Schema(name = "productName", title = "产品名称")
    private String productName;

    /**
     * 子产品名称
     */
    @Schema(name = "subProductName", title = "子产品名称")
    private String subProductName;

    /**
     * 计费项名称
     */
    @Schema(name = "billingItemName", title = "计费项名称")
    private String billingItemName;

    /**
     * 子计费项名称
     */
    @Schema(name = "subBillingItemName", title = "子计费项名称")
    private String subBillingItemName;

    /**
     * 单位，个、次、GB等
     */
    @Schema(name = "unit", title = "单位，个、次、GB等")
    private String unit;

    /**
     * 价格
     */
    @Schema(name = "price", title = "价格")
    private BigDecimal price;

    /**
     * 计费规格
     */
    @Schema(name = "chargeSize", title = "计费规格")
    private BigDecimal chargeSize;

    /**
     * 生效状态（1生效 0实效）
     */
    @Schema(name = "status", title = "生效状态（1生效 0实效）")
    private Short status;

    /**
     * 备注
     */
    @Schema(name = "remark", title = "备注")
    private String remark;

    @Min(value = 1, message = "{page.index}", groups = {Groups.PageGroup.class})
    private Long pageIndex;

    @Min(value = 1, message = "{page.pageSize}", groups = {Groups.PageGroup.class})
    private Long pageSize;

    private Long total;



}

