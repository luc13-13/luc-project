package com.lc.authorization.server.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "LoginUserInfoDTO", description = "登录用户信息")
public class LoginUserInfoDTO {

    @Schema(name = "username",  description = "用户名，手机号、邮箱、用户名", example = "admin")
    private String username;

    @Schema(name = "roles",  description = "角色列表", example = "{1,2,3}")
    private List<String> roles;

    @Schema(name = "permissions",  description = "权限列表", example = "{read,write}")
    private List<String> permissions;

    @Schema(name = "redirectUrl",  description = "重定向地址", example = "https://www.baidu.com")
    private String redirectUrl;
}
