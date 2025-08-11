package com.lc.authorization.gateway.config;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/8 16:45
 * @version : 1.0
 */
public record AuthResponse(boolean valid, boolean expired, String message, String username) {
}
