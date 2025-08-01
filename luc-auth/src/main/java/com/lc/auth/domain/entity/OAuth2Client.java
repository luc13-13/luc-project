package com.lc.auth.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <pre>
 * OAuth2客户端实体类
 * 用于存储OAuth2客户端注册信息
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oauth2_registered_client")
public class OAuth2Client {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 客户端ID
     */
    @TableField("client_id")
    private String clientId;

    /**
     * 客户端ID发布时间
     */
    @TableField("client_id_issued_at")
    private LocalDateTime clientIdIssuedAt;

    /**
     * 客户端密钥
     */
    @TableField("client_secret")
    private String clientSecret;

    /**
     * 客户端密钥过期时间
     */
    @TableField("client_secret_expires_at")
    private LocalDateTime clientSecretExpiresAt;

    /**
     * 客户端名称
     */
    @TableField("client_name")
    private String clientName;

    /**
     * 客户端认证方法（JSON格式）
     */
    @TableField("client_authentication_methods")
    private String clientAuthenticationMethods;

    /**
     * 授权类型（JSON格式）
     */
    @TableField("authorization_grant_types")
    private String authorizationGrantTypes;

    /**
     * 重定向URI（JSON格式）
     */
    @TableField("redirect_uris")
    private String redirectUris;

    /**
     * 后重定向URI（JSON格式）
     */
    @TableField("post_logout_redirect_uris")
    private String postLogoutRedirectUris;

    /**
     * 授权范围（JSON格式）
     */
    @TableField("scopes")
    private String scopes;

    /**
     * 客户端设置（JSON格式）
     */
    @TableField("client_settings")
    private String clientSettings;

    /**
     * Token设置（JSON格式）
     */
    @TableField("token_settings")
    private String tokenSettings;

    /**
     * 状态：0-禁用，1-启用
     */
    @TableField("status")
    private Integer status;

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
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 更新者
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

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
