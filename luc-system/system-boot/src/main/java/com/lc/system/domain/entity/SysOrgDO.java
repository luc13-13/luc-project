package com.lc.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 组织架构表(luc_system.sys_org)实体类
 *
 * @author lucheng
 * @since 2025-12-13
 */
@Data
@TableName("luc_system.sys_org")
public class SysOrgDO implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 组织ID
     */
    @TableField("org_id")
    private String orgId;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;

    /**
     * 父级组织ID
     */
    @TableField("parent_org_id")
    private String parentOrgId;

    /**
     * 组织名称
     */
    @TableField("org_name")
    private String orgName;

    /**
     * 组织层级
     */
    @TableField("org_level")
    private Integer orgLevel;

    /**
     * 排序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 状态(0禁用 1启用)
     */
    @TableField("status")
    private Boolean status;

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

    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField("deleted")
    private Boolean deleted;
}
