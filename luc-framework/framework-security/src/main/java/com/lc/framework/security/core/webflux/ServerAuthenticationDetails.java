package com.lc.framework.security.core.webflux;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * <pre>
 *     自定义登录后的AuthenticationDetails类，解决Spring-webflux中HttpServletRequest无法序列化问题
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/1/11 16:13
 */
@JsonInclude
public record ServerAuthenticationDetails(String remoteAddress, String sessionId) implements Serializable {
    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @JsonCreator
    public ServerAuthenticationDetails {
    }

    /**
     * Indicates the TCP/IP address the authentication request was received from.
     *
     * @return the address
     */
    @Override
    public String remoteAddress() {
        return this.remoteAddress;
    }

    /**
     * Indicates the <code>HttpSession</code> id the authentication request was received
     * from.
     *
     * @return the session ID
     */
    @Override
    public String sessionId() {
        return this.sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServerAuthenticationDetails that = (ServerAuthenticationDetails) o;
        return Objects.equals(this.remoteAddress, that.remoteAddress) && Objects.equals(this.sessionId, that.sessionId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(" [");
        sb.append("RemoteIpAddress=").append(this.remoteAddress()).append(", ");
        sb.append("SessionId=").append(this.sessionId()).append("]");
        return sb.toString();
    }
}
