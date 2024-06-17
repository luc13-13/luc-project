package com.lc.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2023/11/14 21:03
 * @version : 1.0
 */

@Data
@TableName("sys_user")
public class SysUserDO{
    /** 用户ID */
    @TableId("user_id")
    private Long userId;
    /** 部门ID */
    @TableField("dept_id")
    private Long deptId;
    /** 用户账号 */
    @TableField("user_name")
    private String userName;
    /** 用户昵称 */
    @TableField("nick_name")
    private String nickName;
    /** 用户类型（00系统用户） */
    @TableField("user_type")
    private String userType;
    /** 用户邮箱 */
    @TableField("email")
    private String email;
    /** 手机号码 */
    @TableField("phone_number")
    private String phoneNumber;
    /** 用户性别（0男 1女 2未知） */
    @TableField("sex")
    private String sex;
    /** 头像地址 */
    @TableField("avatar")
    private String avatar;
    /** 密码 */
    @TableField("password")
    private String password;
    /** 帐号状态（0正常 1停用） */
    @TableField("status")
    private String status;
    /** 删除标志（0代表存在 2代表删除） */
    @TableField("del_flag")
    private String delFlag;
    /** 最后登录IP */
    @TableField("login_ip")
    private String loginIp;
    /** 最后登录时间 */
    @TableField("login_date")
    private Date loginDate;
    /** 创建者 */
    @TableField("create_by")
    private String createBy;
    /** 创建时间 */
    @TableField("create_time")
    private Date createTime;
    /** 更新者 */
    @TableField("update_by")
    private String updateBy;
    /** 更新时间 */
    @TableField("update_time")
    private Date updateTime;
    /** 备注 */
    @TableField("remark")
    private String remark;
}


