package com.lc.system.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.system.domain.dto.MenuDTO;
import com.lc.system.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单相关接口
 *
 * @author Lu Cheng
 * @date 2023/12/27 17:03
 */
@Tag(name = "菜单相关接口")
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @Operation(summary = "获取用户菜单树")
    @GetMapping("/all")
    public WebResult<List<MenuDTO>> getAllMenus(HttpServletRequest request) {
        List<MenuDTO> menuTree = menuService.getMenuTreeByUserId(request.getHeader("X-User-Id"));
        return WebResult.success(menuTree);
    }

    @Operation(summary = "获取所有菜单树（管理后台使用）")
    @GetMapping("/admin/tree")
    public WebResult<List<MenuDTO>> getAllMenuTree() {
        List<MenuDTO> menuTree = menuService.getAllMenuTree();
        return WebResult.success(menuTree);
    }
}
