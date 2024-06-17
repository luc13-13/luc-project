package com.lc.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单权限表(SysMenu)表实体类
 *
 * @author lucheng
 * @since 2023-12-27 16:39:20
 */
@Data
@TableName("sys_menu")
public class SysMenuDO implements Serializable {
    /**
     * 菜单ID
     */     
    @Id
    @TableField("menu_id")
    private Long menuId;

    /**
     * 菜单名称
     */    
    @TableField("menu_name")
    private String menuName;
    
    /**
     * 父菜单ID
     */    
    @TableField("parent_id")
    private Long parentId;
    
    /**
     * 显示顺序
     */    
    @TableField("order_num")
    private Integer orderNum;
    
    /**
     * 路由地址
     */    
    @TableField("path")
    private String path;
    
    /**
     * 组件路径
     */    
    @TableField("component")
    private String component;
    
    /**
     * 路由参数
     */    
    @TableField("query")
    private String query;
    
    /**
     * 是否为外链（0是 1否）
     */    
    @TableField("is_frame")
    private Integer isFrame;
    
    /**
     * 是否缓存（0缓存 1不缓存）
     */    
    @TableField("is_cache")
    private Integer isCache;
    
    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */    
    @TableField("menu_type")
    private String menuType;
    
    /**
     * 菜单状态（0显示 1隐藏）
     */    
    @TableField("visible")
    private String visible;
    
    /**
     * 菜单状态（0正常 1停用）
     */    
    @TableField("status")
    @TableLogic(value = "0", delval = "1")
    private String status;
    
    /**
     * 权限标识
     */    
    @TableField("perms")
    private String perms;
    
    /**
     * 菜单图标
     */    
    @TableField("icon")
    private String icon;
    
    /**
     * 创建者
     */    
    @TableField("create_by")
    private String createBy;
    
    /**
     * 创建时间
     */    
    @TableField("create_time")
    private Date createTime;
    
    /**
     * 更新者
     */    
    @TableField("update_by")
    private String updateBy;
    
    /**
     * 更新时间
     */    
    @TableField("update_time")
    private Date updateTime;
    
    /**
     * 备注
     */    
    @TableField("remark")
    private String remark;
    
}

