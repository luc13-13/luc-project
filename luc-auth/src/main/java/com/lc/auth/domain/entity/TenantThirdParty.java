package com.lc.auth.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <pre>
 * 租户第三方账号绑定实体类
 * 用于存储租户与第三方账号（gitee、wechat、github等）的绑定关系
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_tenant_third_party")
public class TenantThirdParty {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;

    /**
     * 第三方平台类型：gitee、wechat、github、qq等
     */
    @TableField("provider")
    private String provider;

    /**
     * 第三方平台用户唯一标识
     */
    @TableField("provider_user_id")
    private String providerUserId;

    /**
     * 第三方平台用户名
     */
    @TableField("provider_username")
    private String providerUsername;

    /**
     * 第三方平台用户昵称
     */
    @TableField("provider_nickname")
    private String providerNickname;

    /**
     * 第三方平台用户头像
     */
    @TableField("provider_avatar")
    private String providerAvatar;

    /**
     * 第三方平台用户邮箱
     */
    @TableField("provider_email")
    private String providerEmail;

    /**
     * 第三方平台返回的原始用户信息（JSON格式）
     */
    @TableField("provider_raw_info")
    private String providerRawInfo;

    /**
     * 绑定状态：0-未绑定，1-已绑定，2-已解绑
     */
    @TableField("bind_status")
    private Integer bindStatus;

    /**
     * 绑定时间
     */
    @TableField("bind_time")
    private LocalDateTime bindTime;

    /**
     * 解绑时间
     */
    @TableField("unbind_time")
    private LocalDateTime unbindTime;

    /**
     * 最后登录时间
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 登录次数
     */
    @TableField("login_count")
    private Long loginCount;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
