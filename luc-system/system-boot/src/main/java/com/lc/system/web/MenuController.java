package com.lc.system.web;

import com.lc.framework.core.constants.RequestHeaderConstants;
import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.core.utils.validator.Groups;
import com.lc.framework.web.utils.MessageUtils;
import com.lc.system.domain.dto.MenuDTO;
import com.lc.system.domain.vo.MenuVO;
import com.lc.system.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lc.framework.core.constants.NumberConstants.STATUS_TRUE;
import static com.lc.framework.core.constants.RequestHeaderConstants.USER_ID;

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
    @GetMapping("/tree/available")
    public WebResult<List<MenuVO>> getMenuTreeAvailable(HttpServletRequest request) {
        MenuDTO queryDTO = MenuDTO.builder()
                .userId(request.getHeader(RequestHeaderConstants.USER_ID))
                .status(STATUS_TRUE)
                .build();
        List<MenuVO> menuTree = menuService.getRouteTreeByUserId(queryDTO);
        return WebResult.success(menuTree);
    }

    @Operation(summary = "获取用户菜单列表（前端路由使用）")
    @GetMapping("/list")
    public WebResult<List<MenuVO>> getMenuList(HttpServletRequest request) {
        MenuDTO queryDTO = MenuDTO.builder().userId(request.getHeader(USER_ID)).build();
        List<MenuVO> menuTree = menuService.getMenuVOList(queryDTO);
        return WebResult.success(menuTree);
    }

    @Operation(summary = "保存菜单")
    @PostMapping("/save")
    public WebResult<String> saveMenu(@RequestBody @Validated(Groups.AddGroup.class) MenuDTO dto) {
        menuService.saveMenu(dto);
        return WebResult.success(MessageUtils.getMessage("menu.api.save.success"));
    }

    @Operation(summary = "更新菜单")
    @PostMapping("/update")
    public WebResult<String> updateMenu(@RequestBody @Validated(Groups.UpdateGroup.class) MenuDTO dto) {
        return WebResult.success(MessageUtils.getMessage("menu.api.update.success"));
    }
}
