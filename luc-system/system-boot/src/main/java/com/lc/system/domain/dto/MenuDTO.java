package com.lc.system.domain.dto;

import com.lc.framework.core.utils.validator.Groups.AddGroup;
import com.lc.framework.core.utils.validator.Groups.DeleteGroup;
import com.lc.framework.core.utils.validator.Groups.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
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

    @NotNull(message = "{menu.dto.id.notBlank}", groups =  {UpdateGroup.class, DeleteGroup.class})
    @Null(message = "{menu.dto.id.null}", groups =  AddGroup.class)
    private Long id;

    @Schema(name = "userId", description = "可以查看菜单的userId")
    private String userId;
    /**
     * 菜单唯一标识
     */
    @NotBlank(message = "{menu.dto.menuId.notBlank}", groups =  {AddGroup.class, DeleteGroup.class})
    @Schema(name = "menuId", description = "菜单唯一标识")
    private String menuId;

    /**
     * 父级菜单ID
     */
    @Schema(name = "parentMenuId", description = "父级菜单ID")
    private String parentMenuId;

    /**
     * 路由名称(必须唯一)
     */
    @NotBlank(groups = AddGroup.class)
    @Schema(name = "name", description = "路由名称(必须唯一)")
    private String name;

    /**
     * 路由路径
     */
    @Schema(name = "path", description = "路由路径")
    private String path;

    /**
     * 组件路径(字符串)
     */
    @Schema(name = "component", description = "组件路径(字符串)")
    private String component;

    /**
     * 重定向路径
     */
    @Schema(name = "redirect", description = "重定向路径")
    private String redirect;

    /**
     * 菜单类型(catalog/menu/button/embedded/link)
     */
    @NotBlank(groups = AddGroup.class)
    @Schema(name = "menuType", description = "菜单类型(catalog/menu/button/embedded/link)")
    private String menuType;

    /**
     * 状态(0:禁用 1:启用)
     */
    @Schema(name = "status", description = "状态(0:禁用 1:启用)")
    private Short status;

    /**
     * 排序号
     */
    @Schema(name = "sortOrder", description = "排序号")
    private Integer sortOrder;

    /**
     * 创建者
     */
    @Schema(name = "createdBy", description = "创建者")
    private String createdBy;

    /**
     * 创建时间
     */
    @Schema(name = "dtCreated", description = "创建时间")
    private Date dtCreated;

    /**
     * 更新者
     */
    @Schema(name = "modifiedBy", description = "更新者")
    private String modifiedBy;

    /**
     * 更新时间
     */
    @Schema(name = "dtModified", description = "更新时间")
    private Date dtModified;

    @NotNull(groups =  AddGroup.class)
    @Schema(name = "meta", description = "菜单元数据")
    private MenuMetaDTO meta;

    @Schema(name = "children", description = "子菜单")
    private List<MenuDTO> children;


    @Schema(name = "menuTypeNotIn", description = "菜单类型条件")
    private List<String> menuTypeNotIn;
}

