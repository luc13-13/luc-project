package com.lc.system.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.system.domain.dto.SysRoleDTO;
import com.lc.system.domain.vo.RoleInfoVO;
import com.lc.system.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/9/4 10:42
 * @version : 1.0
 */
@Tag(name = "角色")
@RestController
@RequestMapping("/role")
public class RoleController {

    private final SysRoleService sysRoleService;

    public RoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @Operation(summary = "角色列表")
    @GetMapping("/list")
    public WebResult<List<RoleInfoVO>> list() {
        return WebResult.success(sysRoleService.getRoleList());
    }

    @Operation(summary = "新增、更新角色")
    @PostMapping("/save")
    public WebResult<String> save(@RequestBody @Validated() SysRoleDTO dto) {
        return WebResult.success(sysRoleService.saveRole(dto));
    }

    @Operation(summary = "角色详情")
    @GetMapping("/detail")
    public WebResult<RoleInfoVO> detail(@RequestParam("roleId") @NotBlank(message = "roleId不能为空") String roleId) {
        return WebResult.success(sysRoleService.getRoleDetails(roleId));
    }

    @Operation(summary = "删除角色")
    @PostMapping("/delete")
    public WebResult<String> delete(@RequestBody @Validated SysRoleDTO dto) {
        return WebResult.success(sysRoleService.deleteRole(dto.getRoleId()));
    }
}
