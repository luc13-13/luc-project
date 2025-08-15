package com.lc.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表(luc_system.sys_user)表数据传输类
 *
 * @author lucheng
 * @since 2025-08-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SysUserDTO")
public class SysUserDTO implements Serializable {

    /**
     * 用户ID
     */
    @Schema(name = "userId", title = "用户ID")
    private String userId;

    /**
     * 部门ID
     */
    @Schema(name = "deptId", title = "部门ID")
    private String deptId;

    /**
     * 用户账号, 用于登陆
     */
    @Schema(name = "userName", title = "用户账号, 用于登陆")
    private String userName;

    /**
     * 用户昵称
     */
    @Schema(name = "nickName", title = "用户昵称")
    private String nickName;

    /**
     * 用户类型, 00系统用户, 10核心账号, 20子账号
     */
    @Schema(name = "userType", title = "用户类型, 00系统用户, 10核心账号, 20子账号")
    private String userType;

    /**
     * 用户邮箱, 用于登陆
     */
    @Schema(name = "email", title = "用户邮箱, 用于登陆")
    private String email;

    /**
     * 手机号码, 用于登陆
     */
    @Schema(name = "phone", title = "手机号码, 用于登陆")
    private String phone;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @Schema(name = "sex", title = "用户性别（0男 1女 2未知）")
    private Short sex;

    /**
     * 头像地址
     */
    @Schema(name = "avatar", title = "头像地址")
    private String avatar;

    /**
     * 密码，bcrypt加密后的数据
     */
    @Schema(name = "password", title = "密码，bcrypt加密后的数据")
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Schema(name = "status", title = "帐号状态（0正常 1停用）")
    private Short status;

    /**
     * 最后登录IP
     */
    @Schema(name = "loginIp", title = "最后登录IP")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @Schema(name = "loginDate", title = "最后登录时间")
    private Date loginDate;

    /**
     * 创建者
     */
    @Schema(name = "createdBy", title = "创建者")
    private String createdBy;

    /**
     * 创建时间
     */
    @Schema(name = "dtCreated", title = "创建时间")
    private Date dtCreated;

    /**
     * 更新者
     */
    @Schema(name = "modifiedBy", title = "更新者")
    private String modifiedBy;

    /**
     * 更新时间
     */
    @Schema(name = "dtModified", title = "更新时间")
    private Date dtModified;

    /**
     * 备注
     */
    @Schema(name = "remark", title = "备注")
    private String remark;

    /**
     * 逻辑删除(0:未删除 1:已删除)
     */
    @Schema(name = "deleted", title = "逻辑删除(0:未删除 1:已删除)")
    private Short deleted;


}

