package com.lc.system.domain.dto;

import com.lc.framework.core.utils.validator.Groups.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统角色表(luc_system.sys_role)表数据传输类
 *
 * @author lucheng
 * @since 2025-09-04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleDTO implements Serializable {
    /**
     * 主键ID
     */
    @NotBlank(message = "id不能为空", groups = { UpdateGroup.class, DeleteGroup.class })
    private Integer id;

    /**
     * 角色ID
     */
    @NotBlank(message = "roleId不能为空", groups = { AddGroup.class, UpdateGroup.class, DeleteGroup.class })
    private String roleId;

    /**
     * 角色名称
     */
    @NotBlank(message = "roleName不能为空", groups = AddGroup.class)
    private String roleName;

    /**
     * 角色描述
     */
    @NotBlank(message = "", groups = AddGroup.class)
    private String description;

    /**
     * 状态(0:禁用 1:启用)
     */
    private Boolean status;

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date dtCreated;

    /**
     * 更新者
     */
    private String modifiedBy;

    /**
     * 更新时间
     */
    private Date dtModified;

    /**
     * 逻辑删除(0:未删除 1:已删除)
     */
    private Boolean deleted;

    /**
     * 关联的菜单ID列表
     */
    private List<String> menuIds;

}
