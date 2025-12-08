package com.lc.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <pre>
 * 
 * <pre/>
 * 
 * @author : Lu Cheng
 * @date : 2025/9/4 10:42
 * @version : 1.0
 */
@Data
@Builder
@Schema(description = "角色信息")
public class RoleInfoVO {

    private Integer id;

    private String roleId;

    private String roleName;

    private String description;

    private Boolean status;

    private Date dtCreated;

    @Schema(description = "角色绑定的菜单ID列表")
    private List<String> menuIds;
}
