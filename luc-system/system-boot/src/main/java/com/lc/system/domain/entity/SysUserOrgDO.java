package com.lc.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户组织关联表(luc_system.sys_user_org)实体类
 *
 * @author lucheng
 * @since 2025-12-13
 */
@Data
@TableName("luc_system.sys_user_org")
public class SysUserOrgDO implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 组织ID
     */
    @TableField("org_id")
    private String orgId;

    /**
     * 是否主组织(0否 1是)
     */
    @TableField("is_primary")
    private Boolean isPrimary;

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
}
