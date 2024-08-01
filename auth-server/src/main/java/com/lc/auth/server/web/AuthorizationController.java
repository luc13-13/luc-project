package com.lc.auth.server.web;

import com.lc.auth.server.domain.dto.LoginUserInfoDTO;
import com.lc.auth.server.domain.dto.MenuDTO;
import com.lc.framework.core.mvc.WebResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/20 15:17
 */
@Tag(name = "用户认证接口", description = "用户认证接口")
@Slf4j
@RequestMapping("")
@RestController
public class AuthorizationController {

    @Operation(description = "获取当前登录的用户信息")
    @GetMapping("/getInfo")
    public WebResult<LoginUserInfoDTO> getInfo() {
        LoginUserInfoDTO userInfoDTO = new LoginUserInfoDTO();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userInfoDTO.setUsername(authentication.getName());
        log.info("获取当前登录用户信息：{}", userInfoDTO);
        return WebResult.successData(userInfoDTO);
    }

    @GetMapping("/menu/getRouters")
    public WebResult<List<MenuDTO>> getMenuList() {
        List<MenuDTO> menuDTOList = new ArrayList<>();

//        menuDTOList.add(MenuDTO.builder().path("/").component("ParentView").build());
        menuDTOList.add(MenuDTO.builder().build());
        return WebResult.successData(menuDTOList);
    }
}
