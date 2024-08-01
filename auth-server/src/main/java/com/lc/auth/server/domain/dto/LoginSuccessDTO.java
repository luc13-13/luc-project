package com.lc.auth.server.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/19 14:49
 */
@Data
@Schema(name = "LoginSuccessDTO", title = "登录成功后返回的结果")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginSuccessDTO {
    @Schema(name = "username", title = "用户名")
    private String username;

    @Schema(name = "jsessionid", title = "jsessionid")
    private String jsessionid;

    @Schema(name = "accessToken", title = "accessToken" )
    private String accessToken;

    @Schema(name = "refreshToken", title = "refreshToken" )
    private String refreshToken;

    @Schema(name = "token", title = "token" )
    private String token;
}
