package com.lc.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表(luc_system.sys_user)表实体类
 *
 * @author lucheng
 * @since 2025-08-15
 */
@Data
@TableName("luc_system.sys_user")
public class SysUserDO implements Serializable {
    /**
     * 主键id
     */
    @TableId("id")
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 部门ID
     */
    @TableField("dept_id")
    private String deptId;

    /**
     * 用户账号, 用于登陆
     */
    @TableField("user_name")
    private String userName;

    /**
     * 用户昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 用户类型, 00系统用户, 10核心账号, 20子账号
     */
    @TableField("user_type")
    private String userType;

    /**
     * 用户邮箱, 用于登陆
     */
    @TableField("email")
    private String email;

    /**
     * 手机号码, 用于登陆
     */
    @TableField("phone")
    private String phone;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @TableField("sex")
    private Short sex;

    /**
     * 头像地址
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 密码，bcrypt加密后的数据
     */
    @TableField("password")
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    @TableField("status")
    private Boolean status;

    /**
     * 最后登录IP
     */
    @TableField("login_ip")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @TableField("login_date")
    private Date loginDate;

    /**
     * 创建者
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField("dt_created")
    private Date dtCreated;

    /**
     * 更新者
     */
    @TableField("modified_by")
    private String modifiedBy;

    /**
     * 更新时间
     */
    @TableField("dt_modified")
    private Date dtModified;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 逻辑删除(0:未删除 1:已删除)
     */
    @TableField("deleted")
    private Short deleted;

}

