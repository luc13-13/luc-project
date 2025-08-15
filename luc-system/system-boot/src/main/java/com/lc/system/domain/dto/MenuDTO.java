package com.lc.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统菜单表(luc_system.menu)表数据传输类
 *
 * @author lucheng
 * @since 2025-08-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "MenuDTO")
public class MenuDTO implements Serializable {

    /**
     * 菜单唯一标识
     */
    @Schema(name = "menuId", title = "菜单唯一标识")
    private String menuId;

    /**
     * 父级菜单ID
     */
    @Schema(name = "parentMenuId", title = "父级菜单ID")
    private String parentMenuId;

    /**
     * 路由名称(必须唯一)
     */
    @Schema(name = "name", title = "路由名称(必须唯一)")
    private String name;

    /**
     * 路由路径
     */
    @Schema(name = "path", title = "路由路径")
    private String path;

    /**
     * 组件路径(字符串)
     */
    @Schema(name = "component", title = "组件路径(字符串)")
    private String component;

    /**
     * 重定向路径
     */
    @Schema(name = "redirect", title = "重定向路径")
    private String redirect;

    /**
     * 菜单类型(catalog/menu/button/embedded/link)
     */
    @Schema(name = "menuType", title = "菜单类型(catalog/menu/button/embedded/link)")
    private String menuType;

    /**
     * 状态(0:禁用 1:启用)
     */
    @Schema(name = "status", title = "状态(0:禁用 1:启用)")
    private Short status;

    /**
     * 排序号
     */
    @Schema(name = "sortOrder", title = "排序号")
    private Integer sortOrder;

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

    @Schema(name = "meta", title = "菜单元数据")
    private MenuMetaDTO meta;

    @Schema(name = "children", title = "子菜单")
    private List<MenuDTO> children;
}

