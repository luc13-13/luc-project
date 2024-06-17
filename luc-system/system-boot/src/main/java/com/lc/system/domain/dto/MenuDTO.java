package com.lc.system.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/20 15:31
 */
@Data
@Builder
@Schema(name = "MenuDTO", title = "封装用户菜单数据")
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO {
    private String redirect;

    private MenuMeta meta;
    /**
     * 菜单ID
     */
    @Id
    @Schema(name = "menuId", title = "菜单id")
    private Long menuId;

    /**
     * 菜单名称
     */
    @Schema(name = "menuName", title = "菜单名")
    private String name;

    /**
     * 父菜单ID
     */
    @Schema(name = "parentId", title = "父级菜单")
    private Long parentId;

    /**
     * 显示顺序
     */
    @TableField("order_num")
    @Schema(name = "orderNum", title = "菜单序号")
    private Integer orderNum;

    /**
     * 路由地址
     */
    @TableField("path")
    @Schema(name = "path", title = "菜单对应的访问路径")
    private String path;

    /**
     * 组件路径
     */
    @TableField("component")
    @Schema(name = "component", title = "菜单对应的组件路径")
    private String component;

    /**
     * 路由参数
     */
    @TableField("query")
    @Schema(name = "query", title = "访问菜单携带的路由参数")
    private String query;

    /**
     * 是否为外链（0是 1否）
     */
    @Schema(name = "isFrame", title = "是否为外链（0是 1否）")
    private Boolean isFrame;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    @Schema(name = "isCache", title = "是否缓存（0缓存 1不缓存）")
    private Boolean isCache;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @Schema(name = "menuType", title = "菜单类型（M目录 C菜单 F按钮）")
    private String menuType;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    @Schema(name = "visible", title = "菜单状态（0显示 1隐藏）")
    private Boolean visible;

    /**
     * 权限标识
     */
    @Schema(name = "permissions", title = "权限标识")
    private String permissions;

    /**
     * 菜单图标
     */
    @Schema(name = "icon", title = "菜单图标")
    private String icon;

    @Schema(name = "children", title = "子菜单列表")
    private List<MenuDTO> children;
}
