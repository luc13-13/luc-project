package com.lc.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 租户配额表(luc_system.sys_tenant_quota)实体类
 *
 * @author lucheng
 * @since 2025-12-13
 */
@Data
@TableName("luc_system.sys_tenant_quota")
public class SysTenantQuotaDO implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;

    /**
     * 产品代码
     */
    @TableField("product_code")
    private String productCode;

    /**
     * 配额数量
     */
    @TableField("quota")
    private Integer quota;

    /**
     * 已使用数量
     */
    @TableField("used")
    private Integer used;

    /**
     * 过期时间
     */
    @TableField("expire_date")
    private Date expireDate;

    /**
     * 创建者
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "dt_created", fill = FieldFill.INSERT)
    private Date dtCreated;

    /**
     * 更新者
     */
    @TableField(value = "modified_by", fill = FieldFill.UPDATE)
    private String modifiedBy;

    /**
     * 更新时间
     */
    @TableField(value = "dt_modified", fill = FieldFill.UPDATE)
    private Date dtModified;
}
