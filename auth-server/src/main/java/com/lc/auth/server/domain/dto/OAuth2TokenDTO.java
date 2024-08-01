package com.lc.auth.server.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
 * @date 2024/1/18 9:40
 */
@Data
@Schema(name = "OAuth2TokenDTO", title = "获取token成功后返回的结果")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2TokenDTO {

    @Schema(name = "accessToken", title = "访问令牌")
    private String accessToken;

    @Schema(name = "accessTokenExpiredAt", title = "访问令牌过期时间戳，定义见Instant.toEpochMilli, 前端请求接口前根据该时间判断是否重新获取")
    @JsonSerialize(using = ToStringSerializer.class)
    private long accessTokenExpiredAt;

    @Schema(name = "refreshToken", title = "用于访问令牌过期时重新获取")
    private String refreshToken;

    @Schema(name = "refreshTokenExpiredAt", title = "刷新令牌过期时间戳，定义见Instant.toEpochMilli，前端请求接口前根据该时间判断是否重新登录")
    @JsonSerialize(using = ToStringSerializer.class)
    private long refreshTokenExpiredAt;

    @Schema(name = "tokenType", title = "token类型")
    private String tokenType;

}
