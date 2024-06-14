package com.lc.authorization.server.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * <pre>
 *     拓展OAuth2授权方式时， 在此参数上进行拓展
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023/12/12 15:30
 */
@Data
@Schema(name = "OAuth2AuthorizationRequestDTO", title = "OAuth2 授权端点请求参数")
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2AuthorizationRequestDTO {

    private String client_id;

    private String client_secret;

    private Set<String> scopes;
}
