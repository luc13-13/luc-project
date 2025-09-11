package com.lc.system.domain.dto;

import com.lc.framework.data.permission.entity.DataScopeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <pre>
 *     用户详情, 包含权限、角色
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/28 16:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UserDTO", description = "用户信息DTO")
public class UserDTO implements DataScopeEntity {

    private String username;

    private List<String> permissions;

    private String userId;

    private String currentRole;

    private String currentDeptId;

    private String currentRegionId;

    private List<String> roleIds;

    private List<String> permissionIds;

    private List<String> deptIds;

    private List<String> regionIds;
}
