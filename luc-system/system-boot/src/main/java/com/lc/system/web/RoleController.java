package com.lc.system.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.system.domain.vo.RoleInfoVO;
import com.lc.system.service.SysRoleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/9/4 10:42
 * @version : 1.0
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    private final SysRoleService sysRoleService;

    public RoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @GetMapping("/list")
    public WebResult<List<RoleInfoVO>> list(HttpServletRequest request) {
        return WebResult.success(sysRoleService.getRoleList());
    }
}
