package com.lc.system.web;

import com.lc.framework.datascope.entity.DataScopeEntity;
import com.lc.system.domain.bo.OAuth2Profile;
import com.lc.system.domain.dto.UserDTO;
import com.lc.system.mapper.SysUserMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/test")
@Tag(name = "测试接口",description = "用于测试网关路由功能")
public class TestController {
    @Autowired
    private SysUserMapper sysUserMapper;
    @GetMapping("/hello")
    public String test1(){
        return "hello test-luc1";
    }

    @GetMapping("/scope")
    public String testDataScope() {
        DataScopeEntity dataScopeEntity = UserDTO.builder()
                .roleIds(Arrays.asList("1", "2"))
                .userId("1")
                .build();
        OAuth2Profile sysUserDO = sysUserMapper.queryRolePermission("2", dataScopeEntity);
        return "";
    }

    @GetMapping("")
    public String index(){
        return "welcome index";
    }
}
