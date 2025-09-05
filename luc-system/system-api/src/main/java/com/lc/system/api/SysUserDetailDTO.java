package com.lc.system.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
public class SysUserDetailDTO implements Serializable {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private String userId;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    private String deptId;

    /**
     * 用户账号, 用于登陆
     */
    @Schema(description = "用户账号, 用于登陆")
    private String userName;

    /**
     * 用户昵称
     */
    @Schema(name = "nickName", title = "用户昵称")
    private String nickName;

    /**
     * 用户类型, 00系统用户, 10核心账号, 20子账号
     */
    @Schema(description = "用户类型, 00系统用户, 10核心账号, 20子账号")
    private String userType;

    /**
     * 用户邮箱, 用于登陆
     */
    @Schema(description = "用户邮箱, 用于登陆")
    private String email;

    /**
     * 手机号码, 用于登陆
     */
    @Schema(description = "手机号码, 用于登陆")
    private String phone;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @Schema(description = "用户性别（0男 1女 2未知）")
    private Short sex;

    /**
     * 头像地址
     */
    @Schema(description = "头像地址")
    private String avatar;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Schema(description = "帐号状态（0正常 1停用）")
    private Boolean status;

    /**
     * bcrypt加密后的密码
     */
    @Schema(description = "bcrypt加密后的密码")
    private String password;

    /**
     * 角色及其关联权限
     */
    @Schema(description = "角色及其关联权限")
    private Map<String, List<String>> roleAuthoritiesMap;

}

