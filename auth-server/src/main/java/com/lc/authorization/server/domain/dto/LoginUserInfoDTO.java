package com.lc.authorization.server.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.util.List;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/20 15:20
 */
@Data
@Tag(name = "LoginUserInfoDTO", description = "登录用户信息")
public class LoginUserInfoDTO {
    @ApiModelProperty(name = "username",  example = "用户名，手机号、邮箱、用户名")
    private String username;
    private List<String> roles;
    private List<String> permissions;
    private String redirectUrl;
}
