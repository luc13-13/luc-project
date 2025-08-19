package com.lc.system.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * <pre>
 *     前端路由信息
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/18 10:54
 * @version : 1.0
 */
@Data
@Builder
@Schema(description = "前端信息")
public class MenuVO {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(name = "id", title = "uid")
    private Long id;
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
     * 排序号
     */
    @Schema(name = "sortOrder", title = "排序号")
    private Integer sortOrder;

    @Schema(name = "meta", title = "菜单元数据")
    private MenuMetaVO meta;

    @Schema(name = "children", title = "子菜单")
    private List<MenuVO> children;
}
