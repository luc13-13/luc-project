package com.lc.framework.security.auth.server.jackson;

import com.fasterxml.jackson.annotation.*;
import com.lc.framework.security.core.user.LoginUserDetail;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 31/12/25 15:27
 * @version : 1.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class UsernamePasswordTokenMixIn {
    @JsonCreator
    public UsernamePasswordTokenMixIn(@JsonProperty("principal") LoginUserDetail principal, @JsonProperty("credential") Object credentials) {

    }
}
