package com.lc.system.domain.bo;

import lombok.Data;

import java.util.Set;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-11-16 10:15
 */
@Data
public class OAuth2Profile {
    private String userId;

    private String loginName;

    private Set<String> permissionSet;

    private Set<String> roleSet;
}
