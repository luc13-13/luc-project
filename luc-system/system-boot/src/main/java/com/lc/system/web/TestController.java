package com.lc.system.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.datascope.entity.DataScopeEntity;
import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.system.domain.bo.OAuth2Profile;
import com.lc.system.domain.dto.UserDTO;
import com.lc.system.mapper.SysUserMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 * @author Lu Cheng
 */
@RestController
@RequestMapping("/test")
@Tag(name = "测试接口",description = "用于测试网关路由功能")
@AllArgsConstructor
public class TestController {

    private SysUserMapper sysUserMapper;

    private StorageClientTemplate storageClientTemplate;

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

    @PostMapping("upload")
    public WebResult<String> upload(@RequestParam("file") MultipartFile file) {
        storageClientTemplate.upload(null, null, file);
        return WebResult.success();
    }
}
